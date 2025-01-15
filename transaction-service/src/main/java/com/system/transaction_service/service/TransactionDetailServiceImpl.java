package com.system.transaction_service.service;

import com.system.common_library.dto.response.AccountDTO;
import com.system.common_library.enums.*;
import com.system.transaction_service.config.VaultConfig;
import com.system.transaction_service.dto.response.PagedDTO;
import com.system.transaction_service.dto.transaction.*;
import com.system.transaction_service.entity.ExternalTransaction;
import com.system.transaction_service.entity.InternalTransaction;
import com.system.transaction_service.entity.TransactionDetail;
import com.system.transaction_service.entity.TransactionState;
import com.system.transaction_service.mapper.TransactionDetailMapper;
import com.system.transaction_service.repository.ExternalBankRepository;
import com.system.transaction_service.repository.TransactionDetailRepository;
import com.system.transaction_service.repository.TransactionRepository;
import com.system.transaction_service.repository.TransactionStateRepository;
import com.system.transaction_service.service.interfaces.PagingService;
import com.system.transaction_service.service.interfaces.TransactionDetailService;
import com.system.transaction_service.util.Constant;
import de.huxhorn.sulky.ulid.ULID;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TransactionDetailServiceImpl implements TransactionDetailService {

    private final MessageSource messageSource;

    private final VaultConfig vaultConfig;

    private final RedisTemplate<String, Object> redisTemplate;

    private final TransactionDetailMapper transactionDetailMapper;

    private final PagingService pagingService;

    private final TransactionDetailRepository transactionDetailRepository;

    private final TransactionRepository transactionRepository;

    private final ExternalBankRepository externalBankRepository;

    private final TransactionStateRepository transactionStateRepository;

    @Override
    public TransactionExtraDTO findById(String id) {

        Optional<TransactionDetail> transaction = transactionDetailRepository.findByIdAndStatus(id, true);

        return transaction.map(transactionDetailMapper::entityToExtraDTO).orElse(null);
    }

    @Override
    public PagedDTO<TransactionDTO> findAllByCondition(
            List<Direction> directionList, List<FeePayer> feePayerList, List<Initiator> initiatorList,
            List<Method> methodList, List<TransactionType> transactionTypeList, List<Type> typeList,
            String search, BigDecimal amountStart, BigDecimal amountEnd, String sort, int page, int limit) {

        Pageable pageable = pagingService.getPageable(sort, page, limit, TransactionDetail.class);

        Page<TransactionDetail> pageResult = transactionDetailRepository.findAllByCondition(
                true, directionList, feePayerList, initiatorList, methodList, transactionTypeList, search,
                amountStart, amountEnd, pageable);

        pageResult = new PageImpl<>(pageResult.stream().filter(
                p -> (typeList.isEmpty() ||
                        (p.getTransaction() instanceof InternalTransaction i &&
                                typeList.contains(
                                        i.getType())))).toList(), pageResult.getPageable(), pageResult.getTotalElements());

        return new PagedDTO<>(pageResult.map(transactionDetailMapper::entityToDTO));
    }

    @Override
    public void createExternal(CreateExternalDTO create) {

        try {

            // Check OTP (Notification service)
            String key = Constant.CACHE_TRANSACTION_PREFIX + "otp:"
                    + create.getSenderAccount() + create.getReceiverAccount();
            Object otpFromRedis = redisTemplate.opsForValue().get(key);

            if (otpFromRedis == null || !otpFromRedis.toString().equals(create.getOtpCode())) {

                throw new InvalidParameterException(
                        messageSource.getMessage(Constant.INVALID_OTP_CODE, null, LocaleContextHolder.getLocale()));
            } else {

                // Check sender account validation (Core banking)
                AccountDTO senderAccount = AccountDTO.builder().build();

                // Check receiver account validation (Napas)
                AccountDTO receiverAccount = AccountDTO.builder().build();

                // Check master account (Core banking)
                AccountDTO masterAccount = AccountDTO.builder().build();

                boolean isPayFee = create.getFee().compareTo(BigDecimal.ZERO) > 0;
                if (senderAccount == null || !senderAccount.getIsActive() ||
                        receiverAccount == null || !receiverAccount.getIsActive() ||
                        (isPayFee && (masterAccount == null || !masterAccount.getIsActive()))) {

                    throw new InvalidParameterException(
                            messageSource.getMessage(
                                    Constant.INVALID_ACCOUNT, null, LocaleContextHolder.getLocale()));
                } else {

                    // Create external transaction
                    ExternalTransaction transaction = transactionDetailMapper.createToExternalEntity(create);
                    transaction.setExternalBank(
                            externalBankRepository.findByNapasCodeAndStatus(create.getNapasCode(), true)
                                    .orElseThrow(Exception::new));

                    // Create transaction detail
                    BigDecimal amount = create.getAmount().negate();

                    BigDecimal fee = create.getFeePayer().equals(FeePayer.SENDER) ? create.getFee() : BigDecimal.ZERO;

                    BigDecimal netAmount = amount.add(fee.negate());

                    if (senderAccount.getAvailableBalance().compareTo(netAmount.abs()) < 0) {

                        throw new InvalidParameterException(
                                messageSource.getMessage(
                                        Constant.INSUFFICIENT_BALANCE, null, LocaleContextHolder.getLocale()));
                    }

                    String id = new ULID().nextULID();
                    List<TransactionDetail> detailList = new ArrayList<>(List.of(
                            TransactionDetail.builder()
                                    .id(id)
                                    .account(create.getSenderAccount())
                                    .amount(amount)
                                    .fee(fee)
                                    .netAmount(netAmount)
                                    .previousBalance(senderAccount.getBalance())
                                    .currentBalance(senderAccount.getBalance().add(netAmount))
                                    .availableBalance(senderAccount.getAvailableBalance().add(netAmount))
                                    .direction(Direction.SEND)
                                    .referenceCode(null)
                                    .description(create.getDescription())
                                    .status(true)
                                    .build()));

                    if (isPayFee) {

                        detailList.add(TransactionDetail.builder()
                                .id(new ULID().nextULID())
                                .account(vaultConfig.getAccountNumber())
                                .amount(fee)
                                .fee(BigDecimal.ZERO)
                                .netAmount(fee)
                                .previousBalance(masterAccount.getBalance())
                                .currentBalance(masterAccount.getBalance().add(fee))
                                .availableBalance(masterAccount.getAvailableBalance().add(fee))
                                .direction(Direction.RECEIVE)
                                .referenceCode(id)
                                .description(create.getDescription())
                                .status(true)
                                .build());
                    }

                    // Set transaction detail
                    transaction.setTransactionDetailList(detailList);

                    // Set transaction state
                    List<TransactionState> stateList = List.of(
                            TransactionState.builder()
                                    .id(new ULID().nextULID())
                                    .state(State.PENDING)
                                    .description(State.PENDING.getDescription())
                                    .status(true)
                                    .build());
                    transaction.setTransactionStateList(stateList);

                    // Save transaction
                    ExternalTransaction externalTransaction = transactionRepository.save(transaction);

                    // Update sender account balance (Core banking)

                    // Update master account balance (Core banking)

                    // Update receiver account balance (Napas)
                    boolean isSuccess = new Random().nextBoolean();

                    // Update transaction state
                    transactionStateRepository.save(TransactionState.builder()
                            .id(new ULID().nextULID())
                            .transaction(externalTransaction)
                            .state(isSuccess ? State.COMPLETED : State.FAILED)
                            .description(isSuccess ? State.COMPLETED.getDescription() : State.FAILED.getDescription())
                            .status(true)
                            .build());

                    // Send event to notification service

                }
            }
        } catch (Exception e) {

            throw new InvalidParameterException(e instanceof InvalidParameterException ? e.getMessage() :
                    messageSource.getMessage(Constant.CREATE_FAIL, null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public void createInternal(CreateInternalDTO create) {

    }

    @Override
    public void createPayment(CreatePaymentDTO create) {

    }
}
