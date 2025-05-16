package programmers.team6.domain.vacation.rule;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.entity.VacationInfoLog;
import programmers.team6.global.entity.Positive;

@RequiredArgsConstructor
public final class MonthlyVacationRule {

	private static final Positive STATUTORY_GRANT_DAYS = new Positive(1);

	private final Positive grantDays;

	public static MonthlyVacationRule statutory() {
		return new MonthlyVacationRule(STATUTORY_GRANT_DAYS);
	}

	public VacationInfoLog grant(VacationInfo vacationInfo) {
		return vacationInfo.updateTotalCount(vacationInfo.getTotalCount() + grantDays.toInt());
	}
}
