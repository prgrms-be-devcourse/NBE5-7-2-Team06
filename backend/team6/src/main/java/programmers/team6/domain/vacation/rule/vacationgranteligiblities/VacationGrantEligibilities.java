package programmers.team6.domain.vacation.rule.vacationgranteligiblities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class VacationGrantEligibilities {

	private final List<VacationGrantEligibility> eligibility;
	private final VacationEligibilityCriteria criteria;

	public VacationGrantEligibilities(List<VacationGrantEligibility> eligibility, VacationEligibilityCriteria criteria) {
		this.eligibility = new ArrayList<>(eligibility);
		this.criteria = criteria;
	}

	public List<VacationGrantInfo> getAnnualVacationGrantInfos() {
		return Collections.unmodifiableList(getVacationGrantInfos(getAnnualVacationEligibleInfos()));
	}

	public List<VacationGrantInfo> getMonthlyVacationGrantInfos() {
		return Collections.unmodifiableList(getVacationGrantInfos(getMonthlyVacationEligibleInfos()));
	}

	private List<VacationGrantEligibility> getMonthlyVacationEligibleInfos() {
		return eligibility.stream().filter(criteria::isMonthlyVacationEligible).toList();
	}

	private List<VacationGrantEligibility> getAnnualVacationEligibleInfos() {
		return eligibility.stream().filter(criteria::isAnnualVacationEligible).toList();
	}

	private List<VacationGrantInfo> getVacationGrantInfos(List<VacationGrantEligibility> vacationGrantEligibilities) {
		List<VacationGrantInfo> result = new ArrayList<>();
		for (VacationGrantEligibility info : vacationGrantEligibilities) {
			result.add(criteria.toGrantInfo(info));
		}
		return result;
	}
}
