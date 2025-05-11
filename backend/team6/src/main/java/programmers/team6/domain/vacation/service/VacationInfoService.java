package programmers.team6.domain.vacation.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.repository.VacationInfoRepository;
import programmers.team6.domain.vacation.rule.AnnualVacationGrantRule;
import programmers.team6.domain.vacation.rule.grantconfig.VacationGrantConfig;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibilities;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibilitiesFactory;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibility;

@RequiredArgsConstructor
@Service
public class VacationInfoService {

	private final VacationInfoRepository vacationInfoRepository;
	private final VacationGrantEligibilitiesFactory eligibilitiesFactory;

	public VacationGrantEligibilities selectEligiblitiesFrom(LocalDate date) {
		AnnualVacationGrantRule annualVacationGrantRule = VacationGrantConfig.statutoryRule();
		List<VacationGrantEligibility> vacationGrantEligibilities = vacationInfoRepository.findEligibilities(
			annualVacationGrantRule.getAnnualVacationStartJoinDateFrom(date), date);
		return eligibilitiesFactory.create(date, vacationGrantEligibilities, annualVacationGrantRule);
	}

}
