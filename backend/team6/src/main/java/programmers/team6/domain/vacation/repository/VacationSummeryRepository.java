package programmers.team6.domain.vacation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import programmers.team6.domain.vacation.dto.VacationMonthlySummaryResponse;

public interface VacationSummeryRepository {

    Page<VacationMonthlySummaryResponse> getMonthlyVacationSummary(Integer targetYear, Pageable pageable);
}
