package com.system.transaction_service.dto.transaction;

import com.system.common_library.enums.AccountType;
import com.system.common_library.enums.FeePayer;
import com.system.common_library.enums.Initiator;
import com.system.common_library.enums.Method;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateExternalDTO implements Serializable {

    private String napasCode;
    private String swiftCode;
    private String accountSenderId;
    private String accountSender;
    private AccountType accountSenderType;
    private String accountSenderName;
    private String accountReceiverId;
    private String accountReceiver;
    private AccountType accountReceiverType;
    private String accountReceiverName;
    private String transactionCode;
    private BigDecimal amount;
    private BigDecimal fee;
    private FeePayer feePayer;
    private String note;
    private String otpCode;
    private Initiator initiator;
    private Method method;
    private String description;
}
