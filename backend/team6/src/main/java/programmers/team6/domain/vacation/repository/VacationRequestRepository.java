package programmers.team6.domain.vacation.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import programmers.team6.domain.vacation.entity.VacationRequest;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {
	@Query("SELECT vr.id FROM VacationRequest vr WHERE vr.requester.id = :memberId ORDER BY vr.createdAt DESC")
	Page<Long> findIdsByRequesterIdPaging(@Param("memberId") Long memberId, Pageable pageable);

	@Query("SELECT vr FROM VacationRequest vr JOIN FETCH vr.type JOIN FETCH vr.requester WHERE vr.id IN :ids")
	List<VacationRequest> findByIdsWithFetch(@Param("ids") List<Long> ids);
}
