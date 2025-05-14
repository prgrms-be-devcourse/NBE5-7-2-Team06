package programmers.team6.domain.vacation.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.member.repository.MemberRepository;
import programmers.team6.domain.vacation.dto.ApprovalFirstStepDetailResponse;
import programmers.team6.domain.vacation.dto.ApprovalFirstStepSelectResponse;
import programmers.team6.domain.vacation.dto.ApprovalSecondStepDetailResponse;
import programmers.team6.domain.vacation.dto.ApprovalSecondStepSelectResponse;
import programmers.team6.domain.vacation.dto.ApprovalStepRejectRequest;
import programmers.team6.domain.vacation.dto.ApprovalStepSelectRequest;
import programmers.team6.domain.vacation.entity.ApprovalStep;
import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.entity.VacationRequest;
import programmers.team6.domain.vacation.repository.ApprovalStepRepository;
import programmers.team6.domain.vacation.repository.VacationInfoRepository;
import programmers.team6.domain.vacation.util.mapper.ApprovalStepMapper;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApprovalStepService {    // todo : 비즈니스 로직 분리 또는 엔티티 도메인 메서드로 리팩토링 고려

	private static final int STEP1 = 1;
	private static final int STEP2 = 2;

	private final ApprovalStepRepository approvalStepRepository;
	private final MemberRepository memberRepository;
	private final VacationInfoRepository vacationInfoRepository;

	public Page<ApprovalFirstStepSelectResponse> findFirstStepByMemberId(Long memberId, Pageable pageable) {
		return approvalStepRepository.findFirstStepByMemberId(memberId, STEP1, pageable);
	}

	public Page<ApprovalFirstStepSelectResponse> findFirstStepByFilter(
		ApprovalStepSelectRequest request, Long memberId, Pageable pageable) {
		return approvalStepRepository.findFirstStepByFilter(memberId, request.typeId(),
			request.name(), request.from(), request.to(), request.status(), STEP1, pageable);
	}

	public Page<ApprovalSecondStepSelectResponse> findSecondStepByMemberId(Long memberId, Pageable pageable) {
		return approvalStepRepository.findSecondStepByMemberId(memberId, STEP2, pageable);
	}

	public Page<ApprovalSecondStepSelectResponse> findSecondStepByFilter(
		ApprovalStepSelectRequest request, Long memberId, Pageable pageable) {
		return approvalStepRepository.findSecondStepByFilter(memberId, request.typeId(),
			request.name(), request.from(), request.to(), request.status(), STEP2, pageable);
	}

	public ApprovalFirstStepDetailResponse findFirstStepDetailById(Long approvalStepId, Long memberId) {
		ApprovalStep findApprovalStep = approvalStepRepository.findByIdAndMemberIdAndStep(approvalStepId,
				memberId, STEP1)
			.orElseThrow(() -> new IllegalArgumentException("해당 1차 결재 목록이 없습니다."));
		return ApprovalStepMapper.fromFirstStepEntity(findApprovalStep);
	}

	public ApprovalSecondStepDetailResponse findSecondStepDetailById(Long approvalStepId, Long memberId) {
		ApprovalStep findApprovalStep = approvalStepRepository.findByIdAndMemberIdAndStep(approvalStepId,
				memberId, STEP2)
			.orElseThrow(() -> new IllegalArgumentException("해당 2차 결재 목록이 없습니다."));
		return ApprovalStepMapper.fromSecondStepEntity(findApprovalStep);
	}

	@Transactional
	public void approveFirstStep(Long approvalStepId, Long memberId) {
		ApprovalStep firstStepApproval = approvalStepRepository.findByIdAndMemberIdAndStep(approvalStepId,
				memberId, STEP1)
			.orElseThrow(() -> new IllegalArgumentException("해당 1차 결재 목록이 없습니다."));

		firstStepApproval.validateApprovable();

		ApprovalStep secondStepApproval = approvalStepRepository.findByVacationRequestIdAndStep(
				firstStepApproval.getVacationRequestId(), STEP2)
			.orElseThrow(() -> new IllegalArgumentException("해당 2차 결재 목록이 없습니다."));

		firstStepApproval.approve();
		secondStepApproval.pending();

	}

	@Transactional
	public void rejectFirstStep(Long approvalStepId, Long memberId, ApprovalStepRejectRequest request) {
		ApprovalStep firstStepApproval = approvalStepRepository.findByIdAndMemberIdAndStep(approvalStepId,
				memberId, STEP1)
			.orElseThrow(() -> new IllegalArgumentException("해당 1차 결재 목록이 없습니다."));

		firstStepApproval.validateRejectable();

		ApprovalStep secondStepApproval = approvalStepRepository.findByVacationRequestIdAndStep(
				firstStepApproval.getVacationRequestId(), STEP2)
			.orElseThrow(() -> new IllegalArgumentException("해당 2차 결재 목록이 없습니다."));

		firstStepApproval.reject(request.reason());
		secondStepApproval.reject();
		firstStepApproval.rejectVacation();

	}

	@Transactional
	public void approveSecondStep(Long approvalStepId, Long memberId) {
		ApprovalStep findApprovalStep = approvalStepRepository.findByIdAndMemberIdAndStep(approvalStepId,
				memberId, STEP2)
			.orElseThrow(() -> new IllegalArgumentException("해당 2차 결재 목록이 없습니다."));

		findApprovalStep.validateApprovable();

		VacationInfo findVacationInfo = vacationInfoRepository.findByMemberIdAndVacationType(
				findApprovalStep.getVacationMemberId(),
				findApprovalStep.getVacationCode())
			.orElseThrow(() -> new IllegalArgumentException("해당 휴가 유형 정보가 없습니다."));

		int count = findApprovalStep.calcVacationDays();

		if (findVacationInfo.canUseVacation(count)) {
			findApprovalStep.approve();

			findApprovalStep.approveVacation();
			findVacationInfo.useVacation(count);
		} else {
			findApprovalStep.cancel();
			findApprovalStep.cancelVacation();

			// throw new IllegalArgumentException("잔여 연차 부족으로 취소되었습니다.");
			/*
				todo
				? : 예외를 터트리면 롤백됨.
				1. 해당 예외는 트랜잭션에서 제외 시키는 방법
				2. 예외처리를 하지말고, 해당 응답을 void가 아닌 상태, 메시지를 반환해주는 방법
					(성공이면 상태 + 휴가 결재 완료, 실패면 실패 + 잔여 연차 부족 ~~)
			 */
		}

	}

	@Transactional
	public void rejectSecondStep(Long approvalStepId, Long memberId, ApprovalStepRejectRequest request) {
		ApprovalStep findApprovalStep = approvalStepRepository.findByIdAndMemberIdAndStep(approvalStepId,
				memberId, STEP2)
			.orElseThrow(() -> new IllegalArgumentException("해당 2차 결재 목록이 없습니다."));

		findApprovalStep.validateRejectable();
		findApprovalStep.reject(request.reason());
		findApprovalStep.rejectVacation();

	}

	// 휴가 신청 시 호출되어, 해당 멤버의 결재 단계 생성
	public void saveApprovalStep(Long firstApproverId, Long secondApproverId, VacationRequest vacationRequest) {
		Member findFirstMember = memberRepository.findById(firstApproverId)
			.orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));
		Member findSecondMember = memberRepository.findById(secondApproverId)
			.orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));

		approvalStepRepository.save(ApprovalStepMapper.toEntity(findFirstMember, vacationRequest, STEP1));
		approvalStepRepository.save(ApprovalStepMapper.toEntity(findSecondMember, vacationRequest, STEP2));
	}

	// 휴가 요청 취소될 경우, 관련 결재 단계 상태 CANCELED
	public void cancelApprovalStep(Long vacationStepId) {
		List<ApprovalStep> findApprovalSteps = approvalStepRepository.findByVacationRequestId(vacationStepId);
		for (ApprovalStep findApprovalStep : findApprovalSteps) {
			findApprovalStep.cancel();
		}
	}

}
