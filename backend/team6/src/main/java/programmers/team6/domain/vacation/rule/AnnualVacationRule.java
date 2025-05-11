package programmers.team6.domain.vacation.rule;

import static programmers.team6.domain.vacation.rule.DateUtil.*;

import java.time.LocalDate;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibility;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantInfo;

@RequiredArgsConstructor
public final class AnnualVacationRule {

	private final Positive boundaryYear;
	private final Positive increaseYear;
	private final Positive vacationIncreaseDays;
	private final Positive initialGrantDays;
	private final Positive maxGrantDays;

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
