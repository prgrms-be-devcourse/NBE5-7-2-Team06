package programmers.team6.domain.vacation.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.admin.utils.QueryDSLUtils;
import programmers.team6.domain.member.entity.QCode;
import programmers.team6.domain.member.entity.QDept;
import programmers.team6.domain.member.entity.QMember;
import programmers.team6.domain.vacation.dto.VacationRequestCalendarResponse;
import programmers.team6.domain.vacation.entity.QVacationRequest;
import programmers.team6.domain.vacation.enums.VacationRequestStatus;

@Repository
@RequiredArgsConstructor
public class VacationRequestSearchRepository {

	private final JPAQueryFactory queryFactory;

	public List<VacationRequestCalendarResponse> findApprovedVacationsByMonth(
		VacationRequestStatus status, LocalDateTime start, LocalDateTime end, Long deptId
	) {
		QVacationRequest vacationRequest = QVacationRequest.vacationRequest;
		QMember member = QMember.member;
		QDept dept = QDept.dept;
		QCode position = QCode.code1;  // member.position
		QCode type = QCode.code1;      // vacationRequest.type

		BooleanBuilder builder = QueryDSLUtils.createEmptyCondition();

		if (status != null) {
			QueryDSLUtils.equal(builder, vacationRequest.status, status);
		}
		if (start != null) {
			QueryDSLUtils.greaterThanOrEqualTo(builder, vacationRequest.from, start);
		}
		if (end != null) {
			QueryDSLUtils.lessThan(builder, vacationRequest.to, end);
		}
		if (deptId != null && deptId != 0) {
			QueryDSLUtils.equal(builder,dept.id, deptId);
		}
		return queryFactory
			.select(Projections.constructor(
				VacationRequestCalendarResponse.class,
				member.name,
				dept.deptName,
				type.name,
				position.name,
				vacationRequest.from,
				vacationRequest.to
			))
			.from(vacationRequest)
			.join(vacationRequest.member, member)
			.join(member.dept, dept)
			.join(member.position, position)
			.join(vacationRequest.type, type)
			.where(builder)
			.fetch();
	}
}
