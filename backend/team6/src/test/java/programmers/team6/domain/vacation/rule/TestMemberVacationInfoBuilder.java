package programmers.team6.domain.vacation.rule;

import java.time.LocalDate;
import java.time.LocalDateTime;

import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibility;

public class TestMemberVacationInfoBuilder {
	public static final Integer DEFAULT_ID = 1;
	public static final LocalDateTime DEFAULT_JOIN_DATE = LocalDateTime.of(2024, 10, 18,0,0,0);
	public static final Integer DEFAULT_VACATION_COUNT = 3;
	public static final Integer DEFAULT_VERSION = 0;


	private Integer id = DEFAULT_ID;
	private LocalDateTime joinDate = DEFAULT_JOIN_DATE;
	private Integer vacationCount = DEFAULT_VACATION_COUNT;
	private Integer version = DEFAULT_VERSION;

	public static TestMemberVacationInfoBuilder defaultInitBuilder() {
		return new TestMemberVacationInfoBuilder();
	}

	public TestMemberVacationInfoBuilder id(Integer id) {
		this.id = id;
		return this;
	}

	public TestMemberVacationInfoBuilder joinDate(LocalDateTime joinDate) {
		this.joinDate = joinDate;
		return this;
	}

	public TestMemberVacationInfoBuilder vacationCount(Integer vacationCount) {
		this.vacationCount = vacationCount;
		return this;
	}

	public TestMemberVacationInfoBuilder version(Integer version) {
		this.version = version;
		return this;
	}

	public VacationGrantEligibility build() {
		return new VacationGrantEligibility(id, joinDate, vacationCount, version);
	}
}
