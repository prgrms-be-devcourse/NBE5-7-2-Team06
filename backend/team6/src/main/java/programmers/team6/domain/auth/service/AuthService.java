package programmers.team6.domain.auth.service;

import java.util.NoSuchElementException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.auth.dto.JwtMemberInfo;
import programmers.team6.domain.auth.dto.TokenBody;
import programmers.team6.domain.auth.dto.TokenPairWithExpiration;
import programmers.team6.domain.auth.dto.request.MemberLoginRequest;
import programmers.team6.domain.auth.dto.request.MemberSignUpRequest;
import programmers.team6.domain.auth.dto.response.AuthTokenResponse;
import programmers.team6.domain.auth.token.JwtTokenProvider;
import programmers.team6.domain.member.entity.Code;
import programmers.team6.domain.member.entity.Dept;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.member.repository.CodeRepository;
import programmers.team6.domain.member.repository.DeptRepository;
import programmers.team6.domain.member.repository.MemberInfoRepository;
import programmers.team6.domain.member.repository.MemberRepository;
import programmers.team6.domain.member.util.mapper.MemberMapper;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final MemberRepository memberRepository;
	private final MemberInfoRepository memberInfoRepository;
	private final DeptRepository deptRepository;
	private final CodeRepository codeRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final JwtService jwtService;

	public void signUp(MemberSignUpRequest memberSignUpRequest) {

		Dept dept = deptRepository.findById(memberSignUpRequest.dept()).orElseThrow(
			() -> new IllegalArgumentException("해당 부서를 찾을 수 없습니다.")
		);

		Code position = codeRepository.findByGroupCodeAndCode("POSITION", memberSignUpRequest.position())
			.orElseThrow(
				() -> new IllegalArgumentException("해당 직위를 찾을 수 없습니다.")
			);

		String encodedPassword = passwordEncoder.encode(memberSignUpRequest.password());

		Member member = MemberMapper.MemberCreateRequestToEntity(
			memberSignUpRequest, dept, position, encodedPassword
		);

		memberRepository.save(member);
	}

	public boolean isEmailDuplicated(String email) {
		return memberInfoRepository.existsByEmail(email);
	}

	public AuthTokenResponse login(MemberLoginRequest memberLoginRequest) {

		Member member = memberRepository.findByEmail(memberLoginRequest.email())
			.orElseThrow(() -> new NoSuchElementException("이메일 없음"));

		if (!passwordEncoder.matches(memberLoginRequest.password(), member.getMemberInfo().getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		TokenPairWithExpiration tokenPair = jwtTokenProvider.generateTokenPair(
			new JwtMemberInfo(member.getId(), member.getName(), member.getRole()));

		return new AuthTokenResponse(tokenPair, member.getId(), member.getName(), member.getRole());
	}

	public TokenPairWithExpiration reissue(String refreshToken) {
		if (!jwtTokenProvider.validate(refreshToken)) {
			//toDo exceptionhandler 처리
			throw new BadCredentialsException("유효하지 않은 refresh 토큰");
		}

		if (jwtTokenProvider.isBlackListed(refreshToken)) {
			throw new BadCredentialsException("블랙리스트에 등록된 토큰");
		}

		TokenBody tokenBody = jwtTokenProvider.parseClaims(refreshToken);

		return jwtTokenProvider.generateTokenPair(
			new JwtMemberInfo(tokenBody.id(), tokenBody.name(), tokenBody.role())
		);
	}

	public void logout(TokenBody tokenBody, String accessToken) {

		jwtService.saveBlackList(accessToken,
			tokenBody.expiration().getTime() - System.currentTimeMillis());

		jwtService.deleteRefreshToken(tokenBody.id());
	}

}
