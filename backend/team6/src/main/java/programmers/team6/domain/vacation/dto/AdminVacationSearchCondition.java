package programmers.team6.domain.vacation.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminVacationSearchCondition {
	// 휴가 신청 범위
	private LocalDate start;
	private LocalDate end;

	// 특정 분기 (1,2,3,4,상,하반기)
	private Integer year;
	private Quarter quarter;

	// 휴가 신청자 이름
	private String applicantName;

	// 부서 이름
	private String deptName;

	// code id (휴가 종류, 휴가 신청자 포지션, 결재자 포지션)
	private Long codeId;

	// 휴가 신청 상태
	private VacationRequestStatus vacationRequestStatus;

	// 휴가 결재자 이름
	private String approverName;
}
