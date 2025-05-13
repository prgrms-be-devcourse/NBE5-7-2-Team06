package programmers.team6.domain.member.exception;

import lombok.Getter;
import programmers.team6.domain.member.enums.CodeExceptionMessage;

@Getter
public class CodeException extends RuntimeException {
	private final CodeExceptionMessage codeExceptionMessage;

	public CodeException(CodeExceptionMessage codeExceptionMessage) {
		this.codeExceptionMessage = codeExceptionMessage;
	}

}
