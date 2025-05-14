package programmers.team6.domain.vacation.util.mapper;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import programmers.team6.domain.member.entity.Code;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.vacation.dto.VacationCreateRequestDto;
import programmers.team6.domain.vacation.dto.VacationCreateResponseDto;
import programmers.team6.domain.vacation.dto.VacationInfoSelectResponseDto;
import programmers.team6.domain.vacation.dto.VacationListResponseDto;
import programmers.team6.domain.vacation.dto.VacationUpdateResponseDto;
import programmers.team6.domain.vacation.entity.ApprovalStep;
import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.entity.VacationRequest;
import programmers.team6.domain.vacation.enums.VacationRequestStatus;

@Component
public class VacationMapper {
	// VacationInfo → VacationInfoSelectResponseDto
	public VacationInfoSelectResponseDto toVacationInfoSelectResponseDto(VacationInfo vacationInfo) {
		return VacationInfoSelectResponseDto.builder()
			.totalCount(vacationInfo.getTotalCount())
			.useCount(vacationInfo.getUseCount())
			.remainCount(vacationInfo.getRemainCount())
			.build();
	}

	// VacationCreateRequestDto → VacationRequest
	public VacationRequest toVacationRequest(VacationCreateRequestDto requestDto, Code vacationType,
		VacationRequestStatus status, Member requester) {
		return VacationRequest.builder()
			.from(requestDto.getFrom())
			.to(requestDto.getTo())
			.reason(requestDto.getReason())
			.type(vacationType)
			.status(status)
			.requester(requester)
			.build();
	}

	// 매개변수 → ApprovalStep
	public ApprovalStep toApprovalStep(Member approver, VacationRequest vacationRequest, int step,
		VacationRequestStatus status) {
		return ApprovalStep.builder()
			.member(approver)
			.vacationRequest(vacationRequest)
			.step(step)
			.status(status)
			.build();
	}

	// VacationRequest → VacationCreateResponseDto
	public VacationCreateResponseDto toVacationCreateResponseDto(
		VacationRequest vacationRequest,
		String vacationTypeName,
		VacationRequestStatus vacationRequestStatus,
		String approverName) {
		return VacationCreateResponseDto.builder()
			.requestId(vacationRequest.getId())
			.from(vacationRequest.getFrom())
			.to(vacationRequest.getTo())
			.reason(vacationRequest.getReason())
			.vacationType(vacationTypeName)
			.approvalStatus(vacationRequestStatus.name())
			.approverName(approverName)
			.createdAt(vacationRequest.getCreatedAt())
			.updatedAt(vacationRequest.getUpdatedAt())
			.build();
	}

	// VacationRequest → VacationUpdateResponseDto
	// 휴가 요청 수정 후 응답 DTO 생성
	public VacationUpdateResponseDto toVacationUpdateResponseDto(
		VacationRequest vacationRequest,
		String vacationTypeName,
		String approverName) {
		return VacationUpdateResponseDto.builder()
			.requestId(vacationRequest.getId())
			.from(vacationRequest.getFrom())
			.to(vacationRequest.getTo())
			.reason(vacationRequest.getReason())
			.vacationType(vacationTypeName)
			.approvalStatus(vacationRequest.getStatus().name())
			.approverName(approverName)
			.updatedAt(vacationRequest.getUpdatedAt())
			.build();
	}

	//
	public VacationListResponseDto toVacationListResponseDto(
		Page<VacationRequest> page,
		List<VacationCreateResponseDto> content) {
		return VacationListResponseDto.builder()
			.content(content)
			.pageNumber(page.getNumber())
			.pageSize(page.getSize())
			.totalElements(page.getTotalElements())
			.totalPages(page.getTotalPages())
			.first(page.isFirst())
			.last(page.isLast())
			.build();
	}
}