package programmers.team6.domain.vacation.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import programmers.team6.domain.vacation.dto.VacationMonthlySummaryResponse;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VacationPageAdapter implements VacationSummeryRepository {

    private final VacationRequestRepository vacationRequestRepository;
    private final VacationInfoRepository vacationInfoRepository;

    @Override
    public Page<VacationMonthlySummaryResponse> getMonthlyVacationSummary(Integer targetYear, Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = pageable.getPageNumber() * limit;
        List<VacationMonthlySummaryResponse> result = vacationRequestRepository.getMonthlyVacationSummary(targetYear, limit, offset);

        long count = vacationInfoRepository.countAllMemberIds();
        return new PageImpl<>(result, pageable, count);
    }
}
