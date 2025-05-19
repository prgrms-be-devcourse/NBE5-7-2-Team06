package programmers.team6.domain.admin.service;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.repository.VacationRequestRepository;

@Component
@RequiredArgsConstructor
public class VacationRequestsReader {

	private final VacationRequestRepository vacationRequestRepository;

	public VacationRequests vacationRequestFrom(List<Long> ids, Integer year, String code) {
		return new VacationRequests(vacationRequestRepository.findByMemberIdInAndYear(ids, year, code));
	}
}
