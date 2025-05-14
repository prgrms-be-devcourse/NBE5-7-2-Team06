package programmers.team6.domain.vacation.rule;

import java.time.LocalDate;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.enums.VacationInfoUpdateResult;
import programmers.team6.global.entity.Positive;

@RequiredArgsConstructor
public final class AnnualVacationGrantRule implements VacationGrantRule {

	private static final Positive STATUTORY_MAX_GRANT_DAYS = new Positive(25);

	private final AnnualVacationRule annualVacationRule;
	private final MonthlyVacationRule monthlyVacationRule;

	private final Positive maxGrantDays;

	public static AnnualVacationGrantRule statutory() {
		return new AnnualVacationGrantRule(AnnualVacationRule.statutory(), MonthlyVacationRule.statutory(),
			STATUTORY_MAX_GRANT_DAYS);
	}

	public LocalDate getAnnualVacationStartJoinDateFrom(LocalDate date) {
		return annualVacationRule.getAnnualVacationStartJoinDateFrom(date);
	}

	public VacationInfoUpdateResult grantAnnual(LocalDate date, Member member, VacationInfo vacationInfo) {
		return annualVacationRule.grant(date, member, vacationInfo, maxGrantDays);
	}

	public VacationInfoUpdateResult grantMonthly(VacationInfo vacationInfo) {
		return monthlyVacationRule.grant(vacationInfo, maxGrantDays);
	}

	@Override
	public boolean canUpdate(Positive totalCount) {
		return !this.maxGrantDays.isLessThan(totalCount);
	}
}
