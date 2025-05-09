package programmers.team6.domain.vacation.service;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.dto.ApprovalFirstStepSelectResponse;
import programmers.team6.domain.vacation.dto.ApprovalStepSelectRequest;
import programmers.team6.domain.vacation.repository.ApprovalStepRepository;

@Service
@RequiredArgsConstructor
public class ApprovalStepService {
	private static final int STEP1 = 1;
	private static final int STEP2 = 2;

	private final ApprovalStepRepository approvalStepRepository;

	@Transactional(readOnly = true)
	public Page<ApprovalFirstStepSelectResponse> findFirstStep(Long memberId, Pageable pageable) {
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

	public void validateMember(Long memberId) {
		if (!approvalStepRepository.existsByMemberId(memberId)) {
			throw new NoSuchElementException("결재 목록 데이터가 없습니다.");
		}
	}

}
