package com.system.transaction_service.service.interfaces;

import com.system.common_library.dto.transaction.CreateExternalDTO;
import com.system.common_library.dto.transaction.CreateInternalDTO;
import com.system.common_library.dto.transaction.CreatePaymentDTO;
import com.system.common_library.dto.transaction.CreateSystemDTO;
import com.system.common_library.enums.*;
import com.system.transaction_service.dto.response.PagedDTO;
import com.system.transaction_service.dto.transaction.TransactionDTO;
import com.system.transaction_service.dto.transaction.TransactionExtraDTO;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionDetailService {

    TransactionExtraDTO findById(String id);

    PagedDTO<TransactionDTO> findAllByCondition(
            List<Direction> directionList, List<FeePayer> feePayerList, List<Initiator> initiatorList,
            List<Method> methodList, List<TransactionType> transactionTypeList, List<Type> typeList,
            String search, BigDecimal amountStart, BigDecimal amountEnd, String sort, int page, int limit);

    void createExternal(CreateExternalDTO create);

    void createInternal(CreateInternalDTO create);

    void createPayment(CreatePaymentDTO create);

    void createSystem(CreateSystemDTO create);
}
