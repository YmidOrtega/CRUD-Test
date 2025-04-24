package com.crudtest.test.module.user.mapper;

import com.crudtest.test.module.user.dto.UserInformationDTO;
import com.crudtest.test.module.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserInformationMapper {
    @Mappings({
            @Mapping(source = "planId.name", target = "plan")
            , @Mapping(source = "roleId.name", target = "role")
    })
    UserInformationDTO toUserInformationDTO(User user);

}
