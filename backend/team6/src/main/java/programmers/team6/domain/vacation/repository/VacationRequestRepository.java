package programmers.team6.domain.vacation.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import programmers.team6.domain.vacation.entity.VacationRequest;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {
	// 휴가 신청자로 조회
	@Query("SELECT vr FROM VacationRequest vr WHERE vr.requester.id = :memberId ORDER BY vr.createdAt DESC")
	List<VacationRequest> findByRequesterId(@Param("memberId") Long memberId);

	// 페이징 처리된 휴가 신청 내역 조회
	@Query("SELECT vr FROM VacationRequest vr WHERE vr.requester.id = :memberId ORDER BY vr.createdAt DESC")
	Page<VacationRequest> findByRequesterIdWithPaging(
		@Param("memberId") Long memberId,
		Pageable pageable
	);
}
