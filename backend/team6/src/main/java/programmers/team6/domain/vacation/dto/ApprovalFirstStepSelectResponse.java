package programmers.team6.domain.vacation.dto;

import java.time.LocalDate;

import programmers.team6.domain.vacation.enums.ApprovalStatus;

public record ApprovalFirstStepSelectResponse(
	Long approvalStepId,
	String type,
	LocalDate from,
	LocalDate to,
	String name,
	String deptName,
	String positionName,
	ApprovalStatus status
) {
}
