package com.system.transaction_service.controller;

import com.system.common_library.enums.*;
import com.system.transaction_service.dto.request.OTPRequestDTO;
import com.system.transaction_service.dto.response.PagedDTO;
import com.system.transaction_service.dto.transaction.*;
import com.system.transaction_service.service.interfaces.NotificationService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "\uD83D\uDCB1 Transaction API")
@RequestMapping("/api/v1/transactions")
@SecurityRequirement(name = "Authorization")
public class TransactionController {

    private final MessageSource messageSource;

    private final TransactionDetailService transactionDetailService;

    private final NotificationService notificationService;

    @GetMapping("")
    @Operation(summary = "Get transaction list")
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

        PagedDTO<TransactionDTO> list = transactionDetailService.findAllByCondition(
                Optional.ofNullable(directionList).orElse(List.of()), Optional.ofNullable(feePayerList).orElse(List.of()),
                Optional.ofNullable(initiatorList).orElse(List.of()), Optional.ofNullable(methodList).orElse(List.of()),
                Optional.ofNullable(transactionTypeList).orElse(List.of()), Optional.ofNullable(typeList).orElse(List.of()),
                search, amountStart, amountEnd, sort, page, limit);

        if (!list.getContent().isEmpty()) {

            return ResponseEntity.status(HttpStatus.OK).body(list);
        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.TEXT_PLAIN).body(
                    messageSource.getMessage(Constant.NOT_FOUND, null, LocaleContextHolder.getLocale()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = TransactionExtraDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "403", description = "Access Denied", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
    })
    public ResponseEntity<?> getById(@PathVariable(value = "id") String id)
            throws MethodArgumentTypeMismatchException {

        TransactionExtraDTO transaction = transactionDetailService.findById(id);

        if (transaction != null) {

            return ResponseEntity.status(HttpStatus.OK).body(transaction);
        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.TEXT_PLAIN).body(
                    messageSource.getMessage(Constant.NOT_FOUND, null, LocaleContextHolder.getLocale()));
        }
    }

    @PostMapping("/otp-code")
    @Operation(summary = "Send OTP code to email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "403", description = "Access Denied", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
    })
    public ResponseEntity<?> sendOtp(@RequestBody @Validated OTPRequestDTO request)
            throws MethodArgumentTypeMismatchException {

        // Send OTP code
        notificationService.sendOtpCode(request);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_PLAIN).body(
                messageSource.getMessage(Constant.SEND_OTP_SUCCESS, null, LocaleContextHolder.getLocale()));
    }

    @PostMapping("/external")
    @Operation(summary = "Create external transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "403", description = "Access Denied", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
    })
    public ResponseEntity<?> createExternal(@RequestBody @Validated CreateExternalDTO create)
            throws MethodArgumentTypeMismatchException {

        transactionDetailService.createExternal(create);

        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.TEXT_PLAIN).body(
                messageSource.getMessage(Constant.CREATE_SUCCESS, null, LocaleContextHolder.getLocale()));
    }

    @PostMapping("/internal")
    @Operation(summary = "Create internal transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "403", description = "Access Denied", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
    })
    public ResponseEntity<?> createInternal(@RequestBody @Validated CreateInternalDTO create)
            throws MethodArgumentTypeMismatchException {

        transactionDetailService.createInternal(create);

        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.TEXT_PLAIN).body(
                messageSource.getMessage(Constant.CREATE_SUCCESS, null, LocaleContextHolder.getLocale()));
    }

    @PostMapping("/payment")
    @Operation(summary = "Create payment transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "403", description = "Access Denied", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content =
                    {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
    })
    public ResponseEntity<?> createPayment(@RequestBody @Validated CreatePaymentDTO create)
            throws MethodArgumentTypeMismatchException {

        transactionDetailService.createPayment(create);

        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.TEXT_PLAIN).body(
                messageSource.getMessage(Constant.CREATE_SUCCESS, null, LocaleContextHolder.getLocale()));
    }
}
