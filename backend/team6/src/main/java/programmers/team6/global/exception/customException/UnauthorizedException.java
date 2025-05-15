package programmers.team6.global.exception.customException;

import programmers.team6.global.exception.code.ErrorCode;

public class UnauthorizedException extends CustomException {

	public UnauthorizedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
