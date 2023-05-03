package com.example.springsecuritywithjwt.authentication;

import lombok.Data;

@Data
public class LoginForm {

	private String username;
	private String password;
}
