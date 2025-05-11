package programmers.team6.domain.vacation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.member.exception.CodeException;
import programmers.team6.domain.vacation.dto.AdminVacationSearchCondition;
import programmers.team6.domain.vacation.dto.VacationRequestDetailReadResponse;
import programmers.team6.domain.vacation.dto.VacationRequestDetailUpdateRequest;
import programmers.team6.domain.vacation.dto.VacationRequestReadResponse;
import programmers.team6.domain.vacation.exception.VacationException;
import programmers.team6.domain.vacation.service.VacationRequestService;

@RestController
@RequestMapping("/api/admin/vacationRequest")
@RequiredArgsConstructor
public class VacationRequestController {
	private final VacationRequestService vacationRequestService;

	@GetMapping("/search")
	@ResponseStatus(HttpStatus.OK)
	Page<VacationRequestReadResponse> selectVacationRequests(
		@PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
		@RequestBody AdminVacationSearchCondition searchCondition) {
		return vacationRequestService.search(pageable, searchCondition);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	VacationRequestDetailReadResponse showVacationRequestDetail(@PathVariable Long id) {
		return vacationRequestService.selectVacationRequestDetailById(id);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void updateVacationRequestDetail(@PathVariable Long id,
		@RequestBody VacationRequestDetailUpdateRequest vacationRequestDetailUpdateRequest) {
		vacationRequestService.updateVacationRequestDetailById(id, vacationRequestDetailUpdateRequest);
	}

	// TODO - 추후에 전역 예외 처리할 것같아, 임시적으로 여기에 exception handling 필요
	@ExceptionHandler(VacationException.class)
	public ResponseEntity<String> handleVacationException(VacationException e) {
		return ResponseEntity.badRequest().body(e.getVacationExceptionMessage().getMessage());
	}

	@ExceptionHandler(CodeException.class)
	public ResponseEntity<String> handleCodeException(CodeException e) {
		return ResponseEntity.badRequest().body(e.getCodeExceptionMessage().getMessage());
	}
}
