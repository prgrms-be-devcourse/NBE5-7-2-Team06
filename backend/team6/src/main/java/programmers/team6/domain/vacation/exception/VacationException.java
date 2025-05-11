package programmers.team6.domain.vacation.exception;

import lombok.Getter;
import programmers.team6.domain.vacation.enums.VacationExceptionMessage;

@Getter
public class VacationException extends RuntimeException {
	private final VacationExceptionMessage vacationExceptionMessage;

	public VacationException(VacationExceptionMessage vacationExceptionMessage) {
		this.vacationExceptionMessage = vacationExceptionMessage;
	}

}
