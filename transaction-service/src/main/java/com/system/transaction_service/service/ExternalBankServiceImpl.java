package com.system.transaction_service.service;

import com.system.transaction_service.dto.bank.CreateExternalBankDTO;
import com.system.transaction_service.dto.bank.ExternalBankDTO;
import com.system.transaction_service.dto.bank.ExternalBankExtraDTO;
import com.system.transaction_service.dto.bank.UpdateExternalBankDTO;
import com.system.transaction_service.dto.response.PagedDTO;
import com.system.transaction_service.entity.ExternalBank;
import com.system.transaction_service.mapper.ExternalBankMapper;
import com.system.transaction_service.repository.ExternalBankRepository;
import com.system.transaction_service.service.interfaces.ExternalBankService;
import com.system.transaction_service.service.interfaces.FileService;
import com.system.transaction_service.service.interfaces.PagingService;
import com.system.transaction_service.util.Constant;
import de.huxhorn.sulky.ulid.ULID;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ExternalBankServiceImpl implements ExternalBankService {

    private final MessageSource messageSource;

    private final ExternalBankMapper externalBankMapper;

    private final PagingService pagingService;

    private final FileService fileService;

    private final ExternalBankRepository externalBankRepository;

    private static final String FOLDER_NAME = "Bank";

    @Override
    @Cacheable(cacheNames = "external_banks:detail", key = "#id")
    public ExternalBankExtraDTO findById(String id) {

        Optional<ExternalBank> bank = externalBankRepository.findByIdAndStatus(id, true);

        return bank.map(externalBankMapper::entityToExtraDTO).orElse(null);
    }

    @Override
    @Cacheable(cacheNames = "external_banks:list")
    public PagedDTO<ExternalBankDTO> findAllByCondition(
            Boolean isAvailable, String search, String sort, int page, int limit) {

        Pageable pageable = pagingService.getPageable(sort, page, limit, ExternalBank.class);
        Page<ExternalBank> pageResult = externalBankRepository.findAllByCondition(true, isAvailable, search, pageable);

        return new PagedDTO<>(pageResult.map(externalBankMapper::entityToDTO));
    }

    @Override
    public void create(CreateExternalBankDTO create) {

        try {

            ExternalBank bank = externalBankMapper.createToEntity(create);

            String fileName = FOLDER_NAME + "/" + new ULID().nextULID();
            String link = fileService.upload(create.getLogo(), fileName);
            if (!link.isBlank()) {

                bank.setLogo(link);
                bank.setLogoImageName(fileName);
            }

            externalBankRepository.save(bank);
        } catch (Exception e) {

            throw new InvalidParameterException(
                    messageSource.getMessage(Constant.CREATE_FAIL, null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public void update(UpdateExternalBankDTO update, String id) {

        Optional<ExternalBank> bank = externalBankRepository.findByIdAndStatus(id, true);
        if (bank.isPresent()) {

            try {

                if (!bank.get().getLogo().isBlank() && !bank.get().getLogoImageName().isBlank()) {

                    fileService.remove(bank.get().getLogoImageName());
                }

                String fileName = FOLDER_NAME + "/" + new ULID().nextULID();
                String link = fileService.upload(update.getLogo(), fileName);
                if (!link.isBlank()) {

                    bank.get().setLogo(link);
                    bank.get().setLogoImageName(fileName);
                }

                externalBankRepository.save(externalBankMapper.updateToEntity(update, bank.get()));
            } catch (Exception e) {

                throw new InvalidParameterException(
                        messageSource.getMessage(Constant.UPDATE_FAIL, null, LocaleContextHolder.getLocale()));
            }
        } else {

            throw new InvalidParameterException(
                    messageSource.getMessage(Constant.INVALID_EXTERNAL_BANK, null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    @CacheEvict(cacheNames = "external_banks:detail", key = "#id")
    public void delete(String id) {

        Optional<ExternalBank> bank = externalBankRepository.findByIdAndStatus(id, true);
        if (bank.isPresent()) {

            try {

                if (!bank.get().getLogo().isBlank() && !bank.get().getLogoImageName().isBlank()) {

                    fileService.remove(bank.get().getLogoImageName());
                }

                bank.get().setStatus(false);
                externalBankRepository.save(bank.get());
            } catch (Exception e) {

                throw new InvalidParameterException(
                        messageSource.getMessage(Constant.DELETE_FAIL, null, LocaleContextHolder.getLocale()));
            }
        } else {

            throw new InvalidParameterException(
                    messageSource.getMessage(Constant.INVALID_EXTERNAL_BANK, null, LocaleContextHolder.getLocale()));
        }
    }
}
