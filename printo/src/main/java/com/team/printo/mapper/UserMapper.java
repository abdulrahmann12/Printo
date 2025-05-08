package com.team.printo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team.printo.dto.UserDTO;
import com.team.printo.dto.UserRegisterDTO;
import com.team.printo.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(target = "email", source = "email")
	@Mapping(target = "id", source = "id")
	UserDTO toDTO(User user);
	
	@Mapping(target = "email", source = "email")
	@Mapping(target = "id", source = "id")
    User toEntity(UserDTO userDTO);
	
	
	
	@Mapping(target = "email", source = "email")
	@Mapping(target = "id", source = "id")
	UserRegisterDTO toRegisterDTO(User user);
	
	@Mapping(target = "email", source = "email")
	@Mapping(target = "id", source = "id")
    User toEntity(UserRegisterDTO userDTO);
	
	
	@Mapping(target = "email", source = "email")
	@Mapping(target = "id", source = "id")
	List<UserDTO> toDTOS(List<User> users);
	
	@Mapping(target = "email", source = "email")
	@Mapping(target = "id", source = "id")
    List<User> toEntities(List<UserDTO> userDTOS);
}
