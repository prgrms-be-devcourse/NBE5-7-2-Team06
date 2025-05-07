package programmers.team6.domain.member.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.member.dto.MemberCreateRequest;
import programmers.team6.domain.member.entity.Code;
import programmers.team6.domain.member.entity.Dept;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.member.repository.CodeRepository;
import programmers.team6.domain.member.repository.DeptRepository;
import programmers.team6.domain.member.repository.MemberRepository;
import programmers.team6.domain.member.util.mapper.MemberMapper;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	private final DeptRepository deptRepository;

	private final CodeRepository codeRepository;

	public Member saveMember(MemberCreateRequest memberCreateRequest) {

		Dept dept = deptRepository.findById(memberCreateRequest.getDept()).orElseThrow(
			() -> new IllegalArgumentException("해당 부서를 찾을 수 없습니다.")
		);

		Code position = codeRepository.findByGroupCodeAndCode("POSITION", memberCreateRequest.getPosition())
			.orElseThrow(
				() -> new IllegalArgumentException("해당 직위를 찾을 수 없습니다.")
			);

		String encodedPassword = memberCreateRequest.getPassword();
		//todo spring security 적용 후 암호화
		Member member = MemberMapper.MemberCreateRequestToEntity(memberCreateRequest, dept, position, encodedPassword);

		return memberRepository.save(member);
	}
}
