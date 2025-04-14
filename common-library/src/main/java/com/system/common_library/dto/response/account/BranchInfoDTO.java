package com.system.common_library.dto.response.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchInfoDTO implements Serializable {
    private String branchId;
    private String branchName;
    private String address;
    private String description;
}
