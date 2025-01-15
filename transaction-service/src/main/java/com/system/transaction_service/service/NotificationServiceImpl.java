package com.system.transaction_service.service;

import com.system.transaction_service.dto.request.OTPRequestDTO;
import com.system.transaction_service.service.interfaces.NotificationService;
import com.system.transaction_service.util.Constant;
import com.system.transaction_service.util.OTPGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final int OTP_LENGTH = 6;

    private final RedisTemplate<String, Object> redisTemplate;

    private final MessageSource messageSource;

//    @DubboReference
//    private final NotificationDubboService notificationDubboService;

    @Override
    public void sendOtpCode(OTPRequestDTO request) {

        String otpCode = OTPGenerator.generateOTP(OTP_LENGTH);

        // Call send otp function by gRPC (Notification service)
//        boolean isSucceed = notificationDubboService.sendOtpCode(request.getEmail(), LocalDateTime.now().plusSeconds(60));
        boolean isSucceed = new Random().nextBoolean();

        if (isSucceed) {

            String key = Constant.CACHE_TRANSACTION_PREFIX + "otp:"
                    + request.getSenderAccount() + request.getReceiverAccount();
            redisTemplate.opsForValue().set(key, otpCode);
            redisTemplate.opsForValue().getAndExpire(key, Duration.ofMillis(60000L));
        } else {

            throw new InvalidParameterException(
                    messageSource.getMessage(Constant.SEND_OTP_FAILED, null, LocaleContextHolder.getLocale()));
        }
    }
}
