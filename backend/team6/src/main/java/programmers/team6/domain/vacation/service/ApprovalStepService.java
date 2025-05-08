package programmers.team6.domain.vacation.service;

import java.util.List;

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
	public List<ApprovalFirstStepSelectResponse> findFirstStep(ApprovalStepSelectRequest request) {
		return approvalStepRepository.findFirstStepByMemberId(request.getMemberId(), STEP1);
	}

	@Transactional(readOnly = true)
	public List<ApprovalFirstStepSelectResponse> findFirstStepByFilter(ApprovalStepFilterRequest request) {
		return approvalStepRepository.findFirstStepByFilter(request.getMemberId(), request.getTypeId(),
			request.getName(), request.getFrom(), request.getTo(), request.getStatus(), STEP1);
	}

}
