package programmers.team6.domain.vacation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import programmers.team6.domain.vacation.dto.VacationRequestDetailReadResponse;
import programmers.team6.domain.vacation.entity.VacationRequest;

@Repository
public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {
	@Query(value =
		"select new programmers.team6.domain.vacation.dto.VacationRequestDetailReadResponse(vr.from, vr.to, m.name, d.deptName,p.name,vr.reason,t.name,vr.status) "
			+ "from VacationRequest vr join vr.type t " + "join vr.member m join m.dept d join m.position p "
			+ "where vr.id = :id")
	Optional<VacationRequestDetailReadResponse> findVacationRequestDetailById(@Param("id") Long id);

	Optional<VacationRequest> findVacationRequestById(Long id);
}
