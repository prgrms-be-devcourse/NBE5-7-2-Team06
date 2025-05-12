package programmers.team6.domain.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ConfigurationProperties(prefix = "jwt")
public record JwtConfiguration(
	String secret,
	long accessTokenExpiration,
	long refreshTokenExpiration,
	String header,
	String prefix
) {
	@PostConstruct
	public void check() {
		log.info("Checking jwt token");
		log.info("secret = {}", secret);
	}
}
