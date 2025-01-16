package com.system.core_banking_service.mapper;

import com.system.common_library.dto.response.TransactionDTO;
import com.system.core_banking_service.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "accountId", source = "account.accountId")
    @Mapping(target = "accountNumber", source = "account.accountNumber")
    @Mapping(target = "customerId", source = "account.customerId")
    @Mapping(target = "customerName", source = "account.customerName")
    @Mapping(target = "customerPhone", source = "account.customerPhone")
    TransactionDTO entityToDTO(Transaction entity);
}
