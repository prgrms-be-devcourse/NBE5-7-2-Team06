package programmers.team6.domain.vacation.dto;

import java.time.LocalDate;

import programmers.team6.domain.vacation.enums.ApprovalStatus;

public record ApprovalSecondStepSelectResponse(
	Long approvalStepId,
	String type,
	LocalDate from,
	LocalDate to,
	String name,
	String deptName,
	String positionName,
	ApprovalStatus firstApprovalStatus,
	ApprovalStatus secondApprovalStatus
) {
}
