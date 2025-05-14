package programmers.team6.domain.vacation.rule;

import static programmers.team6.global.util.DateUtil.*;

import java.time.LocalDate;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibility;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantInfo;
import programmers.team6.global.entity.Positive;

@RequiredArgsConstructor
public final class AnnualVacationRule {

	private static final Positive STATUTORY_BOUNDARY_YEAR = new Positive(1);
	private static final Positive STATUTORY_INCREASE_YEAR = new Positive(2);
	private static final Positive STATUTORY_INCREASE_DAYS = new Positive(1);
	private static final Positive STATUTORY_INITIAL_GRANT_DAYS = new Positive(15);
	private static final Positive STATUTORY_MAX_GRANT_DAYS = new Positive(25);
	private static final VacationCode TYPE = VacationCode.ANNUAL;

	private final Positive boundaryYear;
	private final Positive increaseYear;
	private final Positive vacationIncreaseDays;
	private final Positive initialGrantDays;

	public static AnnualVacationRule statutory() {
		return new AnnualVacationRule(STATUTORY_BOUNDARY_YEAR, STATUTORY_INCREASE_YEAR, STATUTORY_INCREASE_DAYS,
			STATUTORY_INITIAL_GRANT_DAYS);
	}

	public LocalDate getAnnualVacationStartJoinDateFrom(LocalDate date) {
		return date.minusYears(boundaryYear.toInt());
	}

	public VacationInfoUpdateResult grant(LocalDate date, Member member, VacationInfo vacationInfo,
		Positive maxGrantDays) {
		int yearsOfService = calcYearsOfService(date, member.getJoinDate().toLocalDate());
		return vacationInfo.init(
			Math.min(vacationInfo.getTotalCount() + calcIncreaseDays(yearsOfService), maxGrantDays.toInt()));
	}

	private int calcIncreaseDays(int yearsOfService) {
		return initialGrantDays.toInt() + calculateAdditionalVacationDays(yearsOfService);
	}

	private int calculateAdditionalVacationDays(int yearsOfService) {
		return (((yearsOfService - boundaryYear.toInt()) / increaseYear.toInt()) * vacationIncreaseDays.toInt());
	}

	public boolean isNonOverMaxVacation(Positive totalCount) {
		return !this.maxGrantDays.isLessThan(totalCount);
	}

	public VacationInfo vacationInfo(Long memberId) {
		return new VacationInfo(initialGrantDays.toInt(), TYPE.getCode(), memberId);
	}
}
