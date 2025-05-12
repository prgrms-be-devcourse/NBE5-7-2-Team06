package programmers.team6.domain.admin.dto;

import java.time.LocalDate;

import programmers.team6.domain.admin.enums.Quarter;
import programmers.team6.domain.vacation.enums.VacationRequestStatus;

public record AdminVacationSearchCondition(
	DateRangeCondition dateRange,
	ApplicantCondition applicant,
	ApproverCondition approver,
	// 휴가 신청 상태
	VacationRequestStatus vacationRequestStatus
) {

	public record DateRangeCondition(
		// 시작일
		LocalDate start,
		// 종료일
		LocalDate end,
		// 년도
		Integer year,
		// 분기
		Quarter quarter
	) {
		public DateRangeCondition {
			if (quarter == null) {
				quarter = Quarter.NONE;
			}
		}
	}

	// 휴가 신청자
	public record ApplicantCondition(
		// 이름
		String name,
		// 부서명
		String deptName,
		// 직책 codeId
		Long positionCodeId,
		// 휴가 종류 codeId
		Long vacationTypeCodeId
	) {
	}

	// 휴가 결재자
	public record ApproverCondition(
		// 이름
		String name
	) {
	}
}
