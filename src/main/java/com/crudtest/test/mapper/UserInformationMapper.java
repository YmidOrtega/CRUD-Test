package com.crudtest.test.mapper;

import com.crudtest.test.dto.UserInformationDTO;
import com.crudtest.test.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserInformationMapper {
    @Mappings({
            @Mapping(source = "planId.name", target = "plan")
    })
    UserInformationDTO toUserInformationDTO(User user);

}
