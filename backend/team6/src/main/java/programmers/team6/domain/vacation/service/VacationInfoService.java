package programmers.team6.domain.vacation.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.repository.dto.UpdatedResults;
import programmers.team6.domain.vacation.repository.VacationEligibilitiesRepository;
import programmers.team6.domain.vacation.rule.AnnualVacationGrantRule;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibilities;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibilitiesFactory;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibility;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantInfo;

@RequiredArgsConstructor
@Service
public class VacationInfoService {

	private final VacationGrantEligibilitiesFactory eligibilitiesFactory;
	private final VacationEligibilitiesRepository vacationEligibilitiesRepository;

	public VacationGrantEligibilities selectEligiblitiesFrom(LocalDate date) {
		AnnualVacationGrantRule vacationGrantRule = AnnualVacationGrantRule.statutory();
		List<VacationGrantEligibility> eligibilities = vacationEligibilitiesRepository.findEligibilities(
			vacationGrantRule.getAnnualVacationStartJoinDateFrom(date), date);
		return eligibilitiesFactory.create(date, eligibilities, vacationGrantRule);
	}

	@Transactional
	public void updateEligiblities(VacationGrantEligibilities eligibilities) {
		UpdatedResults<VacationGrantInfo> annualVacationEligiblitiesResult = vacationEligibilitiesRepository.updateAnnualVacationEligiblities(
			eligibilities.getAnnualVacationGrantInfos());
		UpdatedResults<VacationGrantInfo> monthlyVacationEligiblitiesResult = vacationEligibilitiesRepository.updateMonthlyVacationEligiblities(
			eligibilities.getMonthlyVacationGrantInfos());
		if (annualVacationEligiblitiesResult.isHasAnyNonUpdated()
			|| monthlyVacationEligiblitiesResult.isHasAnyNonUpdated()) {
			throw new RuntimeException();
		}
	}
}
