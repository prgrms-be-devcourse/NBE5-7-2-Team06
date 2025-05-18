package programmers.team6.domain.vacation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record VacationInfoUpdateTotalCountRequest(@NotNull Integer id, @NotNull @PositiveOrZero Double totalCount,
												  @NotNull String type, @NotNull @PositiveOrZero Integer version) {

	public boolean isSameType(String type) {
		return this.type.equals(type);
	}
}
