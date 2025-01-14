package com.system.common_library.service;

import java.time.LocalDateTime;

public interface NotificationDubboService {

    boolean sendOtpCode(String email, LocalDateTime expireTime);
}
