package programmers.team6.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import programmers.team6.domain.member.entity.Dept;

public interface DeptRepository extends JpaRepository<Dept, Long> {
	Optional<Dept> findByDeptName(String deptName);
}
