package programmers.team6.domain.vacation.dto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import programmers.team6.domain.member.entity.Code_;
import programmers.team6.domain.member.entity.Dept_;
import programmers.team6.domain.member.entity.Member_;
import programmers.team6.domain.vacation.entity.ApprovalStep;
import programmers.team6.domain.vacation.entity.ApprovalStep_;
import programmers.team6.domain.vacation.entity.VacationRequest;
import programmers.team6.domain.vacation.entity.VacationRequest_;
import programmers.team6.domain.vacation.enums.Quarter;
import programmers.team6.domain.vacation.repository.VacationRequestRepository;
import programmers.team6.domain.vacation.utils.CriteriaCustomPredicateBuilder;
import programmers.team6.domain.vacation.utils.CriteriaCustomQueryBuilder;
import programmers.team6.domain.vacation.utils.QueryUtils;

@Repository
@RequiredArgsConstructor
public class VacationRequestSearchCustom {
	private final VacationRequestRepository vacationRequestRepository;
	private final EntityManager entityManager;

	/**
	 * ApprovalStep와 VacationRequest를 join하고 AdminVacationSearchCondition의 변수들을 통해 다중 필터 구현
	 * From<A,B> = 'from 엔티티'에서 엔티티 부분 , A타입의 객체부터 B타입의 속성을 탐색 (그래서 Root<A,A>이고 Join<A,B>임, Root와 Join 둘다 From 상속)
	 * SingularAttribute<C,D> = 메타모델에서 사용하는 단일 속성 타입 정보 , C = 특정 엔티티, D = C 엔티티의 필드 타입
	 * Path<X> = 메타모델 경로 혹은 루트로부터 탐색된 속성(특정 속성의 경로) , X = 속성 타입, 해당 path가 가르키는 최종 필드 타입
	 * @param searchCondition
	 * @param pageable
	 * @return
	 */
	public Page<VacationRequestReadResponse> search(AdminVacationSearchCondition searchCondition, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<VacationRequestReadResponse> cq = cb.createQuery(VacationRequestReadResponse.class);

		Root<ApprovalStep> as = cq.from(ApprovalStep.class);
		Join<ApprovalStep, VacationRequest> vr = as.join("vacationRequest", JoinType.INNER);

		/**
		 * 1. 각 휴가 신청서마다 최종 결제자 기준으로 조회
		 * 2. 휴가 신청 범위
		 * 3. 특정 년도 혹은 특정 분기 (1,2,3,4,상,하반기)
		 * 4. 휴가 신청자 이름
		 * 5. 부서 이름
		 * 6. 휴가 종류
		 * 7. 휴가 신청자 포지션
		 * 8. 결재자 포지션
		 * 9. 휴가 신청 상태
		 * 10. 휴가 결재자이름
		 */
		List<Predicate> predicates = CriteriaCustomPredicateBuilder.<ApprovalStep>builder(cb)
			.applyEqualFilter(as, ApprovalStep_.step, vr, VacationRequest_.lastApprovalStep)
			.applyDateRangeFilter(vr, VacationRequest_.from, VacationRequest_.to,
				searchCondition.dateRange().start(), searchCondition.dateRange().end())
			.applyDateRangeFilter(vr, VacationRequest_.from, VacationRequest_.to, Quarter.getStart(
				searchCondition.dateRange().year(), searchCondition.dateRange().quarter()), Quarter.getEnd(
				searchCondition.dateRange().year(), searchCondition.dateRange().quarter()))
			.applyLikeFilter(vr, searchCondition.applicant().name(), VacationRequest_.member, Member_.name)
			.applyLikeFilter(vr, searchCondition.applicant().deptName(), VacationRequest_.member, Member_.dept,
				Dept_.deptName)
			.applyEqualFilter(vr, searchCondition.applicant().vacationTypeCodeId(), VacationRequest_.type, Code_.id)
			.applyEqualFilter(vr, searchCondition.applicant().positionCodeId(), VacationRequest_.member,
				Member_.position, Code_.id)
			.applyEqualFilter(as, searchCondition.approver().positionCodeId(), VacationRequest_.member,
				Member_.position, Code_.id)
			.applyEqualFilter(vr, searchCondition.vacationRequestStatus(), VacationRequest_.status)
			.applyLikeFilter(as, searchCondition.approver().name(), ApprovalStep_.member, Member_.name)
			.build();

		/**
		 * Predicates들을 기반을 Query 생성
		 */
		TypedQuery<VacationRequestReadResponse> query = CriteriaCustomQueryBuilder.builder(
				cq, cb)
			.applyDynamicPredicates(predicates)
			.projection(VacationRequestReadResponse.class, vr.get(VacationRequest_.type),
				vr.get(VacationRequest_.from),
				vr.get(VacationRequest_.to),
				vr.get(VacationRequest_.member).get(Member_.name),
				as.get(ApprovalStep_.member).get(Member_.name),
				vr.get(VacationRequest_.member).get(Member_.dept).get(Dept_.deptName),
				vr.get(VacationRequest_.status))
			.orderByLatest(vr, VacationRequest_.createdAt)
			.createQuery(entityManager)
			.build();

		return QueryUtils.makeQueryToPageable(query, pageable,
			vacationRequestRepository.count());
	}
}
