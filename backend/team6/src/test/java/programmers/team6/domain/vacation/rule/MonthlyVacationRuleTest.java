package programmers.team6.domain.vacation.rule;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibility;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantInfo;
import programmers.team6.global.entity.Positive;

class MonthlyVacationRuleTest {

	private static VacationGrantEligibility createTestMemberVacationInfo() {
		return new VacationGrantEligibility(1, LocalDateTime.of(2024, 10, 18, 0, 0, 0), 3, 0);
	}

	@Test
	void 지급정보생성() {
		int grantDays = 2;
		MonthlyVacationRule monthlyVacationRule = new MonthlyVacationRule(new Positive(grantDays));
		VacationGrantEligibility vacationGrantEligibility = createTestMemberVacationInfo();
		VacationGrantInfo grantInfo = monthlyVacationRule.toGrantInfo(vacationGrantEligibility);

		assertThat(grantInfo).isEqualTo(
			new VacationGrantInfo(vacationGrantEligibility.id(), grantDays + vacationGrantEligibility.vacationCount(),
				vacationGrantEligibility.version()));
	}
}