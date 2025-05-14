package programmers.team6.global.exception.code;

import org.springframework.http.HttpStatus;

import programmers.team6.global.exception.ErrorStatus;

public enum NotFoundErrorCode implements ErrorCode {

	NOT_FOUND_DEPT("부서 정보를 찾을 수 없습니다."),
	NOT_FOUND_POSITION("직위 정보를 찾을 수 없습니다."),
	NOT_FOUND_EMAIL("이메일 정보를 찾을 수 없습니다.");

	private final String message;

	NotFoundErrorCode(String message) {
		this.message = message;
	}

	@Override
	public ErrorStatus getErrorStatus() {
		return ErrorStatus.NOT_FOUND;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return HttpStatus.NOT_FOUND;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
