package programmers.team6.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import programmers.team6.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	@Query("select m from Member m join fetch m.memberInfo mi where mi.email = :email")
	Optional<Member> findByEmail(@Param("email") String email);

}
