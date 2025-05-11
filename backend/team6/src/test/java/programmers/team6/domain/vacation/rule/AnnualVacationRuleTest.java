package programmers.team6.domain.vacation.rule;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibility;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantInfo;
import programmers.team6.global.entity.Positive;

class AnnualVacationRuleTest {

	@Test
	void 연차변환기준입사일_생성() {
		int boundaryYear = 2;
		AnnualVacationRule annualVacationRule = createAnnualVacationRule(boundaryYear, 0, 0, 0, 0);
		LocalDate date = LocalDate.of(2024, 10, 18);

		LocalDate result = annualVacationRule.getAnnualVacationStartJoinDateFrom(date);

		assertThat(result).isEqualTo(LocalDate.of(2022, 10, 18));
	}

	@ParameterizedTest
	@CsvSource({"1,false", "2,true"})
	void 연차대상자_확인(Integer year, boolean expected) {
		AnnualVacationRule annualVacationRule = createAnnualVacationRule(2, 0, 0, 0, 0);
		LocalDateTime date = LocalDateTime.of(2022, 10, 18,0,0,0);
		VacationGrantEligibility vacationGrantEligibility = TestMemberVacationInfoBuilder.defaultInitBuilder()
			.joinDate(date)
			.build();

		boolean result = annualVacationRule.isAnnualVacationEligible(date.plusYears(year).toLocalDate(), vacationGrantEligibility);

		assertThat(result).isEqualTo(expected);
	}

	@ParameterizedTest
	@CsvSource(value = {"2023-10-18T00:00:00,15","2021-10-18T00:00:00,16","2003-10-18T00:00:00,25","2001-10-18T00:00:00,25"},delimiter = ',')
	void 지급정보생성(LocalDateTime joinDate, int increaseCount) {
		AnnualVacationRule annualVacationRule = createAnnualVacationRule(1, 2, 1, 15, 25);
		LocalDateTime now = LocalDateTime.of(2024, 10, 18,0,0,0);
		VacationGrantEligibility vacationGrantEligibility = TestMemberVacationInfoBuilder.defaultInitBuilder()
			.joinDate(joinDate)
			.build();

		VacationGrantInfo grantInfo = annualVacationRule.toGrantInfo(now.toLocalDate(), vacationGrantEligibility);

		assertThat(grantInfo.totalCount()).isEqualTo(TestMemberVacationInfoBuilder.DEFAULT_VACATION_COUNT + increaseCount);
	}

	private AnnualVacationRule createAnnualVacationRule(Integer boundaryYear, Integer increaseYear,
		Integer vacationIncreaseDays, Integer initialGrantDays, Integer maxGrantDays) {
		return new AnnualVacationRule(new Positive(boundaryYear), new Positive(increaseYear),
			new Positive(vacationIncreaseDays), new Positive(initialGrantDays), new Positive(maxGrantDays));
	}
}