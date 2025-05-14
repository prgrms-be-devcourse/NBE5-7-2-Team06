package programmers.team6.domain.vacation.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.member.entity.Code;
import programmers.team6.domain.member.entity.Dept;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.member.repository.CodeRepository;
import programmers.team6.domain.member.repository.MemberRepository;
import programmers.team6.domain.vacation.dto.VacationCreateRequestDto;
import programmers.team6.domain.vacation.dto.VacationCreateResponseDto;
import programmers.team6.domain.vacation.dto.VacationInfoSelectResponseDto;
import programmers.team6.domain.vacation.dto.VacationListResponseDto;
import programmers.team6.domain.vacation.dto.VacationUpdateRequestDto;
import programmers.team6.domain.vacation.dto.VacationUpdateResponseDto;
import programmers.team6.domain.vacation.entity.ApprovalStep;
import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.entity.VacationRequest;
import programmers.team6.domain.vacation.enums.VacationRequestStatus;
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
		VacationRequestStatus status = VacationRequestStatus.IN_PROGRESS;

		// 휴가 요청 생성
		VacationRequest vacationRequest = vacationMapper.toVacationRequest(requestDto, vacationType, status, requester);
		vacationRequestRepository.save(vacationRequest);

		// 결재 단계 생성
		ApprovalStep approvalStep = vacationMapper.toApprovalStep(approver, vacationRequest, 1,
			VacationRequestStatus.IN_PROGRESS);
		approvalStepRepository.save(approvalStep);

		// 응답 DTO 생성
		return vacationMapper.toVacationCreateResponseDto(
			vacationRequest,
			vacationType.getName(),
			approvalStep.getStatus(),
			approver.getName()
		);
	}

	// 휴가 신청 내역 (페이징: 20개씩)
	@Transactional
	public VacationListResponseDto getVacationRequestList(Long memberId, int page) {
		// 사용자 존재 여부 확인
		memberRepository.findById(memberId)
			.orElseThrow(() -> new RuntimeException("멤버 정보를 찾을 수 없습니다."));

		// 페이지 요청 객체 생성 (페이지 번호는 0부터 시작)
		Pageable pageable = PageRequest.of(page, 20);

		// 휴가 신청 내역 페이징 조회
		Page<VacationRequest> vacationRequestsPage = vacationRequestRepository.findByRequesterIdWithPaging(memberId,
			pageable);

		// 엔티티를 DTO로 변환
		List<VacationCreateResponseDto> content = vacationRequestsPage.getContent().stream()
			.map(request -> {
				// 결재 단계 정보 조회 (첫 번째 결재자 정보만 가져옴)
				ApprovalStep approvalStep = approvalStepRepository.findFirstByVacationRequestOrderByStepAsc(request)
					.orElseThrow(() -> new RuntimeException("결재 단계 정보를 찾을 수 없습니다."));

				// 휴가 유형 이름 조회
				String vacationTypeName = request.getType().getName();

				return vacationMapper.toVacationCreateResponseDto(
					request,
					vacationTypeName,
					request.getStatus(),
					approvalStep.getMember().getName()
				);
			})
			.collect(Collectors.toList());

		// 페이징 응답 DTO 생성
		return vacationMapper.toVacationListResponseDto(vacationRequestsPage, content);
	}

	// 휴가 신청 수정
	@Transactional
	public VacationUpdateResponseDto updateVacationRequest(Long memberId, Long requestId,
		VacationUpdateRequestDto requestDto) {
		// 요청자 확인
		Member requester = memberRepository.findById(memberId)
			.orElseThrow(() -> new RuntimeException("멤버 정보를 찾을 수 없습니다."));

		// 휴가 신청 조회
		VacationRequest vacationRequest = vacationRequestRepository.findById(requestId)
			.orElseThrow(() -> new RuntimeException("휴가 신청 정보를 찾을 수 없습니다."));

		// 수정 권한 확인 (본인이 신청한 휴가만 수정 가능)
		if (!vacationRequest.canUpdate(memberId)) {
			throw new RuntimeException("수정 권한이 없습니다.");
		}

		// 휴가 유형 코드 조회
		Code vacationType = codeRepository.findByGroupCodeAndCode("VACATION_TYPE", requestDto.getVacationType())
			.orElseThrow(() -> new RuntimeException("잘못된 휴가 유형입니다."));

		// 휴가 신청 수정
		vacationRequest.update(requestDto.getFrom(), requestDto.getTo(), requestDto.getReason(), vacationType);

		// 결재자 정보 조회
		ApprovalStep approvalStep = approvalStepRepository.findFirstByVacationRequestOrderByStepAsc(
				vacationRequest)
			.orElseThrow(() -> new RuntimeException("결재 단계 정보를 찾을 수 없습니다."));

		// 응답 DTO 생성
		return vacationMapper.toVacationUpdateResponseDto(
			vacationRequest,
			vacationType.getName(),
			approvalStep.getMember().getName()
		);
	}

	// 대기중인 휴가 신청 취소
	@Transactional
	public boolean cancelVacationRequest(Long memberId, Long requestId) {
		// 멤버 존재 여부 먼저 확인
		memberRepository.findById(memberId)
			.orElseThrow(() -> new RuntimeException("멤버 정보를 찾을 수 없습니다."));

		// 휴가 신청 조회
		VacationRequest vacationRequest = vacationRequestRepository.findById(requestId)
			.orElseThrow(() -> new RuntimeException("휴가 신청 정보를 찾을 수 없습니다."));

		// 휴가 신청 취소
		vacationRequest.cancel(memberId);

		return true;
	}

}