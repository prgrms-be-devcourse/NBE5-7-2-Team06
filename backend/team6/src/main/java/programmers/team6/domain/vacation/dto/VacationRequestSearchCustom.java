package programmers.team6.domain.vacation.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.entity.ApprovalStep;
import programmers.team6.domain.vacation.entity.VacationRequest;
import programmers.team6.domain.vacation.repository.VacationRequestRepository;

@Repository
@RequiredArgsConstructor
public class VacationRequestSearchCustom {
	private final VacationRequestRepository vacationRequestRepository;
	private final EntityManager entityManager;
	private CriteriaBuilder cb;
	private List<Predicate> predicates;

	/**
	 * ApprovalStep와 VacationRequest를 join하고 AdminVacationSearchCondition의 변수들을 통해 다중 필터 구현
	 * @param searchCondition
	 * @param pageable
	 * @return
	 */
	public Page<VacationRequestReadResponse> search(AdminVacationSearchCondition searchCondition, Pageable pageable) {
		this.cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<VacationRequestReadResponse> cq = cb.createQuery(VacationRequestReadResponse.class);

		Root<ApprovalStep> as = cq.from(ApprovalStep.class);
		Join<ApprovalStep, VacationRequest> vr = as.join("vacationRequest", JoinType.INNER);
		this.predicates = new ArrayList<>();
		this.predicates.add(cb.equal(as.get("step"), vr.get("lastApprovalStep")));

		// 휴가 신청 범위
		addPredicateBetweenDates(vr, Optional.ofNullable(searchCondition.getStart()),
			Optional.ofNullable(searchCondition.getEnd()));
		// 특정 분기 (1,2,3,4,상,하반기)
		addPredicateBetweenDates(vr, Quarter.getStartDate(searchCondition.getYear(), searchCondition.getQuarter()),
			Quarter.getEndDate(searchCondition.getYear(), searchCondition.getQuarter()));
		// 휴가 신청자 이름
		addPredicateLike(vr, new String[] {"member", "name"}, searchCondition.getApplicantName());
		// 부서 이름
		addPredicateLike(vr, new String[]{"member", "dept","deptName"}, searchCondition.getDeptName());
		// code id를 활용
		if (searchCondition.getCodeId() != null) {
			// 휴가 종류
			Predicate vacationRequestType = cb.equal(vr.get("type").get("id"), searchCondition.getCodeId());
			// 휴가 신청자 포지션
			Predicate vacationRequestMemberPosition = cb.equal(vr.get("member").get("position").get("id"), searchCondition.getCodeId());
			// 결재자 포지션
			Predicate approvalStepMemberPosition = cb.equal(as.get("member").get("position").get("id"), searchCondition.getCodeId());
			predicates.add(cb.or(vacationRequestType, vacationRequestMemberPosition, approvalStepMemberPosition));
		}
		// 휴가 신청 상태
		addPredicateEqual(vr, new String[] {"status"}, searchCondition.getVacationRequestStatus());
		// 휴가 결재자 이름
		addPredicateLike(as, new String[] {"member", "name"}, searchCondition.getApproverName());

		cq.where(cb.and(predicates.toArray(new Predicate[0])));

		// projection
		cq.select(cb.construct(VacationRequestReadResponse.class,
			vr.get("type"),
			vr.get("from"),
			vr.get("to"),
			vr.get("member").get("name"),
			as.get("member").get("name"),
			vr.get("member").get("dept").get("deptName"),
			vr.get("status")
		));

		// pageable
		List<VacationRequestReadResponse> result = entityManager.createQuery(cq)
			.setFirstResult((int)pageable.getOffset())
			.setMaxResults(pageable.getPageSize())
			.getResultList();

		Long totalCount = vacationRequestRepository.count();
		return new PageImpl<>(result, pageable, totalCount);
	}

	private void addPredicateBetweenDates(From root, Optional<LocalDate> start,
		Optional<LocalDate> end) {
		if (start.isPresent() && end.isPresent()) {
			predicates.add(cb.lessThanOrEqualTo(root.get("from"), end.get()));
			predicates.add(cb.greaterThanOrEqualTo(root.get("to"), start.get()));
		}
	}

	private <T> void addPredicateEqual(Join root, String[] targetFieldNames, T conditionValue) {
		if (conditionValue != null) {
			Path path = root.get(targetFieldNames[0]);
			for (int i = 1; i < targetFieldNames.length; i++) {
				path = path.get(targetFieldNames[i]);
			}
			predicates.add(cb.equal(path, conditionValue));
		}
	}

	private <T> void addPredicateLike(From root, String[] targetFieldNames, T conditionValue) {
		if (conditionValue != null) {
			Path path = root.get(targetFieldNames[0]);
			for (int i = 1; i < targetFieldNames.length; i++) {
				path = path.get(targetFieldNames[i]);
			}
			predicates.add(cb.like(path, "%" + conditionValue + "%"));
		}
	}
}
