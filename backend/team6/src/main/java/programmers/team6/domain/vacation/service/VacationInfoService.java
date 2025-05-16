package programmers.team6.domain.vacation.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.member.repository.MemberRepository;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequest;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequests;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequestsList;
import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.entity.VacationInfoLog;
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
	private final VacationInfoLogPublisher vacationInfoLogPublisher;

	@Transactional
	public void grantAnnualEligiblities(LocalDate date) {
		updateAnnualVacationInfos(date, selectAnnualVacationInfos(date));
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
			VacationInfoLog result = vacationGrantRule.grantAnnual(date, member, annualVacationInfo);
			vacationInfoLogPublisher.publish(result);
		}

		for (VacationInfo monthlyVacationInfo : eligibilities.getMonthlyVacationInfos()) {
			VacationInfoLog result = vacationGrantRule.grantMonthly(monthlyVacationInfo);
			vacationInfoLogPublisher.publish(result);
		}
	}

	private void updateTotalCount(List<VacationInfo> infos, VacationInfoUpdateTotalCountRequests requests) {
		for (VacationInfo info : infos) {
			VacationInfoUpdateTotalCountRequest request = requests.getTarget(info.getVacationType());
			VacationGrantRule vacationGrantRule = vacationGrantRuleFinder.find(info.getVacationType());
			if (!vacationGrantRule.canUpdate(new Positive(request.totalCount())) || !info.isSameVersion(
				request.version())) {
				//TODO : 공통예외구현되면 수정예정
				throw new RuntimeException();
			}

			VacationInfoLog log = info.updateTotalCount(request.totalCount());
			vacationInfoLogPublisher.publish(log);
		}
	}
}
