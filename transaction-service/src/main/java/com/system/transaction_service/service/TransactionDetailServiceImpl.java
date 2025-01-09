package com.system.transaction_service.service;

import com.system.common_library.enums.*;
import com.system.transaction_service.dto.detail.TransactionDetailDTO;
import com.system.transaction_service.dto.response.PagedDTO;
import com.system.transaction_service.entity.TransactionDetail;
import com.system.transaction_service.repository.TransactionDetailRepository;
import com.system.transaction_service.service.interfaces.PagingService;
import com.system.transaction_service.service.interfaces.TransactionDetailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TransactionDetailServiceImpl implements TransactionDetailService {

    private final MessageSource messageSource;

    private final PagingService pagingService;

    private final TransactionDetailRepository transactionDetailRepository;

    @Override
    public PagedDTO<TransactionDetailDTO> findAllByCondition(
            List<Direction> directionList, List<FeePayer> feePayerList, List<Initiator> initiatorList,
            List<Method> methodList, List<TransactionType> transactionTypeList, List<Type> typeList,
            String search, BigDecimal amountStart, BigDecimal amountEnd, String sort, int page, int limit) {

        Pageable pageable = pagingService.getPageable(sort, page, limit, TransactionDetail.class);

        try {


            Page<TransactionDetail> pageResult = transactionDetailRepository.findAllByCondition(
                    true, directionList, feePayerList, initiatorList, methodList, transactionTypeList, search,
                    amountStart, amountEnd, pageable);
        }catch (Exception e){

            System.out.println(e.getMessage());
        }

        return null;
    }
}
