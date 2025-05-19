package programmers.team6.domain.vacation.dto;

public record VacationMonthlyStatisticsResponse(
	Long memberId,
	String userName,
	double totalCount,
	double usedCount,
	double remainCount,
	Long january,
	Long february,
	Long march,
	Long april,
	Long may,
	Long june,
	Long july,
	Long august,
	Long september,
	Long october,
	Long november,
	Long december
) {
}
