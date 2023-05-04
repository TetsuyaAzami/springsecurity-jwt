package com.example.springsecuritywithjwt.authentication;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class MyAuthenticationUserDetailsService
		implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

	@Override
	public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token)
			throws UsernameNotFoundException {
		//
		DecodedJWT decodedJWT;

		String headerToken = token.getPrincipal().toString();
		if (!StringUtils.hasLength(headerToken)) {
			throw new BadCredentialsException("Authorization header must not be empty");
		}

		if (!headerToken.startsWith("Bearer ")) {
			throw new BadCredentialsException("Authorization header must start with Bearer");
		}

		String jwtToken = headerToken.substring(7);

		try {
			decodedJWT = JWT.require(Algorithm.HMAC256(System.getenv("SECRET_KEY"))).build().verify(jwtToken);
		} catch (JWTDecodeException e) {
			throw new BadCredentialsException("Authorization header token is invalid");
		}

		if (decodedJWT.getToken().isEmpty()) {
			throw new UsernameNotFoundException("Authorization header must not be empty");
		}

		Person person = new Person();
		person.setUsername(decodedJWT.getClaim("username").asString());
		person.setPassword("");
		person.setRoles(decodedJWT.getClaim("role").asString());

		return new MyUserDetails(person);
	}
}
