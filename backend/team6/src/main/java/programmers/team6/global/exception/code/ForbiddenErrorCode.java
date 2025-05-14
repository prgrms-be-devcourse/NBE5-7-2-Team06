package programmers.team6.global.exception.code;

import org.springframework.http.HttpStatus;

import programmers.team6.global.exception.ErrorStatus;

public enum ForbiddenErrorCode implements ErrorCode {

	FORBIDDEN_PENDING("회원가입 승인 대기중입니다");

	private final String message;

	ForbiddenErrorCode(String message) {
		this.message = message;
	}

	@Override
	public ErrorStatus getErrorStatus() {
		return ErrorStatus.FORBIDDEN;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return HttpStatus.FORBIDDEN;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
