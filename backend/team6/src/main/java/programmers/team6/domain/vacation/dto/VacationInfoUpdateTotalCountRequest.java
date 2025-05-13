package programmers.team6.domain.vacation.dto;

public record VacationInfoUpdateTotalCountRequest(Integer id, Integer totalCount, String type, Integer version) {

	public boolean isSameType(String type) {
		return this.type.equals(type);
	}
}
