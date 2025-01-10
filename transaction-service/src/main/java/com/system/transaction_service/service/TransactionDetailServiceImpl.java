package com.system.transaction_service.service;

import com.system.common_library.enums.*;
import com.system.transaction_service.dto.response.PagedDTO;
import com.system.transaction_service.dto.transaction.*;
import com.system.transaction_service.entity.ExternalTransaction;
import com.system.transaction_service.entity.InternalTransaction;
import com.system.transaction_service.entity.TransactionDetail;
import com.system.transaction_service.mapper.TransactionDetailMapper;
import com.system.transaction_service.repository.TransactionDetailRepository;
import com.system.transaction_service.repository.TransactionRepository;
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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TransactionDetailServiceImpl implements TransactionDetailService {

    private final MessageSource messageSource;

    private final TransactionDetailMapper transactionDetailMapper;

    private final PagingService pagingService;

    private final TransactionDetailRepository transactionDetailRepository;

    private final TransactionRepository transactionRepository;

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
                                typeList.contains(i.getType())))).toList(), pageResult.getPageable(), pageResult.getTotalElements());

        return new PagedDTO<>(pageResult.map(transactionDetailMapper::entityToDTO));
    }

    @Override
    public void createExternal(CreateExternalDTO create) {

        try {

            // Check OTP (Notification service)

            // Check account sender validation (Core banking)

            // Check account receiver validation (Napas)

            // Create external transaction
            ExternalTransaction transaction = transactionDetailMapper.createToExternalEntity(create);

            // Create transaction detail
            BigDecimal fee = create.getFee();

            BigDecimal amount = (fee.compareTo(BigDecimal.ZERO) > 0 || create.getFeePayer().equals(FeePayer.SENDER)) ?
                    create.getAmount() : create.getAmount().add(fee.negate());

            BigDecimal netAmount = create.getFee();

            List<TransactionDetail> detailList = new ArrayList<>(List.of(
                    TransactionDetail.builder()
                            .id(new ULID().nextULID())
                            .account(create.getAccountSender())
                            .amount(amount)
                            .fee(fee)
                            .netAmount(netAmount)
                            .build()));

            if (transaction.getFee().compareTo(BigDecimal.ZERO) > 0)
                detailList.add(TransactionDetail.builder()
                        .build());

            // Set transaction detail
            transaction.setTransactionDetailList(detailList);

            // Save transaction
            transactionRepository.save(transaction);

            // Update account sender balance

            // Send event to notification service

        } catch (Exception e) {

            throw new InvalidParameterException(
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
