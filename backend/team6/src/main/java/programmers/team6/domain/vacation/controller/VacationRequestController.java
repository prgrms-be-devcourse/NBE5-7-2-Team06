package programmers.team6.domain.vacation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.dto.AdminVacationSearchCondition;
import programmers.team6.domain.vacation.dto.VacationRequestReadResponse;
import programmers.team6.domain.vacation.service.VacationRequestService;

@RestController
@RequestMapping("/api/admin/vacationRequest")
@RequiredArgsConstructor
public class VacationRequestController {
	private final VacationRequestService vacationRequestService;

	@GetMapping("/search")
	@ResponseStatus(HttpStatus.OK)
	Page<VacationRequestReadResponse> selectVacationRequests(
		@PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
		Pageable pageable, @RequestBody AdminVacationSearchCondition searchCondition) {
		return vacationRequestService.search(pageable, searchCondition);
	}

}
