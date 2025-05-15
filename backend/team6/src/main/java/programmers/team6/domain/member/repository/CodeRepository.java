package programmers.team6.domain.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import programmers.team6.domain.member.dto.CodeReadResponse;
import programmers.team6.domain.member.entity.Code;

public interface CodeRepository extends JpaRepository<Code, Long> {
	Optional<Code> findByIdAndGroupCode(Long id, String groupCode);

	Optional<Code> findByGroupCodeAndCode(String groupCode, String code);

	List<Code> findByGroupCode(String vacationType);

	@Query(value = "select new programmers.team6.domain.member.dto.CodeReadResponse(c.id,c.groupCode,c.code,c.name) "
		+ "from Code c")
	Page<CodeReadResponse> findCodePage(Pageable pageable);

}
