package programmers.team6.domain.vacation.dto;

import jakarta.validation.constraints.NotNull;

public record VacationInfoUpdateTotalCountRequest(@NotNull Integer id, @NotNull Integer totalCount,
												  @NotNull String type, @NotNull Integer version) {

	public boolean isSameType(String type) {
		return this.type.equals(type);
	}
}
