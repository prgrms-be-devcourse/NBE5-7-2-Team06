package programmers.team6.domain.admin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.member.repository.MemberRepository;
import programmers.team6.domain.vacation.dto.VacationMonthlyStatisticsResponse;
import programmers.team6.domain.vacation.enums.VacationCode;
import programmers.team6.domain.vacation.repository.VacationRequestRepository;

@Service
@RequiredArgsConstructor
public class VacationStatisticsService {

	private final MemberRepository memberRepository;
	private final VacationRequestRepository vacationRequestRepository;
	private final VacationInfoLogReader vacationInfoLogReader;
	private final VacationRequestsReader vacationRequestsReader;
	private final VacationStatisticsMapper mapper;

	@Transactional(readOnly = true)
	public Page<VacationMonthlyStatisticsResponse> getMonthlyVacationStatistics(Integer year, Pageable pageable) {
		Page<Member> members = memberRepository.findAll(pageable);
		List<Long> ids = toIds(members);
		VacationRequests vacationRequests = vacationRequestsReader.vacationRequestFrom(ids, year, VacationCode.ANNUAL.getCode());
		VacationInfoLogs logs = vacationInfoLogReader.lastedLogsFrom(ids, VacationCode.ANNUAL.getCode());
		return mapper.toDto(members, vacationRequests, logs, year);
	}

	private List<Long> toIds(Page<Member> members) {
		return members.stream().map(Member::getId).toList();
	}
}
