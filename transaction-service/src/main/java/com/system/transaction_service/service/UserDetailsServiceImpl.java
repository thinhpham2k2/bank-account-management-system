package com.system.transaction_service.service;

import com.system.transaction_service.dto.user.UserDetailCustom;
import com.system.transaction_service.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MessageSource messageSource;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserDetailCustom> result = Optional.empty();
        return result
                .orElseThrow(() -> new UsernameNotFoundException(
                        messageSource.getMessage(Constant.INVALID_ACCOUNT, null, LocaleContextHolder.getLocale())));
    }

}
