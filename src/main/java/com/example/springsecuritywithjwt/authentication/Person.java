package com.example.springsecuritywithjwt.authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Person {

	private long id;
	private String username;
	@JsonIgnore
	private String password;
	private String email;
	private Integer age;
	private Boolean enabled;
	private String roles;
}
