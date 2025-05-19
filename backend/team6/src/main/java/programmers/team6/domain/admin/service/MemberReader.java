package programmers.team6.domain.admin.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.member.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class MemberReader {

	private final MemberRepository memberRepository;

	public Members readHasVacationInfoMemberFrom(Integer year, String name, Pageable pageable) {
		LocalDateTime date = LocalDateTime.of(year, 12, 31, 23, 59);
		if (name == null) {
			return new Members(memberRepository.findAllHasVacationInfoTargetYear(date, pageable));
		}
		return new Members(memberRepository.findAllHasVacationInfoTargetYear(date, name, pageable));
	}
}
