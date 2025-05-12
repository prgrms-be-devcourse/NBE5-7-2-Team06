package programmers.team6.domain.vacation.rule.vacationgranteligiblities;

import java.time.LocalDate;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.rule.AnnualVacationGrantRule;

@RequiredArgsConstructor
public final class VacationEligibilityCriteria {
	private final AnnualVacationGrantRule annualVacationGrantRule;
	private final LocalDate now;

	public boolean isMonthlyVacationEligible(VacationGrantEligibility vacationGrantEligibility) {
		return annualVacationGrantRule.isMonthlyVacationEligible(now, vacationGrantEligibility);
	}

	public boolean isAnnualVacationEligible(VacationGrantEligibility vacationGrantEligibility) {
		return annualVacationGrantRule.isAnnualVacationEligible(now, vacationGrantEligibility);
	}

	public VacationGrantInfo toGrantInfo(VacationGrantEligibility vacationGrantEligibility) {
		return annualVacationGrantRule.toGrantInfo(now, vacationGrantEligibility);
	}
}
