package com.team.printo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team.printo.dto.UserDTO;
import com.team.printo.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(target = "email", source = "email")
	@Mapping(target = "id", source = "id")
	UserDTO toDTO(User user);
	
	@Mapping(target = "email", source = "email")
	@Mapping(target = "id", source = "id")
    User toEntity(UserDTO userDTO);

}
