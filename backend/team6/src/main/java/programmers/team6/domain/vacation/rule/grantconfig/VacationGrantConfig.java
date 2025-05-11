package programmers.team6.domain.vacation.rule.grantconfig;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.rule.AnnualVacationGrantRule;

@RequiredArgsConstructor
public final class VacationGrantConfig {

	private final AnnualVacationGrantConfig annualVacationGrantConfig;
	private final MonthlyVacationGrantConfig monthlyVacationGrantConfig;

	public static VacationGrantConfig statutory() {
		return new VacationGrantConfig(AnnualVacationGrantConfig.statutory(), MonthlyVacationGrantConfig.statutory());
	}

	public static AnnualVacationGrantRule statutoryRule() {
		return statutory().toAnnualVacationGrantRule();
	}

	public AnnualVacationGrantRule toAnnualVacationGrantRule() {
		return new AnnualVacationGrantRule(
			annualVacationGrantConfig.toAnnualVacationRule(),
			monthlyVacationGrantConfig.toMonthlyVacationRule());
	}
}
