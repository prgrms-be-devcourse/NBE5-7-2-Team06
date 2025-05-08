package programmers.team6.domain.vacation.controller;

import java.util.List;

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
	public ResponseEntity<List<ApprovalFirstStepSelectResponse>> findFirstStep(ApprovalStepSelectRequest request) {
		return ResponseEntity.ok(approvalStepService.findFirstStep(request));
	}

	@GetMapping("/first/filter")
	public ResponseEntity<List<ApprovalFirstStepSelectResponse>> findFirstStepByFilter(
		ApprovalStepFilterRequest request) {
		return ResponseEntity.ok(approvalStepService.findFirstStepByFilter(request));
	}

}
