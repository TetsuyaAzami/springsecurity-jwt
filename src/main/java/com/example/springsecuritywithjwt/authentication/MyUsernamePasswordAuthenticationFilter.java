package com.example.springsecuritywithjwt.authentication;

import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	public MyUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
		//
		this.authenticationManager = authenticationManager;
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));

		this.setAuthenticationSuccessHandler((request, response, authentication) -> {
			//
			response.setStatus(200);
			MyUserDetails user = (MyUserDetails) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			response.getWriter().write((new ObjectMapper()).writeValueAsString(user.getPerson()));
		});
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		//
		try {
			LoginForm principal =
					new ObjectMapper().readValue(request.getInputStream(), LoginForm.class);

			return this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					principal.getUsername(), principal.getPassword()));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
