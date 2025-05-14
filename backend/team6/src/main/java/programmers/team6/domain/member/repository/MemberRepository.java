package programmers.team6.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import programmers.team6.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	@Query("SELECT m FROM Member m " +
		"JOIN FETCH m.dept d " +
		"LEFT JOIN FETCH d.deptLeader " +
		"WHERE m.id = :memberId")
	Optional<Member> findByIdWithDeptAndLeader(@Param("memberId") Long memberId);
}
