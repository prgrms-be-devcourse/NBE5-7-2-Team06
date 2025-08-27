package programmers.team6.domain.admin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.admin.dto.AdminVacationSearchCondition;
import programmers.team6.domain.admin.dto.VacationRequestSearchResponse;
import programmers.team6.domain.admin.utils.QueryDSLUtils;
import programmers.team6.domain.member.entity.QCode;
import programmers.team6.domain.member.entity.QDept;
import programmers.team6.domain.member.entity.QMember;
import programmers.team6.domain.vacation.entity.QApprovalStep;
import programmers.team6.domain.vacation.entity.QVacationRequest;

@Repository
@RequiredArgsConstructor
public class AdminVacationRequestSearchCustom {
	private final JPAQueryFactory queryFactory;

	/**
	 * ApprovalStep와 VacationRequest를 join하고 AdminVacationSearchCondition의 변수들을 통해 다중 필터 구현
	 * From<A,B> = 'from 엔티티'에서 엔티티 부분 , A타입의 객체부터 B타입의 속성을 탐색 (그래서 Root<A,A>이고 Join<A,B>임, Root와 Join 둘다 From 상속)
	 * SingularAttribute<C,D> = 메타모델에서 사용하는 단일 속성 타입 정보 , C = 특정 엔티티, D = C 엔티티의 필드 타입
	 * Path<X> = 메타모델 경로 혹은 루트로부터 탐색된 속성(특정 속성의 경로) , X = 속성 타입, 해당 path가 가르키는 최종 필드 타입
	 * @param searchCondition 검색 필터
	 * @param pageable 페이징 정보
	 * @return 검색 결과 페이지
	 */
	public Page<VacationRequestSearchResponse> search(AdminVacationSearchCondition searchCondition, Pageable pageable) {

		QApprovalStep approvalStep = QApprovalStep.approvalStep;
		QVacationRequest vacationRequest = QVacationRequest.vacationRequest;
		QMember member = QMember.member;
		QCode code = QCode.code1;
		QDept dept = QDept.dept;

		/** 필터링
		 * 1. 휴가 신청 범위
		 * 2. 특정 년도 혹은 특정 분기 (1,2,3,4,상,하반기)
		 * 3. 휴가 신청자 이름
		 * 4. 부서 이름
		 * 5. 휴가 종류
		 * 6. 휴가 신청자 포지션
		 * 7. 휴가 신청 상태
		 */
		BooleanBuilder builder = QueryDSLUtils.createEmptyCondition();
		if (searchCondition.dateRange() != null) {
			if (searchCondition.dateRange().start() != null && searchCondition.dateRange().end() != null) {
				QueryDSLUtils.dataRange(builder, vacationRequest.from, vacationRequest.to,
					searchCondition.dateRange().start(), searchCondition.dateRange().end());
			}
			if (searchCondition.dateRange().year() != null && searchCondition.dateRange().quarter() != null) {
				QueryDSLUtils.dataRange(builder, vacationRequest.from, vacationRequest.to,
					searchCondition.dateRange().quarter().getStart(searchCondition.dateRange().year()),
					searchCondition.dateRange().quarter().getEnd(searchCondition.dateRange().year()));
			}
		}

		if (searchCondition.applicant().name() != null) {
			QueryDSLUtils.containsIgnoreCase(builder, member.name, searchCondition.applicant().name());
		}

		if (searchCondition.applicant().deptName() != null) {
			QueryDSLUtils.containsIgnoreCase(builder, dept.deptName, searchCondition.applicant().deptName());
		}
		if (searchCondition.applicant().vacationTypeCodeId() != null) {
			QueryDSLUtils.equal(builder, code.id, searchCondition.applicant().vacationTypeCodeId());
		}
		if (searchCondition.applicant().positionCodeId() != null) {
			QueryDSLUtils.equal(builder, member.position.id, searchCondition.applicant().positionCodeId());
		}
		if (searchCondition.vacationRequestStatus() != null) {
			QueryDSLUtils.equal(builder, vacationRequest.status, searchCondition.vacationRequestStatus());
		}

		//⃣ pageable 적용을 위한 vacationRequest ID 먼저 조회
		List<Long> vacationRequestIds = queryFactory
			.select(vacationRequest.id)
			.from(vacationRequest)
			.join(vacationRequest.member, member)
			.join(member.dept, dept)
			.join(vacationRequest.type, code)
			.where(builder)
			.orderBy(vacationRequest.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		//⃣ ID 기준으로 join fetch 하여 최종 결과 생성
		List<VacationRequestSearchResponse> content = queryFactory
			.select(Projections.constructor(VacationRequestSearchResponse.class,
				vacationRequest.id,
				code.name,
				vacationRequest.from,
				vacationRequest.to,
				member.name,
				JPAExpressions.select(
						Expressions.stringTemplate("group_concat({0})", approvalStep.member.name))
					.from(approvalStep)
					.where(approvalStep.vacationRequest.eq(vacationRequest)),
				dept.deptName,
				vacationRequest.status))
			.from(vacationRequest)
			.join(vacationRequest.member, member)
			.join(member.dept, dept)
			.join(vacationRequest.type, code)
			.where(vacationRequest.id.in(vacationRequestIds))
			.orderBy(vacationRequest.createdAt.desc())
			.fetch();

		return PageableExecutionUtils.getPage(content, pageable, () -> vacationRequestIds.size());
	}
}
