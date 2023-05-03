package com.example.springsecuritywithjwt.authentication;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

	private final PersonRepository personRepository;

	public MyUserDetailsService(PersonRepository personRepository) {
		//
		this.personRepository = personRepository;
	}

	@Override
	public MyUserDetails loadUserByUsername(String username) {
		//
		Person searchCondition = new Person();
		searchCondition.setUsername(username);

		Person person = this.personRepository.findByCondition(searchCondition);
		if (person == null)
			throw new UsernameNotFoundException("ユーザ名またはパスワードが間違っています");

		return new MyUserDetails(person);
	}
}
