package com.system.transaction_service.service;

import com.system.transaction_service.dto.request.OTPRequestDTO;
import com.system.transaction_service.service.interfaces.NotificationService;
import com.system.transaction_service.util.OTPGenerator;
import lombok.RequiredArgsConstructor;
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

    @Override
    public void sendOtpCode(OTPRequestDTO request) {

        String otpCode = OTPGenerator.generateOTP(OTP_LENGTH);

        // Call send otp function by gRPC (Notification service)
        boolean isSucceed = new Random().nextBoolean();

        if (isSucceed) {

            String key = request.getAccountSender() + request.getAccountReceiver();
            redisTemplate.opsForValue().set(key, otpCode);
            redisTemplate.opsForValue().getAndExpire(key, Duration.ofMillis(60000L));
        }

        throw new InvalidParameterException();
    }
}
