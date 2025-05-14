package programmers.team6.domain.vacation.rule.vacationgranteligiblities;

import java.util.List;

import org.springframework.stereotype.Component;

import programmers.team6.domain.vacation.entity.VacationInfo;

@Component
public class AnnualVacationInfosFactory {

	public AnnualVacationInfos create(List<VacationInfo> annualVacationInfos, List<VacationInfo> monthlyVacationInfos) {
		return new AnnualVacationInfos(annualVacationInfos, monthlyVacationInfos);
	}
}
