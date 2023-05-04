package com.example.springsecuritywithjwt.authentication;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyRequestHeaderAuthenticationFilter extends RequestHeaderAuthenticationFilter {

	public MyRequestHeaderAuthenticationFilter(AuthenticationManager authenticationManager) {
		//
		setPrincipalRequestHeader(HttpHeaders.AUTHORIZATION);
		setExceptionIfHeaderMissing(false);
		setAuthenticationManager(authenticationManager);
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/hello"));

		this.setAuthenticationSuccessHandler((request, response, authentication) -> {
			//
			log.info("Success2");
		});

		this.setAuthenticationFailureHandler((request, response, authentication) -> {
			//
			log.info("Failure2");
		});
	}
}
