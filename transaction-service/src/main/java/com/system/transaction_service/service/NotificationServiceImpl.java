package com.system.transaction_service.service;

import com.system.common_library.dto.notifcation.OTP;
import com.system.common_library.dto.response.transaction.TransactionNotificationDTO;
import com.system.common_library.service.NotificationDubboService;
import com.system.transaction_service.dto.request.OTPRequestDTO;
import com.system.transaction_service.service.interfaces.NotificationService;
import com.system.transaction_service.util.Constant;
import com.system.transaction_service.util.OTPGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final int OTP_LENGTH = 6;

    private final RedisTemplate<String, Object> redisTemplate;

    private final MessageSource messageSource;

    @DubboReference
    private final NotificationDubboService notificationDubboService;

    @Override
    public void sendOtpCode(OTPRequestDTO request) {

        log.info("Entering sendOtpCode with parameters: request = {}", request.toString());
        String otpCode = OTPGenerator.generateOTP(OTP_LENGTH);

        // Call send otp function by gRPC (Notification service)
//        boolean isSucceed = notificationDubboService.sendOtpCodeTransaction(
//                OTP.builder()
//                        .otp(otpCode)
//                        .expiredTime(LocalDateTime.now().plusSeconds(60)).build(),
//                request.getCifCode());

        boolean isSucceed = true;

        if (isSucceed) {

            String key = Constant.CACHE_TRANSACTION_PREFIX + "otp:" + request.getTransactionId();
            redisTemplate.opsForValue().set(key, otpCode);
            redisTemplate.opsForValue().getAndExpire(key, Duration.ofMillis(60000L));
        } else {

            throw new InvalidParameterException(
                    messageSource.getMessage(Constant.SEND_OTP_FAILED, null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public void sendTransactionNotification(TransactionNotificationDTO data) {

        log.info("Entering sendTransactionNotification with parameters: data = {}", data.toString());
        try {

//            notificationDubboService.sendTransactionNotification(data);
        } catch (Exception ignore) {

        }
    }
}
