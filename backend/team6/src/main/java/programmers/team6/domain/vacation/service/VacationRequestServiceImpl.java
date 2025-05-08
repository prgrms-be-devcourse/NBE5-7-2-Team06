package programmers.team6.domain.vacation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.dto.AdminVacationSearchCondition;
import programmers.team6.domain.vacation.dto.VacationRequestReadResponse;
import programmers.team6.domain.vacation.dto.VacationRequestSearchCustom;

@Service
@RequiredArgsConstructor
public class VacationRequestServiceImpl implements VacationRequestService {
	private final VacationRequestSearchCustom vacationRequestSearchCustom;

	@Override
	public Page<VacationRequestReadResponse> search(Pageable pageable, AdminVacationSearchCondition searchCondition) {
		return vacationRequestSearchCustom.search(searchCondition, pageable);
	}
}
