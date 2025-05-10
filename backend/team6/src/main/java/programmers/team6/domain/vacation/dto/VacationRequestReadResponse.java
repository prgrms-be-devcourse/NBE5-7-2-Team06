package programmers.team6.domain.vacation.dto;

import java.time.LocalDate;

import programmers.team6.domain.member.entity.Code;
import programmers.team6.domain.vacation.enums.VacationRequestStatus;

public record VacationRequestReadResponse(String type, LocalDate from, LocalDate to, String applicantName,
										  String approverName, String deptName, VacationRequestStatus status) {
	public VacationRequestReadResponse(Code type, LocalDate from, LocalDate to, String applicantName,
		String approverName,
		String deptName, VacationRequestStatus status) {
		this(type.getCode(), from, to, applicantName, approverName, deptName, status);
	}
}
