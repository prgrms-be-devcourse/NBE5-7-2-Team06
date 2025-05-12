package programmers.team6.domain.auth.dto.response;

import programmers.team6.domain.auth.dto.TokenPairWithExpiration;
import programmers.team6.domain.member.enums.Role;

public record AuthTokenResponse(
	TokenPairWithExpiration tokenPairWithExpiration,
	Long id,
	String name,
	Role role
) {
}
