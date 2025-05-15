package programmers.team6.domain.auth.controller;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import programmers.team6.domain.auth.dto.TokenPairWithExpiration;
import programmers.team6.domain.auth.dto.request.MemberLoginRequest;
import programmers.team6.domain.auth.dto.request.MemberSignUpRequest;
import programmers.team6.domain.auth.dto.request.RefreshTokenRequest;
import programmers.team6.domain.auth.dto.response.AuthTokenResponse;
import programmers.team6.domain.auth.dto.response.LoginResponse;
import programmers.team6.domain.auth.service.AuthService;
import programmers.team6.domain.auth.util.JwtUtils;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<Void> signUp(@RequestBody MemberSignUpRequest memberSignUpRequest) {

		authService.signUp(memberSignUpRequest);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/email-duplicate-check")
	public ResponseEntity<Map<String, Boolean>> isEmailDuplicated(@RequestParam String email) {

		boolean isEmailDuplicated = authService.isExistsByEmail(email);

		return ResponseEntity.ok(Map.of("isEmailDuplicated", isEmailDuplicated));
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, AuthTokenResponse>> login(@RequestBody MemberLoginRequest memberLoginRequest,
		HttpServletResponse response) {

		LoginResponse loginResponse = authService.login(memberLoginRequest);

		String refreshToken = loginResponse.refreshToken();

		JwtUtils.addRefreshTokenCookie(response, refreshToken, loginResponse.refreshTokenExpiresIn());

		return ResponseEntity.ok(Map.of("token", loginResponse.authTokenResponse()));
	}

	@PostMapping("/reissue")
	public ResponseEntity<AccessTokenResponse> refresh(
		@CookieValue("refreshToken") String refreshToken, HttpServletResponse response) {

		TokenPairWithExpiration tokenPair = authService.reissue(refreshToken);

		JwtUtils.addRefreshTokenCookie(response, tokenPair.refreshToken(), tokenPair.refreshTokenExpiresIn());

		return ResponseEntity.ok(
			new AccessTokenResponse(tokenPair.accessToken(), JwtUtils.toSeconds(tokenPair.accessTokenExpiresIn())));
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@CookieValue("refreshToken") String refreshToken) {

		authService.addBlackList(refreshToken);

		ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
			.httpOnly(true)
			.secure(true)
			.path("/")
			.sameSite("Strict")
			.maxAge(0)
			.build();

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
			.build();
	}

}
