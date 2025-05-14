package programmers.team6.domain.auth.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import programmers.team6.domain.auth.dto.TokenBody;
import programmers.team6.domain.auth.token.JwtTokenProvider;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String token = jwtTokenProvider.extractToken(request);
	
		jwtTokenProvider.validate(token);

		TokenBody tokenbody = jwtTokenProvider.parseClaims(token);

		Authentication auth = new UsernamePasswordAuthenticationToken(
			tokenbody, null, List.of(new SimpleGrantedAuthority(tokenbody.role().toString()))
		);

		SecurityContextHolder.getContext().setAuthentication(auth);

		filterChain.doFilter(request, response);
	}
}
