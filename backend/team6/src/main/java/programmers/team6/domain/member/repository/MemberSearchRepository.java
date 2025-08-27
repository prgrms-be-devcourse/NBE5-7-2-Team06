package programmers.team6.domain.member.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.admin.utils.QueryDSLUtils;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.member.entity.QMember;
import programmers.team6.domain.member.enums.Role;

@Repository
@RequiredArgsConstructor
public class MemberSearchRepository {

	private final JPAQueryFactory queryFactory;

	public Page<Member> searchFrom(String name, Long deptId, Pageable pageable) {
		QMember member = QMember.member;
		BooleanBuilder builder = QueryDSLUtils.createEmptyCondition();

		if (name != null && name.isEmpty()) {
			QueryDSLUtils.containsIgnoreCase(builder, member.name, name);
		}
		if (deptId != null) {
			QueryDSLUtils.equal(builder, member.dept.id, deptId);
		}
		builder.and(member.role.eq(Role.USER));

		List<Member> content = queryFactory.selectFrom(member)
			.where(builder)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return PageableExecutionUtils.getPage(content, pageable,
			() -> QueryDSLUtils.count(queryFactory, builder, member.count(), member));
	}

	public Page<Member> searchFrom(Long deptId, String name, List<Long> ids, Pageable pageable) {
		QMember member = QMember.member;
		BooleanBuilder builder = QueryDSLUtils.createEmptyCondition();

		if (name != null && !name.isEmpty()) {
			QueryDSLUtils.containsIgnoreCase(builder, member.name, name);
		}
		if (deptId != null) {
			QueryDSLUtils.equal(builder, member.dept.id, deptId);
		}
		if (ids != null && !ids.isEmpty()) {
			QueryDSLUtils.in(builder, member.id, ids);
		}

		List<Member> content = queryFactory.selectFrom(member)
			.where(builder)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return PageableExecutionUtils.getPage(content, pageable,
			() -> QueryDSLUtils.count(queryFactory, builder, member.count(), member));
	}

}
