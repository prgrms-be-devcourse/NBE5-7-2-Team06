package programmers.team6.domain.vacation.rule;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibility;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantInfo;
import programmers.team6.global.entity.Positive;

@RequiredArgsConstructor
public final class MonthlyVacationRule {

	private static final Positive STATUTORY_GRANT_DAYS = new Positive(1);

	private final Positive grantDays;

	public static MonthlyVacationRule statutory() {
		return new MonthlyVacationRule(STATUTORY_GRANT_DAYS);
	}

	public VacationGrantInfo toGrantInfo(VacationGrantEligibility vacationGrantEligibility) {
		return new VacationGrantInfo(vacationGrantEligibility.id(),
			grantDays.toInt() + vacationGrantEligibility.vacationCount(),
			vacationGrantEligibility.version());
	}
}
