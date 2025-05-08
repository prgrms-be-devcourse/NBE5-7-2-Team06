package programmers.team6.domain.member.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.member.dto.MemberCreateRequest;
import programmers.team6.domain.member.service.MemberService;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping
	public ResponseEntity<Void> saveMember(@RequestBody MemberCreateRequest memberCreateRequest) {

		memberService.saveMember(memberCreateRequest);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/isEmailDuplicated")
	public ResponseEntity<Map<String, Boolean>> isEmailDuplicated(@RequestParam String email) {

		boolean isEmailDuplicated = memberService.isEmailDuplicated(email);

		return ResponseEntity.ok(Map.of("isEmailDuplicated", isEmailDuplicated));
	}
}
