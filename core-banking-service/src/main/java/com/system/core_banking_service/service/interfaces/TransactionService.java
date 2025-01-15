package com.system.core_banking_service.service.interfaces;

import com.system.common_library.dto.request.CreateExternalTransactionDTO;
import com.system.common_library.dto.request.CreateInternalTransactionDTO;
import com.system.common_library.dto.request.CreateSystemTransactionDTO;
import com.system.common_library.dto.response.TransactionDTO;

public interface TransactionService {

    TransactionDTO createExternal(CreateExternalTransactionDTO create);

    TransactionDTO createInternal(CreateInternalTransactionDTO create);

    TransactionDTO createSystem(CreateSystemTransactionDTO create);
}
