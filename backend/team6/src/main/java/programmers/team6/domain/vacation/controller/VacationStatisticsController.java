package programmers.team6.domain.vacation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import programmers.team6.domain.vacation.dto.VacationMonthlyStatisticsResponse;
import programmers.team6.domain.vacation.repository.VacationStatisticsRepository;

@RestController
@RequestMapping("/vacation/statistics")
@RequiredArgsConstructor
public class VacationStatisticsController {

    private final VacationStatisticsRepository vacationStatisticsRepository;

    @GetMapping("/{year}")
    public Page<VacationMonthlyStatisticsResponse> monthlySummary(@PathVariable("year") Integer year, Pageable pageable) {
        return vacationStatisticsRepository.getMonthlyVacationStatistics(year, pageable);
    }
}
