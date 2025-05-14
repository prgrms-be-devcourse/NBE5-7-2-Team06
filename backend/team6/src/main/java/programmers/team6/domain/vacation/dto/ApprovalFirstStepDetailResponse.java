package programmers.team6.domain.vacation.dto;

import java.time.LocalDate;

import programmers.team6.domain.vacation.enums.ApprovalStatus;

public record ApprovalFirstStepDetailResponse(
	Long approvalStepId,
	String name,
	String deptName,
	String positionName,
	ApprovalStatus status,
	String type,
	LocalDate from,
	LocalDate to,
	String reason,
	String approverName
) {
}
