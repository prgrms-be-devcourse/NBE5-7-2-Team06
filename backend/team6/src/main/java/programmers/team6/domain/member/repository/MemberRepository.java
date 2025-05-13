package programmers.team6.domain.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import programmers.team6.domain.admin.dto.MemberApprovalResponse;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.member.enums.Role;

public interface MemberRepository extends JpaRepository<Member, Long> {

	@Query(
		"select new programmers.team6.domain.admin.dto.MemberApprovalResponse("
			+ "m.id, m.name, m.position.name, m.dept.deptName, m.memberInfo.birth, m.memberInfo.email"
			+ ")"
			+ "from Member m "
			+ "where m.role = :role")
	List<MemberApprovalResponse> findPendingMembers(Role role);
}
