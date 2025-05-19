package programmers.team6.domain.vacation.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import programmers.team6.domain.member.entity.Dept;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.member.service.DeptService;
import programmers.team6.domain.vacation.dto.ApprovalFirstStepDetailResponse;
import programmers.team6.domain.vacation.dto.ApprovalFirstStepSelectResponse;
import programmers.team6.domain.vacation.dto.ApprovalSecondStepDetailResponse;
import programmers.team6.domain.vacation.dto.ApprovalSecondStepSelectResponse;
import programmers.team6.domain.vacation.dto.ApprovalStepRejectRequest;
import programmers.team6.domain.vacation.dto.ApprovalStepSelectRequest;
import programmers.team6.domain.vacation.entity.ApprovalStep;
import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.entity.VacationInfoLog;
import programmers.team6.domain.vacation.entity.VacationRequest;
import programmers.team6.domain.vacation.repository.ApprovalStepRepository;
import programmers.team6.domain.vacation.repository.VacationInfoRepository;
import programmers.team6.domain.vacation.util.mapper.ApprovalStepMapper;
import programmers.team6.global.exception.code.NotFoundErrorCode;
import programmers.team6.global.exception.customException.NotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApprovalStepService {

	private static final int STEP1 = 1;
	private static final int STEP2 = 2;

	private final ApprovalStepRepository approvalStepRepository;
	private final VacationInfoRepository vacationInfoRepository;
	private final VacationInfoLogPublisher vacationInfoLogPublisher;
	private final DeptService deptService;

	public Page<ApprovalFirstStepSelectResponse> findFirstStepByMemberId(Long memberId, Pageable pageable) {
		return approvalStepRepository.findFirstStepByMemberId(memberId, STEP1, pageable);
	}

	public Page<ApprovalFirstStepSelectResponse> findFirstStepByFilter(
		ApprovalStepSelectRequest request, Long memberId, Pageable pageable) {
		return approvalStepRepository.findFirstStepByFilter(memberId, request.type(),
			request.name(), request.from(), request.to(), request.status(), STEP1, pageable);
	}

	public Page<ApprovalSecondStepSelectResponse> findSecondStepByMemberId(Long memberId, Pageable pageable) {
		return approvalStepRepository.findSecondStepByMemberId(memberId, STEP2, pageable);
	}

	public Page<ApprovalSecondStepSelectResponse> findSecondStepByFilter(
		ApprovalStepSelectRequest request, Long memberId, Pageable pageable) {
		return approvalStepRepository.findSecondStepByFilter(memberId, request.type(),
			request.name(), request.from(), request.to(), request.status(), STEP2, pageable);
	}

	public ApprovalFirstStepDetailResponse findFirstStepDetailById(Long approvalStepId, Long memberId) {
		ApprovalStep findApprovalStep = findByIdAndMemberIdAndStep(approvalStepId, memberId, STEP1);
		return ApprovalStepMapper.fromFirstStepEntity(findApprovalStep);
	}

	public ApprovalSecondStepDetailResponse findSecondStepDetailById(Long approvalStepId, Long memberId) {
		ApprovalStep findApprovalStep = findByIdAndMemberIdAndStep(approvalStepId, memberId, STEP2);
		return ApprovalStepMapper.fromSecondStepEntity(findApprovalStep);
	}

	@Transactional
	public void approveFirstStep(Long approvalStepId, Long memberId) {
		ApprovalStep firstStepApproval = findByIdAndMemberIdAndStep(approvalStepId, memberId, STEP1);

		firstStepApproval.validateApprovable();

		ApprovalStep secondStepApproval = findByVacationRequestIdAndStep(firstStepApproval.getVacationRequest(),
			STEP2);

		firstStepApproval.approve();
		secondStepApproval.pending();

	}

	@Transactional
	public void rejectFirstStep(Long approvalStepId, Long memberId, ApprovalStepRejectRequest request) {
		ApprovalStep firstStepApproval = findByIdAndMemberIdAndStep(approvalStepId, memberId, STEP1);

		firstStepApproval.validateRejectable();

		ApprovalStep secondStepApproval = findByVacationRequestIdAndStep(firstStepApproval.getVacationRequest(),
			STEP2);

		firstStepApproval.reject(request.reason());
		secondStepApproval.reject();
		firstStepApproval.rejectVacation();

	}

	@Transactional
	public void approveSecondStep(Long approvalStepId, Long memberId) {
		ApprovalStep findApprovalStep = findByIdAndMemberIdAndStep(approvalStepId, memberId, STEP2);

		findApprovalStep.validateApprovable();

		VacationInfo findVacationInfo = vacationInfoRepository.findByMemberIdAndVacationType(
				findApprovalStep.getVacationMemberId(),
				findApprovalStep.isHalfDay() ? "01" : findApprovalStep.getVacationCode())
			.orElseThrow(() -> new NotFoundException(NotFoundErrorCode.NOT_FOUND_VACATION_INFO));

		double count = findApprovalStep.isHalfDay() ? 0.5 : findApprovalStep.calcVacationDays();
		if (findVacationInfo.canUseVacation(count)) {
			findApprovalStep.approve();
			findApprovalStep.approveVacation();
			VacationInfoLog log = findVacationInfo.useVacation(count);
			vacationInfoLogPublisher.publish(log);
		} else {
			findApprovalStep.cancel();
			findApprovalStep.cancelVacation();

			// throw new 예외("잔여 연차 부족으로 취소되었습니다.");
			/*
				todo
				? : 예외를 터트리면 롤백됨.
				1. 예외 터트리고 해당 예외는 트랜잭션에서 제외 시키는 방법
				2. 예외처리를 하지말고, 응답을 void가 아닌 상태, dto를 반환해주는 방법
					(성공이면 상태 + "휴가 결재 완료", 실패면 실패 + "잔여 연차 부족 ~~")
			 */
		}

	}

	@Transactional
	public void rejectSecondStep(Long approvalStepId, Long memberId, ApprovalStepRejectRequest request) {
		ApprovalStep findApprovalStep = findByIdAndMemberIdAndStep(approvalStepId, memberId, STEP2);

		findApprovalStep.validateRejectable();
		findApprovalStep.reject(request.reason());
		findApprovalStep.rejectVacation();

	}

	// 휴가 신청 시 호출되어, 해당 멤버의 결재 단계 생성
	public void saveApprovalStep(Member firstApprover, VacationRequest vacationRequest) {
		// todo: 2차 결재자 지정 기능 (시스템상 구현 필요)
		Dept findDept = deptService.findByDeptName("인사팀");
		approvalStepRepository.save(ApprovalStepMapper.toEntity(firstApprover, vacationRequest, STEP1));
		approvalStepRepository.save(ApprovalStepMapper.toEntity(findDept.getDeptLeader(), vacationRequest, STEP2));
	}

	// 휴가 요청 취소될 경우, 관련 결재 단계 상태 CANCELED
	public void cancelApprovalStep(VacationRequest vacationStep) {
		List<ApprovalStep> findApprovalSteps = approvalStepRepository.findByVacationRequest(vacationStep);
		for (ApprovalStep findApprovalStep : findApprovalSteps) {
			findApprovalStep.cancel();
		}
	}

	private ApprovalStep findByIdAndMemberIdAndStep(Long approvalStepId, Long memberId, int step) {
		return approvalStepRepository.findByIdAndMemberIdAndStep(approvalStepId, memberId, step)
			.orElseThrow(() -> new NotFoundException(NotFoundErrorCode.NOT_FOUND_APPROVAL_STEP));
	}

	private ApprovalStep findByVacationRequestIdAndStep(VacationRequest vacationRequest, int step) {
		return approvalStepRepository.findByVacationRequestAndStep(vacationRequest, step)
			.orElseThrow(() -> new NotFoundException(NotFoundErrorCode.NOT_FOUND_APPROVAL_STEP));
	}
}
