package programmers.team6.domain.member.enums;

import lombok.Getter;

@Getter
public enum CodeExceptionMessage {
	EMPTY_CODE("존재하지 않는 코드입니다. ");

	private final String message;

	CodeExceptionMessage(String message) {
		this.message = message;
	}
}
