package programmers.team6.domain.vacation.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequest;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequests;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequestsList;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequestsListItem;
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
		List<VacationInfo> vacationInfos = vacationInfoRepository.findAllByMemberIdIn(request.memberIds());
		Map<Long, List<VacationInfo>> vacationInfoMap = vacationInfos.stream()
			.collect(Collectors.groupingBy(VacationInfo::getMemberId));
		for (VacationInfoUpdateTotalCountRequestsListItem item : request.requests()) {
			updateTotalCount(item.vacations(), vacationInfoMap.getOrDefault(item.memberId(),
				Collections.emptyList()));
		}
	}

	@Transactional
	public void updateFrom(Long memberId, VacationInfoUpdateTotalCountRequests vacationInfoUpdateTotalCountRequests) {

		List<VacationInfo> infos = vacationInfoRepository.findAllByMemberId(memberId);

		updateTotalCount(vacationInfoUpdateTotalCountRequests, infos);
	}

	private void updateTotalCount(VacationInfoUpdateTotalCountRequests vacationInfoUpdateTotalCountRequests,
		List<VacationInfo> infos) {
		for (VacationInfo info : infos) {
			VacationInfoUpdateTotalCountRequest request = vacationInfoUpdateTotalCountRequests.getTarget(
				info.getVacationType());
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
