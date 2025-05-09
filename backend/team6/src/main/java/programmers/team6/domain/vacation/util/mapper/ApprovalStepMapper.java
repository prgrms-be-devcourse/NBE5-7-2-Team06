package programmers.team6.domain.vacation.util.mapper;

import programmers.team6.domain.vacation.dto.ApprovalFirstStepDetailResponse;
import programmers.team6.domain.vacation.entity.ApprovalStep;

public class ApprovalStepMapper {
	public static ApprovalFirstStepDetailResponse fromEntity(ApprovalStep approvalStep) {
		return new ApprovalFirstStepDetailResponse(
			approvalStep.getId(),
			approvalStep.getVacationRequest().getMember().getName(),
			approvalStep.getVacationRequest().getMember().getDept().getDeptName(),
			approvalStep.getVacationRequest().getMember().getPosition().getName(),
			approvalStep.getStatus(),
			approvalStep.getVacationRequest().getType().getName(),
			approvalStep.getVacationRequest().getFrom(),
			approvalStep.getVacationRequest().getTo(),
			approvalStep.getVacationRequest().getReason(),
			approvalStep.getMember().getName()
		);
	}
}
