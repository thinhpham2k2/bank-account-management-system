package com.system.transaction_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OTPRequestDTO implements Serializable {

    private String email;
    private String accountSender;
    private String accountReceiver;
}
