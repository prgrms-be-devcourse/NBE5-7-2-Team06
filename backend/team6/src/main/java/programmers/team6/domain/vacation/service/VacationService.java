package programmers.team6.domain.vacation.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.dto.VacationInfoMapper;
import programmers.team6.domain.vacation.dto.VacationInfoSelectResponseDto;
import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.repository.VacationRepository;

@Service
@RequiredArgsConstructor
public class VacationService {

	private final VacationRepository vacationRepository;
	private final VacationInfoMapper vacationInfoMapper;

	// 본인 연차 조회
	public VacationInfoSelectResponseDto getMyVacationInfo(Long memberId) {
		VacationInfo vacationInfo = vacationRepository.findByMemberId(memberId)
			.orElseThrow(() -> new RuntimeException("휴가 정보를 찾을 수 없습니다."));

		return vacationInfoMapper.toVacationInfoSelectResponseDto(vacationInfo);
	}
}
