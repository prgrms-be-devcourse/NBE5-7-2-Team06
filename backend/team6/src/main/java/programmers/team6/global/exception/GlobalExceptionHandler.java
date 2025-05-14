package programmers.team6.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import programmers.team6.global.exception.code.ErrorCode;
import programmers.team6.global.exception.customException.ConflictException;
import programmers.team6.global.exception.customException.ForbiddenException;
import programmers.team6.global.exception.customException.NotFoundException;
import programmers.team6.global.exception.customException.UnauthorizedException;
import programmers.team6.global.exception.response.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
		ErrorCode errorCode = e.getErrorCode();

		log.warn(errorCode.getMessage());

		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(new ErrorResponse(errorCode.getClass().getSimpleName(), errorCode.getMessage(),
				errorCode.getHttpStatus().value()));
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ErrorResponse> handleConflictException(ConflictException e) {
		ErrorCode errorCode = e.getErrorCode();

		log.warn(errorCode.getMessage());

		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(new ErrorResponse(errorCode.getClass().getSimpleName(), errorCode.getMessage(),
				errorCode.getHttpStatus().value()));
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException e) {
		ErrorCode errorCode = e.getErrorCode();

		log.warn(errorCode.getMessage());

		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(new ErrorResponse(errorCode.getClass().getSimpleName(), errorCode.getMessage(),
				errorCode.getHttpStatus().value()));
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
		ErrorCode errorCode = e.getErrorCode();

		log.warn(errorCode.getMessage());

		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(new ErrorResponse(errorCode.getClass().getSimpleName(), errorCode.getMessage(),
				errorCode.getHttpStatus().value()));
	}
}
