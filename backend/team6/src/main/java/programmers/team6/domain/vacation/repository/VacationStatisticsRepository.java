package programmers.team6.domain.vacation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import programmers.team6.domain.vacation.dto.VacationMonthlyStatisticsResponse;

public interface VacationStatisticsRepository {

    Page<VacationMonthlyStatisticsResponse> getMonthlyVacationStatistics(Integer targetYear, Pageable pageable);
}
