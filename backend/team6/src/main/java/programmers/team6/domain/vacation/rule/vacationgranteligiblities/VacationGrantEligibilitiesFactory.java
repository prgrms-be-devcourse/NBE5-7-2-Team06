package programmers.team6.domain.vacation.rule.vacationgranteligiblities;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import programmers.team6.domain.vacation.rule.AnnualVacationGrantRule;

@Component
public class VacationGrantEligibilitiesFactory {

	private static VacationEligibilityCriteria createCriteria(LocalDate date, AnnualVacationGrantRule rule) {
		return new VacationEligibilityCriteria(rule, date);
	}

	public VacationGrantEligibilities create(LocalDate date,
		List<VacationGrantEligibility> grantEligibilities, AnnualVacationGrantRule rule) {
		VacationEligibilityCriteria criteria = createCriteria(date, rule);
		return new VacationGrantEligibilities(grantEligibilities, criteria);
	}
}
