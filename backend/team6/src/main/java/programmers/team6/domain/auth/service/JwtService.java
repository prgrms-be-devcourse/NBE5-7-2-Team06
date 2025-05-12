package programmers.team6.domain.auth.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

	private final StringRedisTemplate stringRedisTemplate;

	public void saveRefreshToken(Long id, String refreshToken, long expirationTime) {
		String key = "RT_" + id;
		stringRedisTemplate.opsForValue().set(key, refreshToken, expirationTime, TimeUnit.MILLISECONDS);
	}

	public void saveBlackList(String accessToken, long expirationTime) {
		String key = "BL_" + accessToken;
		stringRedisTemplate.opsForValue().set(key, "logout", expirationTime, TimeUnit.MILLISECONDS);
	}

	public void deleteRefreshToken(Long id) {
		stringRedisTemplate.delete("RT_" + id);
	}

	public boolean isBlackListed(String accessToken) {
		return stringRedisTemplate.hasKey("BL_" + accessToken);
	}

}
