package com.system.napas_service.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {

        return new RestTemplate();
    }

    @Bean
    public AuditorAware<String> auditorAware() {

        return new AuditorAware<String>() {
            @Override
            public @NotNull Optional<String> getCurrentAuditor() {
                return Optional.empty();
            }
        };
    }
}
