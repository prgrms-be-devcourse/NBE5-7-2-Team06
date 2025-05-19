package programmers.team6.domain.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import programmers.team6.domain.member.dto.CodeDropdownResponse;
import programmers.team6.domain.member.dto.CodeReadResponse;
import programmers.team6.domain.member.entity.Code;

public interface CodeRepository extends JpaRepository<Code, Long> {
	Optional<Code> findByIdAndGroupCode(Long id, String groupCode);

	Optional<Code> findByGroupCodeAndCode(String groupCode, String code);

	@Query("""
		  SELECT new programmers.team6.domain.member.dto.CodeDropdownResponse (c.code,c.name)
		  FROM Code c
		  WHERE c.groupCode = :groupCode
		""")
	List<CodeDropdownResponse> findByGroupCode(@Param("groupCode") String groupCode);

	@Query(value = "select new programmers.team6.domain.member.dto.CodeReadResponse(c.id,c.groupCode,c.code,c.name) "
		+ "from Code c")
	Page<CodeReadResponse> findCodePage(Pageable pageable);

	boolean existsByGroupCodeAndCode(String groupCode, String code);
}
