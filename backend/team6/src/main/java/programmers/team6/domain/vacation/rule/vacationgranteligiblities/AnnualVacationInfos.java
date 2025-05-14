package programmers.team6.domain.vacation.rule.vacationgranteligiblities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import programmers.team6.domain.vacation.entity.VacationInfo;

public final class AnnualVacationInfos {

	private final List<VacationInfo> annualVacationInfos;
	private final List<VacationInfo> monthlyVacationInfos;

	public AnnualVacationInfos(List<VacationInfo> annualVacationInfos,
		List<VacationInfo> monthlyVacationInfos) {
		this.annualVacationInfos = new ArrayList<>(annualVacationInfos);
		this.monthlyVacationInfos = new ArrayList<>(monthlyVacationInfos);
	}

	public List<VacationInfo> getAnnualVacationInfos() {
		return Collections.unmodifiableList(annualVacationInfos);
	}

	public List<VacationInfo> getMonthlyVacationInfos() {
		return Collections.unmodifiableList(monthlyVacationInfos);
	}
}
