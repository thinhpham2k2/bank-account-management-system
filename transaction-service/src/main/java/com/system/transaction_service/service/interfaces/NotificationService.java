package com.system.transaction_service.service.interfaces;

import com.system.transaction_service.dto.request.OTPRequestDTO;

public interface NotificationService {

    void sendOtpCode(OTPRequestDTO request);
}
