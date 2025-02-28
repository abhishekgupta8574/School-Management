package com.abhi.mapper;



import org.springframework.stereotype.Component;

import com.abhi.dto.UserDto;
//import com.abhi.entity.Role;
import com.abhi.entity.UserCredential;

@Component
public class UserMapper {

	public static UserCredential getEntity(UserDto dto) {
		UserCredential user = new UserCredential();
		user.setEmail(dto.getEmail());
		user.setName(dto.getName());
		user.setPassword(dto.getPassword());
		user.setRole(dto.getRole());
		user.setTechStack(dto.getTechStack());
		return user;

	}
}
