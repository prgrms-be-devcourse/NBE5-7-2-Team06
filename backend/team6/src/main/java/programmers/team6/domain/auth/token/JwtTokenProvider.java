package programmers.team6.domain.auth.token;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import programmers.team6.domain.auth.config.JwtConfiguration;
import programmers.team6.domain.auth.dto.JwtMemberInfo;
import programmers.team6.domain.auth.dto.TokenBody;
import programmers.team6.domain.auth.dto.TokenPairWithExpiration;
import programmers.team6.domain.auth.service.JwtService;
import programmers.team6.domain.member.enums.Role;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

	private final JwtConfiguration jwtConfiguration;
	private final JwtService jwtService;

	private static final String HEADER = "Authorization";
	private static final String BEARER = "Bearer ";

	public TokenPairWithExpiration generateTokenPair(JwtMemberInfo jwtMemberInfo) {

		String accessToken = issueAccessToken(jwtMemberInfo);
		String refreshToken = issueRefreshToken(jwtMemberInfo);

		return new TokenPairWithExpiration(accessToken, refreshToken, jwtConfiguration.accessTokenExpiration(),
			jwtConfiguration.refreshTokenExpiration());
	}

	public boolean validate(String token) {
		try {
			Jws<Claims> claimsJws = Jwts.parser()
				.verifyWith(getSecretKey())
				.build()
				.parseSignedClaims(token);

			return true;
		} catch (SecurityException | MalformedJwtException | SignatureException e) {
			log.warn("유효하지 않은 JWT 서명: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.warn("만료된 JWT: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.warn("지원하지 않는 JWT 형식: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.warn("잘못된 JWT 입력값: {}", e.getMessage());
		}
		return false;
	}

	public boolean isBlackListed(String accessToken) {

		if (jwtService.isBlackListed(accessToken)) {
			log.warn(" 블랙리스트 토큰 접근 시도: {}", accessToken);
			return true;
		}

		return false;
	}

	public TokenBody parseClaims(String token) {

		Jws<Claims> claims = Jwts.parser()
			.verifyWith(getSecretKey())
			.build()
			.parseSignedClaims(token);

		Claims payload = claims.getPayload();

		Long id = Long.parseLong(payload.getSubject());

		return new TokenBody(
			id,
			payload.get("name").toString(),
			Role.valueOf(payload.get("role").toString()),
			payload.getExpiration(),
			payload.getIssuedAt());
	}

	public String issueAccessToken(JwtMemberInfo jwtMemberInfo) {
		return issue(jwtMemberInfo, jwtConfiguration.accessTokenExpiration());
	}

	public String issueRefreshToken(JwtMemberInfo jwtMemberInfo) {
		return issue(jwtMemberInfo, jwtConfiguration.refreshTokenExpiration());
	}

	private String issue(JwtMemberInfo jwtMemberInfo, Long expTime) {
		return Jwts.builder()
			.subject(jwtMemberInfo.id().toString())
			.claim("name", jwtMemberInfo.name())
			.claim("role", jwtMemberInfo.role())
			.issuedAt(new Date())
			.expiration(new Date(new Date().getTime() + expTime))
			.signWith(getSecretKey(), Jwts.SIG.HS256)
			.compact();
	}

	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(jwtConfiguration.secret().getBytes());
	}

	public String extractToken(HttpServletRequest request) {
		String header = request.getHeader(HEADER);

		if (header != null && header.startsWith(BEARER)) {
			return header.substring(7);
		}
		return null;
	}

}
