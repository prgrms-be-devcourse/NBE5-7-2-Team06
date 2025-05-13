package programmers.team6.domain.vacation.repository;

import java.time.LocalDate;
import java.util.List;

import programmers.team6.domain.vacation.repository.dto.UpdatedResults;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibility;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantInfo;

public interface VacationEligibilitiesRepository {

	UpdatedResults<VacationGrantInfo> updateAnnualVacationEligiblities(List<VacationGrantInfo> eligibilities);

	UpdatedResults<VacationGrantInfo> updateMonthlyVacationEligiblities(List<VacationGrantInfo> eligibilities);

	List<VacationGrantEligibility> findEligibilities(LocalDate startJoinDate, LocalDate date);
}
