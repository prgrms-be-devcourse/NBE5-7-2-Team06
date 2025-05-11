package programmers.team6.domain.vacation.rule.grantconfig;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.rule.AnnualVacationRule;
import programmers.team6.global.entity.Positive;

@RequiredArgsConstructor
public class AnnualVacationGrantConfig {
	private static final Integer STATUTORY_BOUNDARY_YEAR = 1;
	private static final Integer STATUTORY_INCREASE_YEAR = 2;
	private static final Integer STATUTORY_INCREASE_DAYS = 1;
	private static final Integer STATUTORY_INITIAL_GRANT_DAYS = 15;
	private static final Integer STATUTORY_MAX_GRANT_DAYS = 25;

	private final Integer boundaryYear;
	private final Integer increaseYear;
	private final Integer vacationIncreaseDays;
	private final Integer initialGrantDays;
	private final Integer maxGrantDays;

	static AnnualVacationGrantConfig statutory() {
		return new AnnualVacationGrantConfig(STATUTORY_BOUNDARY_YEAR, STATUTORY_INCREASE_YEAR, STATUTORY_INCREASE_DAYS,
			STATUTORY_INITIAL_GRANT_DAYS, STATUTORY_MAX_GRANT_DAYS);
	}

	AnnualVacationRule toAnnualVacationRule() {
		return createAnnualVacationRule(boundaryYear, increaseYear, vacationIncreaseDays, initialGrantDays,
			maxGrantDays);
	}

	private AnnualVacationRule createAnnualVacationRule(int boundaryYear, int increaseYear,
		int vacationIncreaseDays, int initialGrantDays, int maxGrantDays) {
		return new AnnualVacationRule(new Positive(boundaryYear), new Positive(increaseYear),
			new Positive(vacationIncreaseDays), new Positive(initialGrantDays), new Positive(maxGrantDays));
	}
}
