package com.system.common_library.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountType {
    PAYMENT("Tài khoản thanh toán"),
    SAVINGS("Tài khoản tiết kiệm"),
    LOAN("Tài khoản vay"),
    SALARY("Tài khoản lương"),
    BUSINESS("Tài khoản doanh nghiệp");

    private final String description;
}
