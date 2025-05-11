package programmers.team6.domain.vacation.service;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.member.repository.MemberRepository;
import programmers.team6.domain.vacation.dto.ApprovalFirstStepDetailResponse;
import programmers.team6.domain.vacation.dto.ApprovalFirstStepSelectResponse;
import programmers.team6.domain.vacation.dto.ApprovalSecondStepDetailResponse;
import programmers.team6.domain.vacation.dto.ApprovalSecondStepSelectResponse;
import programmers.team6.domain.vacation.dto.ApprovalStepRejectRequest;
import programmers.team6.domain.vacation.dto.ApprovalStepSelectRequest;
import programmers.team6.domain.vacation.entity.ApprovalStep;
import programmers.team6.domain.vacation.entity.VacationRequest;
import programmers.team6.domain.vacation.enums.ApprovalStatus;
import programmers.team6.domain.vacation.enums.VacationRequestStatus;
import programmers.team6.domain.vacation.repository.ApprovalStepRepository;
import programmers.team6.domain.vacation.util.mapper.ApprovalStepMapper;

@Service
@RequiredArgsConstructor
public class ApprovalStepService {
	private static final int STEP1 = 1;
	private static final int STEP2 = 2;

	private final ApprovalStepRepository approvalStepRepository;
	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public Page<ApprovalFirstStepSelectResponse> findFirstStepByMemberId(Long memberId, Pageable pageable) {
		validateMember(memberId);
		return approvalStepRepository.findFirstStepByMemberId(memberId, STEP1, pageable);
	}

	@Transactional(readOnly = true)
	public Page<ApprovalFirstStepSelectResponse> findFirstStepByFilter(
		ApprovalStepSelectRequest request, Long memberId, Pageable pageable) {
		validateMember(memberId);
		return approvalStepRepository.findFirstStepByFilter(memberId, request.typeId(),
			request.name(), request.from(), request.to(), request.status(), STEP1, pageable);
	}

	@Transactional(readOnly = true)
	public Page<ApprovalSecondStepSelectResponse> findSecondStepByMemberId(Long memberId, Pageable pageable) {
		validateMember(memberId);
		return approvalStepRepository.findSecondStepByMemberId(memberId, STEP2, pageable);
	}

	@Transactional(readOnly = true)
	public Page<ApprovalSecondStepSelectResponse> findSecondStepByFilter(
		ApprovalStepSelectRequest request, Long memberId, Pageable pageable) {
		validateMember(memberId);
		return approvalStepRepository.findSecondStepByFilter(memberId, request.typeId(),
			request.name(), request.from(), request.to(), request.status(), STEP2, pageable);
	}

	public void validateMember(Long memberId) {
		if (!approvalStepRepository.existsByMemberId(memberId)) {
			throw new NoSuchElementException("결재 목록이 없습니다.");
		}
	}

	@Transactional(readOnly = true)
	public ApprovalFirstStepDetailResponse findFirstStepDetailById(Long approvalStepId, Long memberId) {
		ApprovalStep findApprovalStep = approvalStepRepository.findByIdAndMemberIdAndStep(approvalStepId, memberId,
				STEP1)
			.orElseThrow(() -> new IllegalArgumentException("해당 1차 결재 목록이 없습니다."));
		return ApprovalStepMapper.fromFirstStepEntity(findApprovalStep);
	}

	@Transactional(readOnly = true)
	public ApprovalSecondStepDetailResponse findSecondStepDetailById(Long approvalStepId, Long memberId) {
		ApprovalStep findApprovalStep = approvalStepRepository.findByIdAndMemberIdAndStep(approvalStepId, memberId,
				STEP2)
			.orElseThrow(() -> new IllegalArgumentException("해당 2차 결재 목록이 없습니다."));
		return ApprovalStepMapper.fromSecondStepEntity(findApprovalStep);
	}

	@Transactional
	public void approveFirstStep(Long approvalStepId, Long memberId) {
		ApprovalStep findApprovalStep1 = approvalStepRepository.findByIdAndMemberIdAndStep(approvalStepId, memberId,
				STEP1)
			.orElseThrow(() -> new IllegalArgumentException("해당 1차 결재 목록이 없습니다."));
		if (!findApprovalStep1.isApprovable()) {
			throw new IllegalArgumentException("해당 결재를 승인할 수 없습니다.");
		}

		ApprovalStep findApprovalStep2 = approvalStepRepository.findByVacationRequestIdAndStep(
				findApprovalStep1.getVacationRequest().getId(), STEP2)
			.orElseThrow(() -> new IllegalArgumentException("해당 2차 결재 목록이 없습니다."));

		updateApprovalStepStatus(findApprovalStep1, ApprovalStatus.APPROVED, null);
		updateApprovalStepStatus(findApprovalStep2, ApprovalStatus.IN_PROGRESS, null);

	}

	@Transactional
	public void rejectFirstStep(Long approvalStepId, Long memberId, ApprovalStepRejectRequest request) {
		ApprovalStep findApprovalStep1 = approvalStepRepository.findByIdAndMemberIdAndStep(approvalStepId, memberId,
				STEP1)
			.orElseThrow(() -> new IllegalArgumentException("해당 1차 결재 목록이 없습니다."));
		if (!findApprovalStep1.isApprovable()) {
			throw new IllegalArgumentException("해당 결재를 반려할 수 없습니다.");
		}

		ApprovalStep findApprovalStep2 = approvalStepRepository.findByVacationRequestIdAndStep(
				findApprovalStep1.getVacationRequest().getId(), STEP2)
			.orElseThrow(() -> new IllegalArgumentException("해당 2차 결재 목록이 없습니다."));

		updateApprovalStepStatus(findApprovalStep1, ApprovalStatus.REJECTED, request.reason());
		updateApprovalStepStatus(findApprovalStep2, ApprovalStatus.REJECTED, null);

		findApprovalStep1.getVacationRequest().updateStatus(VacationRequestStatus.REJECTED);

	}

	@Transactional
	public void approveSecondStep(Long approvalStepId, Long memberId) {
		ApprovalStep findApprovalStep = approvalStepRepository.findByIdAndMemberIdAndStep(approvalStepId, memberId,
				STEP2)
			.orElseThrow(() -> new IllegalArgumentException("해당 2차 결재 목록이 없습니다."));
		if (!findApprovalStep.isApprovable()) {
			throw new IllegalArgumentException("해당 결재를 승인할 수 없습니다.");
		}

		updateApprovalStepStatus(findApprovalStep, ApprovalStatus.APPROVED, null);

		findApprovalStep.getVacationRequest().updateStatus(VacationRequestStatus.APPROVED);

		// todo: 연차 계산 후 차감 로직

	}

	@Transactional
	public void rejectSecondStep(Long approvalStepId, Long memberId, ApprovalStepRejectRequest request) {
		ApprovalStep findApprovalStep = approvalStepRepository.findByIdAndMemberIdAndStep(approvalStepId, memberId,
				STEP2)
			.orElseThrow(() -> new IllegalArgumentException("해당 2차 결재 목록이 없습니다."));
		if (!findApprovalStep.isApprovable()) {
			throw new IllegalArgumentException("해당 결재를 반려할 수 없습니다.");
		}

		updateApprovalStepStatus(findApprovalStep, ApprovalStatus.REJECTED, request.reason());

		findApprovalStep.getVacationRequest().updateStatus(VacationRequestStatus.REJECTED);

	}

	private void updateApprovalStepStatus(ApprovalStep approvalStep, ApprovalStatus approvalStatus,
		String reason) {
		if (reason == null) {
			approvalStep.apply(approvalStatus);
		} else {
			approvalStep.apply(approvalStatus, reason);
		}
	}

	// 서비스 계층 전용 메서드
	public void saveApprovalStep(Long memberId, VacationRequest vacationRequest, int step) {
		Member findMember = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));
		approvalStepRepository.save(ApprovalStepMapper.toEntity(findMember, vacationRequest, step));
	}

}
