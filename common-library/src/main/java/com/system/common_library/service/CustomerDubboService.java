package com.system.common_library.service;

import com.system.common_library.dto.user.UserDetailDTO;

public interface CustomerDubboService {

    UserDetailDTO loadUserByUsername(String username);
}
