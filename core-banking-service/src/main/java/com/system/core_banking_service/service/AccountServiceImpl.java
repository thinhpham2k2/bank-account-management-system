package com.system.core_banking_service.service;

import com.system.common_library.dto.request.CreateAccountDTO;
import com.system.common_library.dto.request.UpdateAccountDTO;
import com.system.common_library.dto.response.AccountDTO;
import com.system.core_banking_service.entity.Account;
import com.system.core_banking_service.mapper.AccountMapper;
import com.system.core_banking_service.repository.AccountRepository;
import com.system.core_banking_service.service.interfaces.AccountService;
import com.system.core_banking_service.util.Constant;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final MessageSource messageSource;

    private final AccountMapper accountMapper;

    private final AccountRepository accountRepository;

    @Override
    public AccountDTO findById(String accountNumber) {

        Optional<Account> account = accountRepository.findByAccountNumberAndStatus(accountNumber, true);

        return account.map(accountMapper::entityToDTO).orElse(null);
    }

    @Override
    public AccountDTO create(CreateAccountDTO create) {

        try {

            if (accountRepository.existsByAccountId(create.getAccountId())) {

                throw new InvalidParameterException(
                        messageSource.getMessage(
                                Constant.DUPLICATE_ACCOUNT_ID, null, LocaleContextHolder.getLocale()));
            }
            if (accountRepository.existsByAccountNumber(create.getAccountNumber())) {

                throw new InvalidParameterException(
                        messageSource.getMessage(
                                Constant.DUPLICATE_ACCOUNT_NUMBER, null, LocaleContextHolder.getLocale()));
            }

            Account account = accountMapper.createToEntity(create);
            return accountMapper.entityToDTO(accountRepository.save(account));
        } catch (Exception e) {

            throw new InvalidParameterException(e instanceof InvalidParameterException ? e.getMessage() :
                    messageSource.getMessage(Constant.CREATE_FAIL, null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public AccountDTO update(UpdateAccountDTO update, String accountNumber) {

        Optional<Account> entity = accountRepository.findByAccountNumberAndStatus(accountNumber, true);
        if (entity.isPresent()) {

            try {

                Account account = accountMapper.updateToEntity(update, entity.get());
                return accountMapper.entityToDTO(accountRepository.save(account));
            } catch (Exception e) {

                throw new InvalidParameterException(
                        messageSource.getMessage(
                                Constant.UPDATE_FAIL, null, LocaleContextHolder.getLocale()));
            }
        } else {

            throw new InvalidParameterException(
                    messageSource.getMessage(Constant.INVALID_ACCOUNT, null, LocaleContextHolder.getLocale()));
        }
    }
}
