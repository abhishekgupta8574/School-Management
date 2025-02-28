package com.abhi.dto;

import java.util.Set;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

	private String name;
	private String email;
	private String password;
	private String role;
	private String techStack;
}
