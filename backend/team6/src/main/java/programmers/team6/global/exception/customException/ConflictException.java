package programmers.team6.global.exception.customException;

import programmers.team6.global.exception.code.ErrorCode;

public class ConflictException extends CustomException {

	public ConflictException(ErrorCode errorCode) {
		super(errorCode);
	}
}
