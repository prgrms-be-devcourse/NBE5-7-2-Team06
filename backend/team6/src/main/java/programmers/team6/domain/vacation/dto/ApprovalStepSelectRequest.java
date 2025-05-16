package programmers.team6.domain.vacation.dto;

import java.time.LocalDate;

import lombok.extern.slf4j.Slf4j;
import programmers.team6.domain.vacation.enums.ApprovalStatus;

@Slf4j
public record ApprovalStepSelectRequest(
	String type,
	String name,
	LocalDate from,
	LocalDate to,
	ApprovalStatus status
) {
	public boolean hasFilter() {
		return type != null || name != null || from != null || to != null || status != null;
	}
}
