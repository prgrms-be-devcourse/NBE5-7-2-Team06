package programmers.team6.domain.vacation.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.member.repository.MemberRepository;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequest;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequests;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequestsList;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequestsListItem;
import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.enums.VacationInfoUpdateResult;
import programmers.team6.domain.vacation.repository.VacationInfoRepository;
import programmers.team6.domain.vacation.rule.AnnualVacationGrantRule;
import programmers.team6.domain.vacation.rule.VacationGrantRule;
import programmers.team6.domain.vacation.rule.VacationGrantRuleFinder;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.AnnualVacationInfos;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.AnnualVacationInfosFactory;
import programmers.team6.global.entity.Positive;

@RequiredArgsConstructor
@Service
public class VacationInfoService {

	private final MemberRepository memberRepository;
	private final VacationInfoRepository vacationInfoRepository;
	private final AnnualVacationInfosFactory annualVacationInfosFactory;
	private final VacationGrantRuleFinder vacationGrantRuleFinder;

	@Transactional
	public void grantAnnualEligiblities(LocalDate date) {
		updateAnnualVacationInfos(date, selectAnnualVacationInfos(date));
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

	private AnnualVacationInfos selectAnnualVacationInfos(LocalDate date) {
		AnnualVacationGrantRule vacationGrantRule = AnnualVacationGrantRule.statutory();
		LocalDate startJoinDate = vacationGrantRule.getAnnualVacationStartJoinDateFrom(date);
		List<VacationInfo> annualVacations = vacationInfoRepository.findAnnualVacationFrom(startJoinDate, date);
		List<VacationInfo> monthlyVacations = vacationInfoRepository.findMonthlyVacationFrom(startJoinDate, date);
		return annualVacationInfosFactory.create(annualVacations, monthlyVacations);
	}

	private void updateAnnualVacationInfos(LocalDate date, AnnualVacationInfos eligibilities) {
		AnnualVacationGrantRule vacationGrantRule = (AnnualVacationGrantRule)vacationGrantRuleFinder.find("01");
		for (VacationInfo annualVacationInfo : eligibilities.getAnnualVacationInfos()) {
			Member member = memberRepository.findById(annualVacationInfo.getMemberId())
				.orElseThrow(() -> new RuntimeException());
			VacationInfoUpdateResult result = vacationGrantRule.grantAnnual(date, member, annualVacationInfo);
			if (!result.isSuccess()){
				throw new RuntimeException();
			}
		}

		for (VacationInfo monthlyVacationInfo : eligibilities.getMonthlyVacationInfos()) {
			VacationInfoUpdateResult result = vacationGrantRule.grantMonthly(monthlyVacationInfo);
			if (!result.isSuccess()){
				throw new RuntimeException();
			}
		}
	}

	private void updateTotalCount(VacationInfoUpdateTotalCountRequests vacationInfoUpdateTotalCountRequests,
		List<VacationInfo> infos) {
		for (VacationInfo info : infos) {
			VacationInfoUpdateTotalCountRequest request = vacationInfoUpdateTotalCountRequests.getTarget(
				info.getVacationType());
			VacationGrantRule vacationGrantRule = vacationGrantRuleFinder.find(info.getVacationType());
			if (!vacationGrantRule.canUpdate(new Positive(request.totalCount())) || !info.isSameVersion(request.version())) {
				//TODO : 공통예외구현되면 수정예정
				throw new RuntimeException();
			}

			VacationInfoUpdateResult updateResult = info.updateTotalCount(request.totalCount());
			if (!updateResult.isSuccess()) {
				//TODO : 공통예외구현되면 수정예정
				throw new RuntimeException();
			}
		}
	}
}
