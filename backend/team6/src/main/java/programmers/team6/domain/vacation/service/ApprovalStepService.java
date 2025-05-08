package programmers.team6.domain.vacation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.dto.ApprovalFirstStepSelectResponse;
import programmers.team6.domain.vacation.dto.ApprovalStepFilterRequest;
import programmers.team6.domain.vacation.dto.ApprovalStepSelectRequest;
import programmers.team6.domain.vacation.repository.ApprovalStepRepository;

@Service
@RequiredArgsConstructor
public class ApprovalStepService {
	private static final int STEP1 = 1;
	private static final int STEP2 = 2;

	private final ApprovalStepRepository approvalStepRepository;

	@Transactional(readOnly = true)
	public Page<ApprovalFirstStepSelectResponse> findFirstStep(ApprovalStepSelectRequest request, Pageable pageable) {
		validateMember(request.getMemberId());
		return approvalStepRepository.findFirstStepByMemberId(request.getMemberId(), STEP1, pageable);
	}

	@Transactional(readOnly = true)
	public Page<ApprovalFirstStepSelectResponse> findFirstStepByFilter(ApprovalStepFilterRequest request,
		Pageable pageable) {
		validateMember(request.getMemberId());
		return approvalStepRepository.findFirstStepByFilter(request.getMemberId(), request.getTypeId(),
			request.getName(), request.getFrom(), request.getTo(), request.getStatus(), STEP1, pageable);
	}

	public void validateMember(Long memberId) {
		if (!approvalStepRepository.existsByMemberId(memberId)) {
			throw new IllegalArgumentException("결재 목록 데이터가 없습니다.");
		}
	}

}
