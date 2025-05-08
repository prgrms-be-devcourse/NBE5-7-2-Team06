package programmers.team6.domain.vacation.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import programmers.team6.domain.member.entity.Code;

@Getter
@Setter
public class VacationRequestReadResponse {
	private String type;
	private LocalDate from;
	private LocalDate to;
	private String applicantName;
	private String approverName;
	private String deptName;
	private VacationRequestStatus status;

	public VacationRequestReadResponse(Code type, LocalDate from, LocalDate to, String applicantName,
		String approverName,
		String deptName, VacationRequestStatus status) {
		this.type = type.getCode();
		this.from = from;
		this.to = to;
		this.applicantName = applicantName;
		this.approverName = approverName;
		this.deptName = deptName;
		this.status = status;
	}
}
