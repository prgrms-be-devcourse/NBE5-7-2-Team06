package programmers.team6.domain.vacation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import programmers.team6.domain.vacation.dto.AdminVacationSearchCondition;
import programmers.team6.domain.vacation.dto.VacationRequestReadResponse;

public interface VacationRequestService {
	Page<VacationRequestReadResponse> search(Pageable pageable,AdminVacationSearchCondition searchCondition);
}
