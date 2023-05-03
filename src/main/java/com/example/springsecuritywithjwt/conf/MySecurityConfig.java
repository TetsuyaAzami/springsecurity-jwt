package com.example.springsecuritywithjwt.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.example.springsecuritywithjwt.authentication.MyUsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class MySecurityConfig {

	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration authenticationConfiguration) throws Exception {
		//
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/", "/api/login").permitAll()
				.anyRequest().authenticated());
		AuthenticationManager authenticationManager =
				authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));
		http.addFilter(new MyUsernamePasswordAuthenticationFilter(authenticationManager));
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.csrf().disable();

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		//
		return new BCryptPasswordEncoder(8);
	}
}
