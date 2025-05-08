package programmers.team6.domain.vacation.dto;

import org.springframework.stereotype.Component;

import programmers.team6.domain.vacation.entity.VacationInfo;

@Component
public class VacationInfoMapper {
	public VacationInfoSelectResponseDto toVacationInfoSelectResponseDto(VacationInfo vacationInfo) {
		return VacationInfoSelectResponseDto.builder()
			.totalCount(vacationInfo.getTotalCount())
			.useCount(vacationInfo.getUseCount())
			.remainCount(vacationInfo.getRemainCount())
			.build();
	}
}
