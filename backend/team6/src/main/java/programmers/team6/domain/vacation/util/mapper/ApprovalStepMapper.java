package programmers.team6.domain.vacation.util.mapper;

import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.vacation.dto.ApprovalFirstStepDetailResponse;
import programmers.team6.domain.vacation.entity.ApprovalStep;
import programmers.team6.domain.vacation.entity.VacationRequest;
import programmers.team6.domain.vacation.enums.ApprovalStatus;

public class ApprovalStepMapper {
	public static ApprovalFirstStepDetailResponse fromEntity(ApprovalStep approvalStep) {
		return new ApprovalFirstStepDetailResponse(
			approvalStep.getId(),
			approvalStep.getVacationRequest().getMember().getName(),
			approvalStep.getVacationRequest().getMember().getDept().getDeptName(),
			approvalStep.getVacationRequest().getMember().getPosition().getName(),
			approvalStep.getApprovalStatus(),
			approvalStep.getVacationRequest().getType().getName(),
			approvalStep.getVacationRequest().getFrom(),
			approvalStep.getVacationRequest().getTo(),
			approvalStep.getVacationRequest().getReason(),
			approvalStep.getMember().getName()
		);
	}

	public static ApprovalStep toEntity(Member member, VacationRequest vacationRequest, int step) {
		ApprovalStatus approvalStatus = (step == 1) ? ApprovalStatus.IN_PROGRESS : ApprovalStatus.WAITING;
		return ApprovalStep.builder()
			.member(member)
			.vacationRequest(vacationRequest)
			.step(step)
			.approvalStatus(approvalStatus)
			.build();
	}
}
