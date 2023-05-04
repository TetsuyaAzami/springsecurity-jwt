package com.example.springsecuritywithjwt.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import com.example.springsecuritywithjwt.authentication.MyAuthenticationUserDetailsService;
import com.example.springsecuritywithjwt.authentication.MyRequestHeaderAuthenticationFilter;
import com.example.springsecuritywithjwt.authentication.MyUserDetailsService;
import com.example.springsecuritywithjwt.authentication.MyUsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class MySecurityConfig {

	@Autowired
	public void configureProvider(AuthenticationManagerBuilder auth,
			MyUserDetailsService myUserDetailsService,
			MyAuthenticationUserDetailsService myAuthenticationUserDetailsService)
			throws Exception {
		//
		PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider =
				new PreAuthenticatedAuthenticationProvider();
		preAuthenticatedAuthenticationProvider
				.setPreAuthenticatedUserDetailsService(myAuthenticationUserDetailsService);
		preAuthenticatedAuthenticationProvider
				.setUserDetailsChecker(new AccountStatusUserDetailsChecker());
		auth.authenticationProvider(preAuthenticatedAuthenticationProvider);

		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(8));
		auth.authenticationProvider(daoAuthenticationProvider);
	}

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
		http.addFilter(new MyRequestHeaderAuthenticationFilter(authenticationManager));

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.csrf().disable();

		return http.build();
	}
}
