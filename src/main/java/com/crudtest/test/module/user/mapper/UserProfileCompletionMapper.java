package com.crudtest.test.module.user.mapper;

import com.crudtest.test.module.user.dto.UserProfileCompletionDTO;
import com.crudtest.test.module.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface UserProfileCompletionMapper {

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "planId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "uuid", ignore = true)

    User updateUserFromDTO(UserProfileCompletionDTO dto, @MappingTarget User user);

}
