package com.system.common_library.dto.response;

import com.system.common_library.enums.AccountType;
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
public class AccountDTO implements Serializable {

    private String id;
    private String account;
    private BigDecimal balance;
    private AccountType accountType;
    private Boolean isActive;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
}
