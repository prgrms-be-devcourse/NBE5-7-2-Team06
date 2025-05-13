package programmers.team6.domain.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.admin.dto.MemberApprovalResponse;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.member.enums.Role;
import programmers.team6.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberApprovalService {

	private static final Role ROLE_PENDING = Role.PENDING;
	private final MemberRepository memberRepository;

	public List<MemberApprovalResponse> findPendingMembers() {
		return memberRepository.findPendingMembers(ROLE_PENDING);
	}

	@Transactional
	public void approveMember(Long memberId) {
		Member findMember = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));
		findMember.approveMember();

		// todo : vacationInfo 연차 추가하는 것 필요
	}

	@Transactional
	public void deleteMember(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));
		member.validateDeletable();
		memberRepository.delete(member);
	}
}
