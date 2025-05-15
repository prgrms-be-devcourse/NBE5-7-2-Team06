package programmers.team6.global.exception.code;

import org.springframework.http.HttpStatus;

import programmers.team6.global.exception.ErrorStatus;

public enum BadRequestErrorCode implements ErrorCode {
	BAD_REQUEST_APPROVE("해당 결재를 승인할 수 없습니다"),
	BAD_REQUEST_REJECT("해당 결재를 반려할 수 없습니다");

	private final String message;
	private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

	BadRequestErrorCode(String message) {
		this.message = message;
	}

	@Override
	public ErrorStatus getErrorStatus() {
		return ErrorStatus.BAD_REQUEST;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	@Override
	public int getHttpStatusCode() {
		return httpStatus.value();
	}

	@Override
	public String getMessage() {
		return message;
	}
}
