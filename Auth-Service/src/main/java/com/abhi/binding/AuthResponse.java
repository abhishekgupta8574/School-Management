package com.abhi.binding;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class AuthResponse {
	private String token;
	private String loginValid;
}
