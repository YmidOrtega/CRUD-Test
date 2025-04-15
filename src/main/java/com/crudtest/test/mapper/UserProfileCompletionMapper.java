package com.crudtest.test.mapper;

import com.crudtest.test.dto.UserProfileCompletionDTO;
import com.crudtest.test.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface UserProfileCompletionMapper {

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "plan", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "id", ignore = true)

    User updateUserFromDTO(UserProfileCompletionDTO dto, @MappingTarget User user);

}
