package com.crudtest.test.module.user.mapper;

import com.crudtest.test.module.user.dto.AuthUserDTO;
import com.crudtest.test.module.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper (componentModel = "spring")
public interface UserRegistrationMapper {
    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "planId", ignore = true),
            @Mapping(target = "roleId", ignore = true),
            @Mapping(target = "active", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "firstName", ignore = true),
            @Mapping(target = "lastName", ignore = true),
            @Mapping(target = "username", ignore = true),
            @Mapping(target = "birthDate", ignore = true),
            @Mapping(target = "phoneNumber", ignore = true),
            @Mapping(target = "address", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "uuid", ignore = true),

    })

    User toUser(AuthUserDTO authUserDTO);
}
