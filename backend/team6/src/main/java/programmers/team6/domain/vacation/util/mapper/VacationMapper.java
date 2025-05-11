package programmers.team6.domain.vacation.util.mapper;

import org.springframework.stereotype.Component;

import programmers.team6.domain.member.entity.Code;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.vacation.dto.VacationCreateRequestDto;
import programmers.team6.domain.vacation.dto.VacationCreateResponseDto;
import programmers.team6.domain.vacation.dto.VacationInfoSelectResponseDto;
import programmers.team6.domain.vacation.dto.VacationListResponseDto;
import programmers.team6.domain.vacation.entity.ApprovalStep;
import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.entity.VacationRequest;
import programmers.team6.domain.vacation.enums.ApprovalStatus;

@Component
public class VacationMapper {
	public VacationInfoSelectResponseDto toVacationInfoSelectResponseDto(VacationInfo vacationInfo) {
		return VacationInfoSelectResponseDto.builder()
			.totalCount(vacationInfo.getTotalCount())
			.useCount(vacationInfo.getUseCount())
			.remainCount(vacationInfo.getRemainCount())
			.build();
	}

	public VacationRequest toVacationRequest(VacationCreateRequestDto requestDto, Code vacationType,
		ApprovalStatus status, Member requester) {
		return VacationRequest.builder()
			.from(requestDto.getFrom())
			.to(requestDto.getTo())
			.reason(requestDto.getReason())
			.type(vacationType)
			.status(status)
			.requester(requester)
			.build();
	}

	public ApprovalStep toApprovalStep(Member approver, VacationRequest vacationRequest, int step,
		ApprovalStatus status) {
		return ApprovalStep.builder()
			.member(approver)
			.vacationRequest(vacationRequest)
			.step(step)
			.status(status)
			.build();
	}

	public VacationCreateResponseDto toVacationCreateResponseDto(
		VacationRequest vacationRequest,
		String vacationTypeName,
		ApprovalStatus approvalStatus,
		String approverName) {
		return VacationCreateResponseDto.builder()
			.from(vacationRequest.getFrom())
			.to(vacationRequest.getTo())
			.reason(vacationRequest.getReason())
			.vacationType(vacationTypeName)
			.approvalStatus(approvalStatus.name())
			.approverName(approverName)
			.build();
	}

	public VacationListResponseDto toVacationRequestListResponseDto(
		VacationRequest vacationRequest,
		String vacationTypeName,
		String approvalStatus,
		String approverName) {
		return VacationListResponseDto.builder()
			.requestId(vacationRequest.getId())
			.from(vacationRequest.getFrom())
			.to(vacationRequest.getTo())
			.reason(vacationRequest.getReason())
			.vacationType(vacationTypeName)
			.approvalStatus(approvalStatus)
			.approverName(approverName)
			.createdAt(vacationRequest.getCreatedAt())
			.build();
	}

}