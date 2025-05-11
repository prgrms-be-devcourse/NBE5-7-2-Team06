package programmers.team6.domain.vacation.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import programmers.team6.domain.vacation.enums.VacationRequestStatus;

public record VacationRequestDetailReadResponse(LocalDate from, LocalDate to, String name, String deptName,
												String position, String reason, String vacationType,
												VacationRequestStatus vacationRequestStatus,
												List<ApprovalStepDetail> approvalStepDetails) {
	public VacationRequestDetailReadResponse(LocalDate from, LocalDate to, String name, String deptName,
		String position, String reason, String vacationType, VacationRequestStatus vacationRequestStatus) {
		this(from, to, name, deptName, position, reason, vacationType, vacationRequestStatus, new ArrayList<>());
	}

	public VacationRequestDetailReadResponse injectApprovalStepDetails(List<ApprovalStepDetail> approvalStepDetails) {
		this.approvalStepDetails.addAll(approvalStepDetails);
		return this;
	}
}
