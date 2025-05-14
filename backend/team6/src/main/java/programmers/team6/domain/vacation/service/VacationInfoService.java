package programmers.team6.domain.vacation.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequest;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequests;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequestsList;
import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.enums.VacationInfoUpdateResult;
import programmers.team6.domain.vacation.repository.VacationEligibilitiesRepository;
import programmers.team6.domain.vacation.repository.VacationInfoRepository;
import programmers.team6.domain.vacation.repository.dto.UpdatedResults;
import programmers.team6.domain.vacation.rule.AnnualVacationGrantRule;
import programmers.team6.domain.vacation.rule.VacationGrantRule;
import programmers.team6.domain.vacation.rule.VacationGrantRuleFinder;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibilities;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibilitiesFactory;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibility;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantInfo;
import programmers.team6.global.entity.Positive;

@RequiredArgsConstructor
@Service
public class VacationInfoService {

	private final VacationInfoRepository vacationInfoRepository;
	private final VacationGrantEligibilitiesFactory eligibilitiesFactory;
	private final VacationEligibilitiesRepository vacationEligibilitiesRepository;
	private final VacationGrantRuleFinder vacationGrantRuleFinder;

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

	@Transactional
	public void updateFrom(VacationInfoUpdateTotalCountRequestsList request) {
		VacationInfos vacationInfos = findVacationInfos(request.vacationIds());
		updateVacationInfos(request, vacationInfos);
	}

	private VacationInfos findVacationInfos(List<Integer> ids) {
		List<VacationInfo> vacationInfos = vacationInfoRepository.findAllByVacationIdIn(ids);
		return new VacationInfos(vacationInfos);
	}

	private void updateVacationInfos(VacationInfoUpdateTotalCountRequestsList request,
		VacationInfos vacationInfos) {
		for (VacationInfoUpdateTotalCountRequests vacations : request.requests()) {
			updateTotalCount(vacationInfos.getByMemberId(vacations.memberId()), vacations);
		}
	}

	private void updateTotalCount(List<VacationInfo> infos, VacationInfoUpdateTotalCountRequests requests) {
		for (VacationInfo info : infos) {
			VacationInfoUpdateTotalCountRequest request = requests.getTarget(info.getVacationType());
			VacationGrantRule vacationGrantRule = vacationGrantRuleFinder.find(info.getVacationType());
			if (!vacationGrantRule.canUpdate(new Positive(request.totalCount()))) {
				//TODO : 공통예외구현되면 수정예정
				throw new RuntimeException();
			}

			VacationInfoUpdateResult updateResult = info.updateTotalCount(request.version(), request.totalCount());
			if (!updateResult.isSuccess()) {
				//TODO : 공통예외구현되면 수정예정
				throw new RuntimeException();
			}
		}
	}
}
