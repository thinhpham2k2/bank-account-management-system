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
import com.system.transaction_service.service.interfaces.PagingService;
import com.system.transaction_service.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalBankServiceImpl implements ExternalBankService {

    private final MessageSource messageSource;

    private final ExternalBankMapper externalBankMapper;

    private final PagingService pagingService;

    private final ExternalBankRepository externalBankRepository;

    @Override
    public ExternalBankExtraDTO findById(String id) {

        Optional<ExternalBank> bank = externalBankRepository.findByIdAndStatus(id, true);

        return bank.map(externalBankMapper::entityToExtraDTO).orElse(null);
    }

    @Override
    public PagedDTO<ExternalBankDTO> findAllByCondition(Boolean isAvailable, String search, String sort, int page, int limit) {

        if (page < 0) throw new InvalidParameterException(
                messageSource.getMessage(Constant.INVALID_PAGE_NUMBER, null, LocaleContextHolder.getLocale()));
        if (limit < 1) throw new InvalidParameterException(
                messageSource.getMessage(Constant.INVALID_PAGE_SIZE, null, LocaleContextHolder.getLocale()));

        List<Sort.Order> order = new ArrayList<>();

        Set<String> sourceFieldList = pagingService.getAllFields(ExternalBank.class);
        String[] subSort = sort.split(",");
        if (pagingService.checkPropertyPresent(sourceFieldList, subSort[0])) {

            order.add(new Sort.Order(pagingService.getSortDirection(subSort[1]), subSort[0]));
        } else {

            throw new InvalidParameterException("{" + subSort[0] + "} " +
                    messageSource.getMessage(Constant.INVALID_PROPERTY, null, LocaleContextHolder.getLocale()));
        }

        Pageable pageable = PageRequest.of(page, limit).withSort(Sort.by(order));
        Page<ExternalBank> pageResult = externalBankRepository.findAllByCondition(true, isAvailable, search, pageable);

        return new PagedDTO<>(pageResult.map(externalBankMapper::entityToDTO));
    }

    @Override
    public void create(CreateExternalBankDTO create) {

        try {

            externalBankRepository.save(externalBankMapper.createToEntity(create));
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
    public void delete(String id) {

        Optional<ExternalBank> bank = externalBankRepository.findByIdAndStatus(id, true);
        if (bank.isPresent()) {

            try {

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
