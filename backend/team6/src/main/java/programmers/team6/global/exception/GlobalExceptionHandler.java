package programmers.team6.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import programmers.team6.global.exception.code.ErrorCode;
import programmers.team6.global.exception.customException.CustomException;
import programmers.team6.global.exception.response.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundException(CustomException e) {
		ErrorCode errorCode = e.getErrorCode();

		log.warn(errorCode.getMessage());

		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(new ErrorResponse(errorCode.toString(), errorCode.getMessage(),
				errorCode.getHttpStatusCode()));
	}

}
