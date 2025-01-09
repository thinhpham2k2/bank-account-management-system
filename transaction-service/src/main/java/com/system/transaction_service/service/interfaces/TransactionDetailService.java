package com.system.transaction_service.service.interfaces;

import com.system.common_library.enums.*;
import com.system.transaction_service.dto.detail.TransactionDetailDTO;
import com.system.transaction_service.dto.response.PagedDTO;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionDetailService {

    PagedDTO<TransactionDetailDTO> findAllByCondition(
            List<Direction> directionList, List<FeePayer> feePayerList, List<Initiator> initiatorList,
            List<Method> methodList, List<TransactionType> transactionTypeList, List<Type> typeList,
            String search, BigDecimal amountStart, BigDecimal amountEnd, String sort, int page, int limit);
}
