package com.crudtest.test.mapper;

import com.crudtest.test.dto.UserRegistrationDTO;
import com.crudtest.test.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper (componentModel = "spring")
public interface UserRegistrationMapper {
    @Mappings({
            @Mapping(target = "createdAt", expression = "java(java.time.LocalDate.now())"),
            @Mapping(target = "plan", ignore = true),     // Se setea después en el servicio
            @Mapping(target = "active", ignore = true),   // También en el servicio
            @Mapping(target = "id", ignore = true),       // Lo genera la base de datos
            @Mapping(target = "firstName", ignore = true),
            @Mapping(target = "lastName", ignore = true),
            @Mapping(target = "username", ignore = true),
            @Mapping(target = "birthDate", ignore = true),
            @Mapping(target = "phoneNumber", ignore = true),
            @Mapping(target = "address", ignore = true)
    })

    User toUser(UserRegistrationDTO userRegistrationDTO);
}
