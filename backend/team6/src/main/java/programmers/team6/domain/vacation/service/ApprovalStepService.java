package programmers.team6.domain.vacation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.dto.ApprovalFirstStepSelectResponse;
import programmers.team6.domain.vacation.dto.ApprovalStepSelectRequest;
import programmers.team6.domain.vacation.repository.ApprovalStepRepository;

@Service
@RequiredArgsConstructor
public class ApprovalStepService {
	private final ApprovalStepRepository approvalStepRepository;

	@Transactional(readOnly = true)
	public List<ApprovalFirstStepSelectResponse> findFirstStep(ApprovalStepSelectRequest request) {
		return approvalStepRepository.findFirstStepByMemberId(request.getMemberId());
	}

}
