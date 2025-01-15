package com.system.core_banking_service.mapper;

import com.system.common_library.dto.request.CreateAccountDTO;
import com.system.common_library.dto.request.UpdateAccountDTO;
import com.system.common_library.dto.response.AccountDTO;
import com.system.core_banking_service.entity.Account;
import de.huxhorn.sulky.ulid.ULID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "account", source = "accountNumber")
    @Mapping(target = "accountType", source = "type")
    AccountDTO entityToDTO(Account entity);

    @Mapping(target = "id", expression = "java(mapId())")
    @Mapping(target = "state", expression = "java(true)")
    @Mapping(target = "status", expression = "java(true)")
    Account createToEntity(CreateAccountDTO create);

    Account updateToEntity(UpdateAccountDTO update, @MappingTarget Account entity);

    @Named("mapId")
    default String mapId() {

        return new ULID().nextULID();
    }
}
