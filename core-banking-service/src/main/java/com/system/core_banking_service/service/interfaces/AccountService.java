package com.system.core_banking_service.service.interfaces;

import com.system.common_library.dto.request.CreateAccountDTO;
import com.system.common_library.dto.request.UpdateAccountDTO;
import com.system.common_library.dto.response.AccountDTO;

public interface AccountService {

    AccountDTO findById(String accountNumber);

    AccountDTO create(CreateAccountDTO create);

    AccountDTO update(UpdateAccountDTO update, String accountNumber);
}
