package programmers.team6.domain.vacation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import programmers.team6.domain.vacation.dto.VacationMonthlySummaryResponse;
import programmers.team6.domain.vacation.entity.VacationRequest;

import java.util.List;

public interface VacationRequestRepository extends JpaRepository<VacationRequest,Long> {

    /**
    * 해당 함수는
    * */
    @Query(nativeQuery = true)
    List<VacationMonthlySummaryResponse> getMonthlyVacationSummary(@Param("target_year") Integer targetYear,  @Param("limit") int limit, @Param("offset") int offset);
}
