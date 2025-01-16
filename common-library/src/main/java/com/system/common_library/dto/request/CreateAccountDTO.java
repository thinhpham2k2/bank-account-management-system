package com.system.common_library.dto.request;

import com.system.common_library.enums.AccountType;
import com.system.common_library.util.Constant;
import com.system.common_library.validation.interfaces.AccountTypeConstraint;
import com.system.common_library.validation.interfaces.AvailableBalanceConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AvailableBalanceConstraint
public class CreateAccountDTO implements Serializable {

    @NotNull(message = "{" + Constant.ACCOUNT_ID_REQUIRE + "}")
    @Size(min = 2, max = 255, message = "{" + Constant.ACCOUNT_ID_SIZE + "}")
    private String accountId;

    @NotNull(message = "{" + Constant.ACCOUNT_NUMBER_REQUIRE + "}")
    @Size(min = 2, max = 255, message = "{" + Constant.ACCOUNT_NUMBER_SIZE + "}")
    private String accountNumber;

    @Range(min = 0, max = 1000000000, message = "{" + Constant.INVALID_BALANCE + "}")
    private BigDecimal balance;

    @Range(min = 0, max = 1000000000, message = "{" + Constant.INVALID_AVAILABLE_BALANCE + "}")
    private BigDecimal availableBalance;

    @NotNull(message = "{" + Constant.CUSTOMER_ID_REQUIRE + "}")
    @Size(min = 2, max = 255, message = "{" + Constant.CUSTOMER_ID_SIZE + "}")
    private String customerId;

    private String customerName;

    private String customerPhone;

    @NotNull(message = "{" + Constant.CURRENCY_REQUIRE + "}")
    @Size(min = 1, max = 255, message = "{" + Constant.CURRENCY_SIZE + "}")
    private String currency;

    @NotNull(message = "{" + Constant.IS_ACTIVE_REQUIRE + "}")
    private Boolean isActive;

    @AccountTypeConstraint
    private AccountType type;

    private String description;
}
