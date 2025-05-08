package programmers.team6.domain.vacation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.dto.ApprovalFirstStepSelectResponse;
import programmers.team6.domain.vacation.dto.ApprovalStepFilterRequest;
import programmers.team6.domain.vacation.dto.ApprovalStepSelectRequest;
import programmers.team6.domain.vacation.service.ApprovalStepService;

@RestController
@RequestMapping("/approval-step")
@RequiredArgsConstructor
public class ApprovalStepController {

	private final ApprovalStepService approvalStepService;

	@GetMapping("/first")
	public ResponseEntity<Page<ApprovalFirstStepSelectResponse>> findFirstStep(
		ApprovalStepSelectRequest request, @PageableDefault(size = 20) Pageable pageable) {
		return ResponseEntity.ok(approvalStepService.findFirstStep(request, pageable));
	}

	@GetMapping("/first/filter")
	public ResponseEntity<Page<ApprovalFirstStepSelectResponse>> findFirstStepByFilter(
		ApprovalStepFilterRequest request, @PageableDefault(size = 20) Pageable pageable) {
		return ResponseEntity.ok(approvalStepService.findFirstStepByFilter(request, pageable));
	}

}
