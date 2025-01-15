package com.system.core_banking_service.service;

import com.system.common_library.dto.request.CreateExternalTransactionDTO;
import com.system.common_library.dto.request.CreateInternalTransactionDTO;
import com.system.common_library.dto.request.CreateSystemTransactionDTO;
import com.system.common_library.dto.response.TransactionDTO;
import com.system.common_library.enums.Direction;
import com.system.core_banking_service.entity.Account;
import com.system.core_banking_service.entity.Transaction;
import com.system.core_banking_service.mapper.TransactionMapper;
import com.system.core_banking_service.repository.AccountRepository;
import com.system.core_banking_service.repository.TransactionRepository;
import com.system.core_banking_service.service.interfaces.TransactionService;
import com.system.core_banking_service.util.Constant;
import de.huxhorn.sulky.ulid.ULID;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final MessageSource messageSource;

    private final TransactionMapper transactionMapper;

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    @Override
    public TransactionDTO createExternal(CreateExternalTransactionDTO create) {

        try {

            // Get account
            Optional<Account> account = accountRepository.findByAccountNumberAndStatus
                    (create.getAccountNumber(), true);

            // Check account
            if (account.isPresent() && account.get().getIsActive()
                    && create.getAmount().compareTo(BigDecimal.ZERO) != 0) {

                if (create.getFee().compareTo(BigDecimal.ZERO) > 0) {

                    // Get master account
                    Optional<Account> master = accountRepository.findByAccountNumberAndStatus
                            (create.getMasterAccountNumber(), true);

                    if (master.isPresent()) {

                        BigDecimal fee = create.getFee();

                        // Create master transaction
                        Transaction transaction = Transaction.builder()
                                .id(new ULID().nextULID())
                                .account(master.get())
                                .amount(fee)
                                .previousBalance(master.get().getBalance())
                                .currentBalance(master.get().getBalance().add(fee))
                                .availableBalance(master.get().getAvailableBalance().add(fee))
                                .direction(Direction.RECEIVE)
                                .note(create.getNote())
                                .description(create.getDescription())
                                .state(true)
                                .status(true)
                                .build();

                        // Update master account balance
                        master.get().setLastTransactionDate(LocalDateTime.now());
                        master.get().setBalance(master.get().getBalance().add(fee));
                        master.get().setAvailableBalance(master.get().getAvailableBalance().add(fee));
                        accountRepository.save(master.get());
                    } else {

                        throw new InvalidParameterException(
                                messageSource.getMessage(
                                        Constant.INVALID_MASTER_ACCOUNT, null, LocaleContextHolder.getLocale()));
                    }
                }

                BigDecimal amount = create.getAmount();
                boolean isSender = amount.compareTo(BigDecimal.ZERO) < 0;
                BigDecimal balance = account.get().getAvailableBalance();

                // Check balance
                if (isSender && balance.compareTo(amount.negate()) < 0) {

                    throw new InvalidParameterException(
                            messageSource.getMessage(
                                    Constant.INSUFFICIENT_BALANCE, null, LocaleContextHolder.getLocale()));
                }

                // Create transaction
                Transaction transaction = Transaction.builder()
                        .id(new ULID().nextULID())
                        .account(account.get())
                        .amount(amount)
                        .previousBalance(account.get().getBalance())
                        .currentBalance(account.get().getBalance().add(amount))
                        .availableBalance(account.get().getAvailableBalance().add(amount))
                        .direction(isSender ? Direction.RECEIVE : Direction.SEND)
                        .note(create.getNote())
                        .description(create.getDescription())
                        .state(true)
                        .status(true)
                        .build();

                // Update account balance
                account.get().setLastTransactionDate(LocalDateTime.now());
                account.get().setBalance(account.get().getBalance().add(amount));
                account.get().setAvailableBalance(account.get().getAvailableBalance().add(amount));
                accountRepository.save(account.get());

                return transactionMapper.entityToDTO(transactionRepository.save(transaction));
            } else {

                throw new InvalidParameterException(
                        messageSource.getMessage(
                                Constant.INVALID_ACCOUNT, null, LocaleContextHolder.getLocale()));
            }
        } catch (Exception e) {

            throw new InvalidParameterException(e instanceof InvalidParameterException ? e.getMessage() :
                    messageSource.getMessage(Constant.CREATE_FAIL, null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public TransactionDTO createInternal(CreateInternalTransactionDTO create) {

        try {

            // Get sender account
            Optional<Account> sender = accountRepository.findByAccountNumberAndStatus
                    (create.getSenderAccountNumber(), true);

            // Check sender account
            if (sender.isPresent() && sender.get().getIsActive()
                    && create.getSenderAmount().compareTo(BigDecimal.ZERO) < 0) {

                // Get receiver account
                Optional<Account> receiver = accountRepository.findByAccountNumberAndStatus
                        (create.getReceiverAccountNumber(), true);

                // Check receiver account
                if (receiver.isPresent() && receiver.get().getIsActive()
                        && create.getReceiverAmount().compareTo(BigDecimal.ZERO) > 0) {

                    if (create.getFee().compareTo(BigDecimal.ZERO) > 0) {

                        // Get master account
                        Optional<Account> master = accountRepository.findByAccountNumberAndStatus
                                (create.getMasterAccountNumber(), true);

                        if (master.isPresent()) {

                            BigDecimal fee = create.getFee();

                            // Create master transaction
                            Transaction transaction = Transaction.builder()
                                    .id(new ULID().nextULID())
                                    .account(master.get())
                                    .amount(fee)
                                    .previousBalance(master.get().getBalance())
                                    .currentBalance(master.get().getBalance().add(fee))
                                    .availableBalance(master.get().getAvailableBalance().add(fee))
                                    .direction(Direction.RECEIVE)
                                    .note(create.getNote())
                                    .description(create.getDescription())
                                    .state(true)
                                    .status(true)
                                    .build();

                            // Update master account balance
                            master.get().setLastTransactionDate(LocalDateTime.now());
                            master.get().setBalance(master.get().getBalance().add(fee));
                            master.get().setAvailableBalance(master.get().getAvailableBalance().add(fee));
                            accountRepository.save(master.get());
                        } else {

                            throw new InvalidParameterException(
                                    messageSource.getMessage(
                                            Constant.INVALID_MASTER_ACCOUNT, null, LocaleContextHolder.getLocale()));
                        }
                    }

                    BigDecimal senderAmount = create.getSenderAmount();
                    BigDecimal receiverAmount = create.getReceiverAmount();

                    BigDecimal senderBalance = sender.get().getAvailableBalance();
                    BigDecimal receiverBalance = receiver.get().getAvailableBalance();

                    // Check balance
                    if (senderBalance.compareTo(senderAmount.negate()) < 0) {

                        throw new InvalidParameterException(
                                messageSource.getMessage(
                                        Constant.INSUFFICIENT_BALANCE, null, LocaleContextHolder.getLocale()));
                    }

                    // Create sender transaction
                    Transaction senderTransaction = Transaction.builder()
                            .id(new ULID().nextULID())
                            .account(sender.get())
                            .amount(senderAmount)
                            .previousBalance(sender.get().getBalance())
                            .currentBalance(sender.get().getBalance().add(senderAmount))
                            .availableBalance(sender.get().getAvailableBalance().add(senderAmount))
                            .direction(Direction.SEND)
                            .note(create.getNote())
                            .description(create.getDescription())
                            .state(true)
                            .status(true)
                            .build();

                    // Create receiver transaction
                    Transaction receiverTransaction = Transaction.builder()
                            .id(new ULID().nextULID())
                            .account(receiver.get())
                            .amount(receiverAmount)
                            .previousBalance(receiver.get().getBalance())
                            .currentBalance(receiver.get().getBalance().add(receiverAmount))
                            .availableBalance(receiver.get().getAvailableBalance().add(receiverAmount))
                            .direction(Direction.RECEIVE)
                            .note(create.getNote())
                            .description(create.getDescription())
                            .state(true)
                            .status(true)
                            .build();

                    transactionRepository.save(receiverTransaction);

                    // Update sender account balance
                    sender.get().setLastTransactionDate(LocalDateTime.now());
                    sender.get().setBalance(sender.get().getBalance().add(senderAmount));
                    sender.get().setAvailableBalance(sender.get().getAvailableBalance().add(senderAmount));

                    // Update receiver account balance
                    receiver.get().setLastTransactionDate(LocalDateTime.now());
                    receiver.get().setBalance(receiver.get().getBalance().add(receiverAmount));
                    receiver.get().setAvailableBalance(receiver.get().getAvailableBalance().add(receiverAmount));

                    accountRepository.saveAll(List.of(sender.get(), receiver.get()));

                    return transactionMapper.entityToDTO(transactionRepository.save(senderTransaction));

                } else {

                    throw new InvalidParameterException(
                            messageSource.getMessage(
                                    Constant.INVALID_RECEIVER_ACCOUNT, null, LocaleContextHolder.getLocale()));
                }
            } else {

                throw new InvalidParameterException(
                        messageSource.getMessage(
                                Constant.INVALID_SENDER_ACCOUNT, null, LocaleContextHolder.getLocale()));
            }
        } catch (Exception e) {

            throw new InvalidParameterException(e instanceof InvalidParameterException ? e.getMessage() :
                    messageSource.getMessage(Constant.CREATE_FAIL, null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public TransactionDTO createSystem(CreateSystemTransactionDTO create) {
        return null;
    }
}
