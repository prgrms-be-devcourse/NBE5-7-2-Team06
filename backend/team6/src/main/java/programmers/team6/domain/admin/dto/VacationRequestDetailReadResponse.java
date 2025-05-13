package programmers.team6.domain.admin.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import programmers.team6.domain.vacation.enums.VacationRequestStatus;

public record VacationRequestDetailReadResponse(Long id, LocalDate from, LocalDate to, String name, String deptName,
												String position, String reason, String vacationType,
												VacationRequestStatus vacationRequestStatus,
												List<ApprovalStepDetailUpdateResponse> approvalStepDetailUpdateResponses) {
	public VacationRequestDetailReadResponse(Long id, LocalDate from, LocalDate to, String name, String deptName,
		String position, String reason, String vacationType, VacationRequestStatus vacationRequestStatus) {
		this(id, from, to, name, deptName, position, reason, vacationType, vacationRequestStatus, new ArrayList<>());
	}

	public VacationRequestDetailReadResponse injectApprovalStepDetails(
		List<ApprovalStepDetailUpdateResponse> approvalStepDetailUpdateResponses) {
		this.approvalStepDetailUpdateResponses.addAll(approvalStepDetailUpdateResponses);
		return this;
	}
}
