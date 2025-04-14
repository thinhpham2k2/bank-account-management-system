package com.system.common_library.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportType {
    ACCOUNT("Báo cáo tài khoản"),
    TRANSACTION("Báo cáo giao dịch"),
    LOAN("Báo cáo khoản vay"),
    TRANSACTION_LIST("Báo cáo danh sách giao dịch "),
    ACCOUNT_LIST("Báo cáo danh sách tài khoản"),
    LOAN_LIST("Báo cáo danh sách khoản vay");

    private final String description;
}
