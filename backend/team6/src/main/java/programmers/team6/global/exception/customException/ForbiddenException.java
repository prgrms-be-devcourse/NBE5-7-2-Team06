package programmers.team6.global.exception.customException;

import programmers.team6.global.exception.code.ErrorCode;

public class ForbiddenException extends CustomException {

	public ForbiddenException(ErrorCode errorCode) {
		super(errorCode);
	}
}
