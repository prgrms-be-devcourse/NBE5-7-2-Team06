package programmers.team6.domain.vacation.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record VacationInfoUpdateTotalCountRequestsList(
	@NotEmpty @NotNull List<VacationInfoUpdateTotalCountRequestsListItem> requests) {
	public List<Long> memberIds() {
		return requests.stream().map(VacationInfoUpdateTotalCountRequestsListItem::memberId).toList();
	}
}
