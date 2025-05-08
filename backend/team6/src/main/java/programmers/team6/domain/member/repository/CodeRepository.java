package programmers.team6.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import programmers.team6.domain.member.entity.Code;

public interface CodeRepository extends JpaRepository<Code,Long> {
}
