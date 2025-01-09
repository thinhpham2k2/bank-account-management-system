package com.system.transaction_service.controller;

import com.system.common_library.enums.*;
import com.system.transaction_service.dto.detail.TransactionDetailDTO;
import com.system.transaction_service.dto.response.PagedDTO;
import com.system.transaction_service.service.interfaces.TransactionDetailService;
import com.system.transaction_service.util.Constant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "\uD83D\uDCB1 Transaction API")
@RequestMapping("/api/v1/transactions")
@SecurityRequirement(name = "Authorization")
public class TransactionController {

    private final MessageSource messageSource;

    private final TransactionDetailService transactionDetailService;

    @GetMapping("")
    @Operation(summary = "Get transactions list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = PagedDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
    })
    public ResponseEntity<?> getList(@RequestParam(defaultValue = "") String search,
                                     @RequestParam(defaultValue = "0") BigDecimal amountStart,
                                     @RequestParam(defaultValue = "1000000000") BigDecimal amountEnd,
                                     @Parameter(description = "<b>Filter by direction<b>")
                                     @RequestParam(required = false) List<Direction> directionList,
                                     @Parameter(description = "<b>Filter by fee payer<b>")
                                     @RequestParam(required = false) List<FeePayer> feePayerList,
                                     @Parameter(description = "<b>Filter by initiator<b>")
                                     @RequestParam(required = false) List<Initiator> initiatorList,
                                     @Parameter(description = "<b>Filter by method<b>")
                                     @RequestParam(required = false) List<Method> methodList,
                                     @Parameter(description = "<b>Filter by transaction type<b>")
                                     @RequestParam(required = false) List<TransactionType> transactionTypeList,
                                     @Parameter(description = "<b>Filter by internal transaction type<b>")
                                     @RequestParam(required = false) List<Type> typeList,
                                     @RequestParam(defaultValue = "id,desc") String sort,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "10") Integer limit)
            throws MethodArgumentTypeMismatchException {

        PagedDTO<TransactionDetailDTO> list = transactionDetailService.findAllByCondition(
                directionList, feePayerList, initiatorList, methodList, transactionTypeList, typeList,
                search, amountStart, amountEnd, sort, page, limit);

        if (!list.getContent().isEmpty()) {

            return ResponseEntity.status(HttpStatus.OK).body(list);
        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.TEXT_PLAIN).body(
                    messageSource.getMessage(Constant.NOT_FOUND, null, LocaleContextHolder.getLocale()));
        }
    }
}
