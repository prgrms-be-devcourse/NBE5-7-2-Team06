package programmers.team6.domain.auth.util;

import java.util.Date;

public class JwtUtils {

	public static long calculateTtlMillis(long expiration) {
		return Math.max(expiration - System.currentTimeMillis(), 0);
	}

	public static long calculateTtlMillis(Date expiration) {
		return Math.max(expiration.getTime() - System.currentTimeMillis(), 0);
	}

}
