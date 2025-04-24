package com.crudtest.test.module.user.mapper;

import com.crudtest.test.module.user.dto.AddressDTO;
import com.crudtest.test.module.user.model.Address;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface AddressMapper {
    Address toAddress(AddressDTO addressDTO);
    AddressDTO toAddressDTO(Address address);
}
