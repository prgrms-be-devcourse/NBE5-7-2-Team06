package programmers.team6.domain.vacation.dto;

import java.util.List;

public record VacationInfoUpdateTotalCountRequests(List<VacationInfoUpdateTotalCountRequest> vacations) {

	public VacationInfoUpdateTotalCountRequest getTarget(String type) {
		for (VacationInfoUpdateTotalCountRequest vacation : vacations) {
			if (vacation.isSameType(type)) {
				return vacation;
			}
		}
		//TODO : 공통예외나오면 수정 예정
		throw new RuntimeException();
	}
}
