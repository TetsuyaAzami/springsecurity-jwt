package com.example.springsecuritywithjwt.authentication;

import java.util.Arrays;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import lombok.Getter;

@Getter
public class MyUserDetails extends User {

	private Person person;

	public MyUserDetails(Person person) {
		//
		super(person.getUsername(), person.getPassword(),
				Arrays.asList(person.getRoles().split(",")).stream()
						.map(role -> new SimpleGrantedAuthority(role)).toList());
		this.person = person;
	}
}
