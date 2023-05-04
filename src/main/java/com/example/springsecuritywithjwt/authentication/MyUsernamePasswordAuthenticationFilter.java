package com.example.springsecuritywithjwt.authentication;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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
			MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

			String jwtToken = JWT.create().withIssuer("test-issuer").withIssuedAt(Instant.now())
					.withExpiresAt(Instant.now().plus(Duration.ofDays(60)))
					.withClaim("username", userDetails.getPerson().getUsername())
					.withClaim("role", authentication.getAuthorities().iterator().next().toString())
					.sign(Algorithm.HMAC256(System.getenv("SECRET_KEY")));

			response.addHeader("X-AUTH-TOKEN", "Bearer " + jwtToken);
			response.setStatus(200);
			response.getWriter()
					.write(new ObjectMapper().writeValueAsString(authentication.getName()));
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
