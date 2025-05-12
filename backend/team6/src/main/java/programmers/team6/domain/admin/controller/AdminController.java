package programmers.team6.domain.admin.controller;

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
import programmers.team6.domain.admin.dto.AdminVacationSearchCondition;
import programmers.team6.domain.admin.dto.VacationRequestDetailReadResponse;
import programmers.team6.domain.admin.dto.VacationRequestDetailUpdateRequest;
import programmers.team6.domain.admin.dto.VacationRequestSearchResponse;
import programmers.team6.domain.vacation.exception.VacationException;
import programmers.team6.domain.admin.service.AdminService;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
	private final AdminService adminService;

	@GetMapping("/vacation-request")
	@ResponseStatus(HttpStatus.OK)
	Page<VacationRequestSearchResponse> selectVacationRequests(
		@PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
		@RequestBody AdminVacationSearchCondition searchCondition) {
		return adminService.search(pageable, searchCondition);
	}

	@GetMapping("/vacation-request/{id}")
	@ResponseStatus(HttpStatus.OK)
	VacationRequestDetailReadResponse showVacationRequestDetail(@PathVariable Long id) {
		return adminService.selectVacationRequestDetailById(id);
	}

	@PutMapping("/vacation-request/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void updateVacationRequestDetail(@PathVariable Long id,
		@RequestBody VacationRequestDetailUpdateRequest vacationRequestDetailUpdateRequest) {
		adminService.updateVacationRequestDetailById(id, vacationRequestDetailUpdateRequest);
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
