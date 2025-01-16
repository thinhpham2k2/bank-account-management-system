package com.system.common_library.dto.transaction;

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
    private String senderAccountId;
    private String senderAccount;
    private AccountType senderAccountType;
    private String senderAccountName;
    private String receiverAccountId;
    private String receiverAccount;
    private AccountType receiverAccountType;
    private String receiverAccountName;
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
