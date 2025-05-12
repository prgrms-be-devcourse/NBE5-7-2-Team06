package programmers.team6.domain.vacation.enums;

import lombok.Getter;

@Getter
public enum VacationExceptionMessage {
	EMPTY_VACATION_REQUEST_DETAIL("존재하지 않는 휴가계입니다. "),
	EMPTY_APPROVAL_STEP("존재하지 않는 결재단계입니다. "),
	INVALID_APPROVAL_STEP("잘못된 결제 단계입니다. ");

	private final String message;

	VacationExceptionMessage(String message) {
		this.message = message;
	}
}
