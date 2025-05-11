package programmers.team6.domain.vacation.rule.grantconfig;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.rule.MonthlyVacationRule;
import programmers.team6.global.entity.Positive;

@RequiredArgsConstructor
public class MonthlyVacationGrantConfig {

	private static final Integer STATUTORY_GRANT_DAYS = 1;

	private final Integer grantDays;

	static MonthlyVacationGrantConfig statutory() {
		return new MonthlyVacationGrantConfig(STATUTORY_GRANT_DAYS);
	}

	MonthlyVacationRule toMonthlyVacationRule() {
		return new MonthlyVacationRule(new Positive(grantDays));
	}
}
