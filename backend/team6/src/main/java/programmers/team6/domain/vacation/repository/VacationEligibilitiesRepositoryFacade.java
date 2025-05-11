package programmers.team6.domain.vacation.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.repository.dto.UpdatedResults;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibility;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantInfo;

@RequiredArgsConstructor
@Repository
public class VacationEligibilitiesRepositoryFacade implements VacationEligibilitiesRepository {
	private final JdbcVacationEligibilitiesRepository jdbcRepository;
	private final VacationInfoRepository vacationInfoRepository;

	@Override
	public UpdatedResults<VacationGrantInfo> updateAnnualVacationEligiblities(List<VacationGrantInfo> eligibilities) {
		return jdbcRepository.updateAnnualVacationEligiblities(eligibilities);
	}

	@Override
	public UpdatedResults<VacationGrantInfo> updateMonthlyVacationEligiblities(List<VacationGrantInfo> eligibilities) {
		return jdbcRepository.updateMonthlyVacationEligiblities(eligibilities);
	}

	@Override
	public List<VacationGrantEligibility> findEligibilities(LocalDate startJoinDate, LocalDate date) {
		return vacationInfoRepository.findEligibilities(startJoinDate, date);
	}
}
