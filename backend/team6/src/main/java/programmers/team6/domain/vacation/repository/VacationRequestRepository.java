package programmers.team6.domain.vacation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import programmers.team6.domain.vacation.dto.VacationMonthlyStatisticsResponse;
import programmers.team6.domain.vacation.entity.VacationRequest;

import java.util.List;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {

    /**
     * 사용자별로 월단위 휴가사용 개수를 조회해오는 기능입니다.
     * 해당 쿼리를 보고 싶다면 resource의 vacation.xml를 확인하세요
     * @author JiHun
     */
    @Query(nativeQuery = true)
    List<VacationMonthlyStatisticsResponse> getMonthlyVacationStatistics(@Param("target_year") Integer targetYear, @Param("limit") int limit, @Param("offset") int offset);
}
