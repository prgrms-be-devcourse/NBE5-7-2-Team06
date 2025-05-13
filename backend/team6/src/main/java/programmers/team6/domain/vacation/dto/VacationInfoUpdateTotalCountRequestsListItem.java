package programmers.team6.domain.vacation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record VacationInfoUpdateTotalCountRequestsListItem(@NotNull Long memberId,
														   @NotNull @NotEmpty VacationInfoUpdateTotalCountRequests vacations) {
}
