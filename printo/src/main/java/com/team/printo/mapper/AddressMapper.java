package com.team.printo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team.printo.dto.AddressDTO;
import com.team.printo.model.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {

	@Mapping(target = "userId", source = "user.id")
	AddressDTO toDTO(Address address);
	
	@Mapping(target = "user.id", source = "userId")
	@Mapping(target = "orders", ignore = true)
	Address toEntity(AddressDTO address);
}