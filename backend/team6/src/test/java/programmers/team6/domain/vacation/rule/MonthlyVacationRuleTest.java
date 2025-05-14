package programmers.team6.domain.vacation.rule;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.enums.VacationInfoUpdateResult;
import programmers.team6.global.entity.Positive;

class MonthlyVacationRuleTest {

	@Test
	void 지급정보생성() {
		int grantDays = 2;
		MonthlyVacationRule monthlyVacationRule = new MonthlyVacationRule(new Positive(grantDays));
		VacationInfo info = createTestVacationInfo(15);

		VacationInfoUpdateResult grant = monthlyVacationRule.grant(info, new Positive(25));

		assertThat(grant.isSuccess()).isTrue();
		assertThat(info.getTotalCount()).isEqualTo(17);
	}

	@Test
	void 지급정보생성_최대값() {
		int grantDays = 2;
		MonthlyVacationRule monthlyVacationRule = new MonthlyVacationRule(new Positive(grantDays));
		VacationInfo info = createTestVacationInfo(15);

		VacationInfoUpdateResult grant = monthlyVacationRule.grant(info, new Positive(15));

		assertThat(grant.isSuccess()).isTrue();
		assertThat(info.getTotalCount()).isEqualTo(15);
	}

	private static VacationInfo createTestVacationInfo(int totalCount) {
		return new VacationInfo(totalCount,0,"test",1L);
	}
}