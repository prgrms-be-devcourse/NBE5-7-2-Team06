package programmers.team6.domain.vacation.dto;

import java.time.LocalDate;

import programmers.team6.domain.vacation.enums.VacationRequestStatus;

public record VacationRequestReadResponse(String type, LocalDate from, LocalDate to, String applicantName,
										  String[] approverNames, String deptName, VacationRequestStatus status) {
	public VacationRequestReadResponse(String type, LocalDate from, LocalDate to, String applicantName,
		String approverNames, String deptName,
		VacationRequestStatus status) {
		this(type, from, to, applicantName, approverNames.split(","), deptName, status);
	}
}
