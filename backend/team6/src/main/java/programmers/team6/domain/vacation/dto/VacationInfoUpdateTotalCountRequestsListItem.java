package programmers.team6.domain.vacation.dto;

import jakarta.validation.constraints.NotNull;

public record VacationInfoUpdateTotalCountRequestsListItem(@NotNull Long memberId,
														   @NotNull VacationInfoUpdateTotalCountRequests vacations) {
}
