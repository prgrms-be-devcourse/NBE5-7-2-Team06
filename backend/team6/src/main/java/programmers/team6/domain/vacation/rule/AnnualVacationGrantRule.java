package programmers.team6.domain.vacation.rule;

import java.time.LocalDate;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibility;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantInfo;

@RequiredArgsConstructor
public final class AnnualVacationGrantRule {

	private final AnnualVacationRule annualVacationRule;
	private final MonthlyVacationRule monthlyVacationRule;

	public LocalDate getAnnualVacationStartJoinDateFrom(LocalDate date) {
		return annualVacationRule.getAnnualVacationStartJoinDateFrom(date);
	}

	public boolean isMonthlyVacationEligible(LocalDate now, VacationGrantEligibility vacationGrantEligibility) {
		return !annualVacationRule.isAnnualVacationEligible(now, vacationGrantEligibility);
	}

	public boolean isAnnualVacationEligible(LocalDate now, VacationGrantEligibility vacationGrantEligibility) {
		return annualVacationRule.isAnnualVacationEligible(now, vacationGrantEligibility);
	}

	public VacationGrantInfo toGrantInfo(LocalDate now, VacationGrantEligibility vacationGrantEligibility) {
		if (isAnnualVacationEligible(now, vacationGrantEligibility)) {
			return annualVacationRule.toGrantInfo(now, vacationGrantEligibility);
		}
		return monthlyVacationRule.toGrantInfo(vacationGrantEligibility);
	}
}
