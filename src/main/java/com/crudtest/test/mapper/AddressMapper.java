package com.crudtest.test.mapper;

import com.crudtest.test.dto.AddressDTO;
import com.crudtest.test.model.Address;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface AddressMapper {
    Address toAddress(AddressDTO addressDTO);
    AddressDTO toAddressDTO(Address address);
}
