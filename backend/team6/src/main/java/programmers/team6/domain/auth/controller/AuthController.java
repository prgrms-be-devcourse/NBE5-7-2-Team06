package programmers.team6.domain.auth.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import programmers.team6.domain.auth.dto.TokenBody;
import programmers.team6.domain.auth.dto.TokenPairWithExpiration;
import programmers.team6.domain.auth.dto.request.MemberLoginRequest;
import programmers.team6.domain.auth.dto.request.MemberSignUpRequest;
import programmers.team6.domain.auth.dto.request.RefreshTokenRequest;
import programmers.team6.domain.auth.dto.response.AuthTokenResponse;
import programmers.team6.domain.auth.service.AuthService;
import programmers.team6.domain.auth.token.JwtTokenProvider;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final JwtTokenProvider jwtTokenProvider;

	@PostMapping("/signup")
	public ResponseEntity<Void> signUp(@RequestBody MemberSignUpRequest memberSignUpRequest) {

		authService.signUp(memberSignUpRequest);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/email-duplicate-check")
	public ResponseEntity<Map<String, Boolean>> isEmailDuplicated(@RequestParam String email) {

		boolean isEmailDuplicated = authService.isEmailDuplicated(email);

		return ResponseEntity.ok(Map.of("isEmailDuplicated", isEmailDuplicated));
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, AuthTokenResponse>> login(@RequestBody MemberLoginRequest memberLoginRequest) {

		AuthTokenResponse token = authService.login(memberLoginRequest);

		return ResponseEntity.ok(Map.of("token", token));
	}

	@PostMapping("/reissue")
	public ResponseEntity<TokenPairWithExpiration> refresh(
		@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
		String refreshToken = refreshTokenRequest.refreshToken();

		TokenPairWithExpiration tokenPair = authService.reissue(refreshToken);

		return ResponseEntity.ok(tokenPair);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@AuthenticationPrincipal TokenBody tokenBody, HttpServletRequest request) {
		String accessToken = jwtTokenProvider.extractToken(request);

		authService.logout(tokenBody, accessToken);

		return ResponseEntity.noContent().build();
	}

}
