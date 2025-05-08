package programmers.team6.domain.vacation.dto;

public record VacationMonthlySummaryResponse(
        Long memberId,
        String userName,
        Long totalCount,
        Long usedCount,
        Long remainCount,
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
) {}
