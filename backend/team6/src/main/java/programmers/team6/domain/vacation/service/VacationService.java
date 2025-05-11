package programmers.team6.domain.vacation.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import programmers.team6.domain.member.entity.Code;
import programmers.team6.domain.member.entity.Dept;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.member.repository.CodeRepository;
import programmers.team6.domain.member.repository.MemberRepository;
import programmers.team6.domain.vacation.dto.VacationCreateRequestDto;
import programmers.team6.domain.vacation.dto.VacationCreateResponseDto;
import programmers.team6.domain.vacation.dto.VacationInfoSelectResponseDto;
import programmers.team6.domain.vacation.entity.ApprovalStep;
import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.entity.VacationRequest;
import programmers.team6.domain.vacation.enums.ApprovalStatus;
import programmers.team6.domain.vacation.repository.ApprovalStepRepository;
import programmers.team6.domain.vacation.repository.VacationRepository;
import programmers.team6.domain.vacation.repository.VacationRequestRepository;
import programmers.team6.domain.vacation.util.mapper.VacationMapper;

@Service
@RequiredArgsConstructor
public class VacationService {

	private final VacationRepository vacationRepository;
	private final VacationMapper vacationMapper;

	private final VacationRequestRepository vacationRequestRepository;
	private final ApprovalStepRepository approvalStepRepository;

	private final MemberRepository memberRepository;
	private final CodeRepository codeRepository;

	// 본인 연차 조회
	@Transactional
	public VacationInfoSelectResponseDto getMyVacationInfo(Long memberId) {
		VacationInfo vacationInfo = vacationRepository.findByMemberId(memberId)
			.orElseThrow(() -> new RuntimeException("휴가 정보를 찾을 수 없습니다."));

		return vacationMapper.toVacationInfoSelectResponseDto(vacationInfo);
	}

	// 휴가 신청
	@Transactional
	public VacationCreateResponseDto requestVacation(Long memberId, VacationCreateRequestDto requestDto) {
		// 신청자 정보 조회
		Member requester = memberRepository.findById(memberId)
			.orElseThrow(() -> new RuntimeException("멤버 정보를 찾을 수 없습니다."));

		// 부서장 조회 (결재자)
		Dept dept = requester.getDept();
		Member approver = dept.getDeptLeader();

		// 휴가 유형 코드 조회
		Code vacationType = codeRepository.findByGroupCodeAndCode("VACATION_TYPE", requestDto.getVacationType())
			.orElseThrow(() -> new RuntimeException("잘못된 휴가 유형입니다."));

		// 휴가 요청 상태 코드 (기본 대기 상태)
		ApprovalStatus status = ApprovalStatus.PENDING;

		// 휴가 요청 생성
		VacationRequest vacationRequest = vacationMapper.toVacationRequest(requestDto, vacationType, status, requester);
		vacationRequestRepository.save(vacationRequest);

		// 결재 단계 생성
		ApprovalStep approvalStep = vacationMapper.toApprovalStep(approver, vacationRequest, 1, ApprovalStatus.PENDING);
		approvalStepRepository.save(approvalStep);

		// 응답 DTO 생성
		return vacationMapper.toVacationCreateResponseDto(
			vacationRequest,
			vacationType.getName(),
			approvalStep.getStatus(),
			approver.getName()
		);
	}
}