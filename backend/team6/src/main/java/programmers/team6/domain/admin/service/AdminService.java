package programmers.team6.domain.admin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.admin.dto.AdminVacationRequestSearchCustom;
import programmers.team6.domain.admin.dto.AdminVacationSearchCondition;
import programmers.team6.domain.admin.dto.ApprovalStepDetailUpdateResponse;
import programmers.team6.domain.admin.dto.VacationRequestDetailReadResponse;
import programmers.team6.domain.admin.dto.VacationRequestDetailUpdateRequest;
import programmers.team6.domain.admin.dto.VacationRequestSearchResponse;
import programmers.team6.domain.member.entity.Code;
import programmers.team6.domain.member.enums.CodeExceptionMessage;
import programmers.team6.domain.member.exception.CodeException;
import programmers.team6.domain.member.repository.CodeRepository;
import programmers.team6.domain.vacation.entity.ApprovalStep;
import programmers.team6.domain.vacation.entity.VacationRequest;
import programmers.team6.domain.vacation.enums.VacationExceptionMessage;
import programmers.team6.domain.vacation.exception.VacationException;
import programmers.team6.domain.vacation.repository.ApprovalStepRepository;
import programmers.team6.domain.vacation.repository.VacationRequestRepository;

@Service
@RequiredArgsConstructor
public class AdminService {
	private final AdminVacationRequestSearchCustom adminVacationRequestSearchCustom;
	private final VacationRequestRepository vacationRequestRepository;
	private final CodeRepository codeRepository;
	private final ApprovalStepRepository approvalStepRepository;

	@Transactional(readOnly = true)
	public Page<VacationRequestSearchResponse> search(Pageable pageable, AdminVacationSearchCondition searchCondition) {
		return adminVacationRequestSearchCustom.search(searchCondition, pageable);
	}

	@Transactional(readOnly = true)
	public VacationRequestDetailReadResponse selectVacationRequestDetailById(Long id) {
		List<ApprovalStepDetailUpdateResponse> approvalStepDetailUpdateResponses = approvalStepRepository.findApprovalStepDetailById(
			id);
		if (approvalStepDetailUpdateResponses.isEmpty()) {
			throw new VacationException(VacationExceptionMessage.EMPTY_APPROVAL_STEP);
		}
		return vacationRequestRepository.findVacationRequestDetailById(id)
			.orElseThrow(() -> new VacationException(VacationExceptionMessage.EMPTY_VACATION_REQUEST_DETAIL))
			.injectApprovalStepDetails(approvalStepDetailUpdateResponses);
	}

	@Transactional
	public void updateVacationRequestDetailById(Long id,
		VacationRequestDetailUpdateRequest vacationRequestDetailUpdateRequest) {
		VacationRequest vacationRequest = vacationRequestRepository.findVacationRequestById(id)
			.orElseThrow(() -> new VacationException(VacationExceptionMessage.EMPTY_VACATION_REQUEST_DETAIL));

		Code vacationRequestType = codeRepository.findByIdAndGroupCode(vacationRequestDetailUpdateRequest.typeId(),
			"VACATION_TYPE").orElseThrow(() -> new CodeException(CodeExceptionMessage.EMPTY_CODE));
		vacationRequest.update(vacationRequestType, vacationRequestDetailUpdateRequest.from(),
			vacationRequestDetailUpdateRequest.to(), vacationRequestDetailUpdateRequest.vacationRequestStatus(),
			vacationRequestDetailUpdateRequest.reason());

		List<ApprovalStep> approvalSteps = approvalStepRepository.findApprovalStepsByVacationRequest_IdOrderByStepAsc(
			id);
		if (approvalSteps.isEmpty()
			|| vacationRequestDetailUpdateRequest.approvalReason().size() != approvalSteps.size()) {
			throw new VacationException(VacationExceptionMessage.INVALID_APPROVAL_STEP);
		}
		for (int i = 0; i < approvalSteps.size(); i++) {
			approvalSteps.get(i).update(vacationRequestDetailUpdateRequest.approvalReason().get(i));
		}
	}
}
