package programmers.team6.domain.vacation.dto;

import java.time.LocalDate;
import java.util.List;

import programmers.team6.domain.vacation.enums.VacationRequestStatus;

public record VacationRequestReadResponse(String type, LocalDate from, LocalDate to, String applicantName,
										  List<String> approverNames, String deptName, VacationRequestStatus status) {
	public VacationRequestReadResponse(String type, LocalDate from, LocalDate to, String applicantName,
		String approverNames, String deptName, VacationRequestStatus status) {
		this(type, from, to, applicantName, List.of(approverNames.split(",")), deptName, status);
	}

}
