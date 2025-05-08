package programmers.team6.domain.vacation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import programmers.team6.domain.vacation.entity.VacationInfo;

public interface VacationInfoRepository extends JpaRepository<VacationInfo,Long> {

    @Query(value = "SELECT COUNT(DISTINCT vi.memberId) FROM VacationInfo vi")
    long countAllMemberIds();
}
