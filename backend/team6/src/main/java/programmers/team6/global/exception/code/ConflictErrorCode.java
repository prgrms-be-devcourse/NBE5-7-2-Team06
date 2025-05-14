package programmers.team6.global.exception.code;

import org.springframework.http.HttpStatus;

import programmers.team6.global.exception.ErrorStatus;

public enum ConflictErrorCode implements ErrorCode {

	CONFLICT_EMAIL("중복된 이메일입니다.");

	private final String message;

	ConflictErrorCode(String message) {
		this.message = message;
	}

	@Override
	public ErrorStatus getErrorStatus() {
		return ErrorStatus.CONFLICT;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return HttpStatus.CONFLICT;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
