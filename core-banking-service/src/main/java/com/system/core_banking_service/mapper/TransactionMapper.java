package com.system.core_banking_service.mapper;

import com.system.common_library.dto.response.TransactionDTO;
import com.system.core_banking_service.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionDTO entityToDTO(Transaction entity);
}
