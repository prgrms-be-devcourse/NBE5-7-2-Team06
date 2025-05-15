package programmers.team6.global.exception.customException;

import programmers.team6.global.exception.code.ErrorCode;

public class BadRequestException extends CustomException {

	public BadRequestException(ErrorCode errorCode) {
		super(errorCode);
	}
}
