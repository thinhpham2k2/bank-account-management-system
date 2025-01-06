package com.system.common_library.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeePayer {
    SENDER("Người gửi chịu phí"),
    RECEIVER("Người nhận chịu phí");

    private final String description;
}
