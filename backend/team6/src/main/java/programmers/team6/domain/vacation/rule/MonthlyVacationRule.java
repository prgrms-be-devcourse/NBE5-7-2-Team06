package programmers.team6.domain.vacation.rule;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibility;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantInfo;
import programmers.team6.global.entity.Positive;

@RequiredArgsConstructor
public final class MonthlyVacationRule {

	private final Positive grantDays;

	public VacationGrantInfo toGrantInfo(VacationGrantEligibility vacationGrantEligibility) {
		return new VacationGrantInfo(vacationGrantEligibility.id(),
			grantDays.toInt() + vacationGrantEligibility.vacationCount(),
			vacationGrantEligibility.version());
	}
}
