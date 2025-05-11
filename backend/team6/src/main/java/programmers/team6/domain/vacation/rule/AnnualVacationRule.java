package programmers.team6.domain.vacation.rule;

import static programmers.team6.global.util.DateUtil.*;

import java.time.LocalDate;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibility;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantInfo;
import programmers.team6.global.entity.Positive;

@RequiredArgsConstructor
public final class AnnualVacationRule {

	private static final Positive STATUTORY_BOUNDARY_YEAR = new Positive(1);
	private static final Positive STATUTORY_INCREASE_YEAR = new Positive(2);
	private static final Positive STATUTORY_INCREASE_DAYS = new Positive(1);
	private static final Positive STATUTORY_INITIAL_GRANT_DAYS = new Positive(15);
	private static final Positive STATUTORY_MAX_GRANT_DAYS = new Positive(25);

	private final Positive boundaryYear;
	private final Positive increaseYear;
	private final Positive vacationIncreaseDays;
	private final Positive initialGrantDays;
	private final Positive maxGrantDays;

	public static AnnualVacationRule statutory() {
		return new AnnualVacationRule(STATUTORY_BOUNDARY_YEAR, STATUTORY_INCREASE_YEAR, STATUTORY_INCREASE_DAYS,
			STATUTORY_INITIAL_GRANT_DAYS, STATUTORY_MAX_GRANT_DAYS);
	}

	public LocalDate getAnnualVacationStartJoinDateFrom(LocalDate date) {
		return date.minusYears(boundaryYear.toInt());
	}

	public boolean isAnnualVacationEligible(LocalDate now, VacationGrantEligibility vacationGrantEligibility) {
		LocalDate annualVacationStartJoinDate = getAnnualVacationStartJoinDateFrom(now);
		return isEqualsOrBefore(vacationGrantEligibility.joinDateAsLocalDate(), annualVacationStartJoinDate);
	}

	public VacationGrantInfo toGrantInfo(LocalDate now, VacationGrantEligibility vacationGrantEligibility) {
		int yearsOfService = calcYearsOfService(now, vacationGrantEligibility.joinDateAsLocalDate());
		return new VacationGrantInfo(vacationGrantEligibility.id(),
			vacationGrantEligibility.vacationCount() + calcIncreaseDays(yearsOfService),
			vacationGrantEligibility.version());
	}

	private int calcIncreaseDays(int yearsOfService) {
		int increaseDays = initialGrantDays.toInt() + calculateAdditionalVacationDays(yearsOfService);
		return Math.min(increaseDays, maxGrantDays.toInt());
	}

	private int calculateAdditionalVacationDays(int yearsOfService) {
		return (((yearsOfService - boundaryYear.toInt()) / increaseYear.toInt()) * vacationIncreaseDays.toInt());
	}
}
