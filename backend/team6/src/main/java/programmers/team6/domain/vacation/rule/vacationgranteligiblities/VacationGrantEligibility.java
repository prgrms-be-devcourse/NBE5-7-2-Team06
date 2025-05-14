package programmers.team6.domain.vacation.rule.vacationgranteligiblities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record VacationGrantEligibility(Integer id, LocalDateTime joinDate, double vacationCount, Integer version) {
	public LocalDate joinDateAsLocalDate() {
		return joinDate.toLocalDate();
	}
}
