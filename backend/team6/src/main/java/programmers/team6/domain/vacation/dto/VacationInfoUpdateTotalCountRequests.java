package programmers.team6.domain.vacation.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record VacationInfoUpdateTotalCountRequests(
	@NotNull @NotEmpty List<@Valid VacationInfoUpdateTotalCountRequest> vacations) {

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
