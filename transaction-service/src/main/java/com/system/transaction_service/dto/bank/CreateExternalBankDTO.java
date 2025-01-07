package com.system.transaction_service.dto.bank;

import com.system.transaction_service.util.Constant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateExternalBankDTO implements Serializable {

    @NotNull(message = "{" + Constant.BANK_NAME_REQUIRE + "}")
    @Size(min = 2, max = 255, message = "{" + Constant.BANK_NAME_SIZE + "}")
    private String name;

    @NotNull(message = "{" + Constant.SHORT_NAME_REQUIRE + "}")
    @Size(min = 2, max = 255, message = "{" + Constant.SHORT_NAME_SIZE + "}")
    private String shortName;

    @NotNull(message = "{" + Constant.BANK_CODE_REQUIRE + "}")
    @Size(min = 2, max = 50, message = "{" + Constant.BANK_CODE_SIZE + "}")
    private String code;

    @NotNull(message = "{" + Constant.NAPAS_CODE_REQUIRE + "}")
    @Size(min = 2, max = 50, message = "{" + Constant.NAPAS_CODE_SIZE + "}")
    private String napasCode;

    @NotNull(message = "{" + Constant.SWIFT_CODE_REQUIRE + "}")
    @Size(min = 2, max = 50, message = "{" + Constant.SWIFT_CODE_SIZE + "}")
    private String swiftCode;

    @NotNull(message = "{" + Constant.IS_AVAILABLE_REQUIRE + "}")
    private Boolean isAvailable;

    private String contactInfo;

    private MultipartFile logo;

    private String description;
}
