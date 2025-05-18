package programmers.team6.domain.vacation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequest;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequests;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequestsList;
import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.entity.VacationInfoLog;
import programmers.team6.domain.vacation.repository.VacationInfoRepository;
import programmers.team6.domain.vacation.rule.VacationGrantRule;
import programmers.team6.domain.vacation.rule.VacationGrantRuleFinder;
import programmers.team6.domain.vacation.rule.VacationInfos;

@RequiredArgsConstructor
@Service
public class VacationInfoService {

	private final VacationInfoRepository vacationInfoRepository;
	private final VacationGrantRuleFinder vacationGrantRuleFinder;
	private final VacationInfoLogPublisher vacationInfoLogPublisher;

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
			if (canUpdate(info, vacationGrantRule, request)) {
				//TODO : 공통예외구현되면 수정예정
				throw new RuntimeException();
			}

			VacationInfoLog log = info.updateTotalCount(request.totalCount());
			vacationInfoLogPublisher.publish(log);
		}
	}

	private static boolean canUpdate(VacationInfo info, VacationGrantRule vacationGrantRule,
		VacationInfoUpdateTotalCountRequest request) {
		return !vacationGrantRule.canUpdate(request.totalCount()) || !info.isSameVersion(
			request.version());
	}
}
