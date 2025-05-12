package programmers.team6.domain.vacation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.dto.ApprovalFirstStepDetailResponse;
import programmers.team6.domain.vacation.dto.ApprovalFirstStepSelectResponse;
import programmers.team6.domain.vacation.dto.ApprovalSecondStepDetailResponse;
import programmers.team6.domain.vacation.dto.ApprovalSecondStepSelectResponse;
import programmers.team6.domain.vacation.dto.ApprovalStepRejectRequest;
import programmers.team6.domain.vacation.dto.ApprovalStepSelectRequest;
import programmers.team6.domain.vacation.service.ApprovalStepService;

@RestController
@RequestMapping("/approval-steps")
@RequiredArgsConstructor
public class ApprovalStepController {

	private final ApprovalStepService approvalStepService;

	@GetMapping("/first")
	@ResponseStatus(HttpStatus.OK)
	public Page<ApprovalFirstStepSelectResponse> getFirstStep(
		ApprovalStepSelectRequest request, @PageableDefault(size = 20) Pageable pageable) {
		// todo: jwt 에서 memberId 꺼내야함
		Long memberId = 2L;

		if (!request.hasFilter()) {
			return approvalStepService.findFirstStepByMemberId(memberId, pageable);
		} else {
			return approvalStepService.findFirstStepByFilter(request, memberId, pageable);
		}
	}

	@GetMapping("/first/{approvalStepId}")
	@ResponseStatus(HttpStatus.OK)
	public ApprovalFirstStepDetailResponse getFirstStepDetail(@PathVariable Long approvalStepId) {
		// todo : jwt 에서 memberId 꺼내야함
		Long memberId = 2L;

		return approvalStepService.findFirstStepDetailById(approvalStepId, memberId);
	}

	@GetMapping("/second")
	@ResponseStatus(HttpStatus.OK)
	public Page<ApprovalSecondStepSelectResponse> getSecondStep(
		ApprovalStepSelectRequest request, @PageableDefault(size = 20) Pageable pageable) {
		// todo: jwt 에서 memberId 꺼내야함
		Long memberId = 4L;

		if (!request.hasFilter()) {
			return approvalStepService.findSecondStepByMemberId(memberId, pageable);
		} else {
			return approvalStepService.findSecondStepByFilter(request, memberId, pageable);
		}
	}

	@GetMapping("/second/{approvalStepId}")
	@ResponseStatus(HttpStatus.OK)
	public ApprovalSecondStepDetailResponse getSecondStepDetail(@PathVariable Long approvalStepId) {
		// todo : jwt 에서 memberId 꺼내야함
		Long memberId = 4L;

		return approvalStepService.findSecondStepDetailById(approvalStepId, memberId);
	}

	@PatchMapping("/first/{approvalStepId}/approve")
	@ResponseStatus(HttpStatus.OK)
	public void approveFirstStep(@PathVariable Long approvalStepId) {
		// todo : jwt 에서 memberId 꺼내야함
		Long memberId = 2L;

		approvalStepService.approveFirstStep(approvalStepId, memberId);
	}

	@PatchMapping("/first/{approvalStepId}/reject")
	@ResponseStatus(HttpStatus.OK)
	public void rejectFirstStep(@PathVariable Long approvalStepId,
		@Valid @RequestBody ApprovalStepRejectRequest request) {
		// todo : jwt 에서 memberId 꺼내야함
		Long memberId = 2L;
		approvalStepService.rejectFirstStep(approvalStepId, memberId, request);
	}

	@PatchMapping("/second/{approvalStepId}/approve")
	@ResponseStatus(HttpStatus.OK)
	public void approveSecondStep(@PathVariable Long approvalStepId) {
		// todo : jwt 에서 memberId 꺼내야함
		Long memberId = 4L;

		approvalStepService.approveSecondStep(approvalStepId, memberId);
	}

	@PatchMapping("/second/{approvalStepId}/reject")
	@ResponseStatus(HttpStatus.OK)
	public void rejectSecondStep(@PathVariable Long approvalStepId,
		@Valid @RequestBody ApprovalStepRejectRequest request) {
		// todo : jwt 에서 memberId 꺼내야함
		Long memberId = 4L;
		approvalStepService.rejectSecondStep(approvalStepId, memberId, request);
	}

}
