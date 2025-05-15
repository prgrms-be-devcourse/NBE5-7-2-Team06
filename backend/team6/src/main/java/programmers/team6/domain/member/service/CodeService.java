package programmers.team6.domain.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.member.dto.CodeCreateRequest;
import programmers.team6.domain.member.dto.CodeReadResponse;
import programmers.team6.domain.member.entity.Code;
import programmers.team6.domain.member.enums.BasicCodeInfo;
import programmers.team6.domain.member.repository.CodeRepository;
import programmers.team6.domain.member.util.mapper.CodeMapper;
import programmers.team6.global.exception.code.NotFoundErrorCode;
import programmers.team6.global.exception.customException.NotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class CodeService {
	private final CodeRepository codeRepository;

	public void createCode(CodeCreateRequest codeCreateRequest) {
		codeRepository.save(CodeMapper.toCode(codeCreateRequest));
	}

	@Transactional(readOnly = true)
	public Page<CodeReadResponse> readCodePage(Pageable pageable) {
		return codeRepository.findCodePage(pageable);
	}

	public void updateCode(Long id, CodeCreateRequest codeCreateRequest) {
		Code code = codeRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(NotFoundErrorCode.NOT_FOUND_CODE));

		code.updateCode(codeCreateRequest.groupCode(), codeCreateRequest.code(), codeCreateRequest.name());
	}

	/**
	 * 삭제하려는 code가 필수 CODE인 경우 삭제하지 못하도록 수정
	 * @param id
	 */
	public void deleteCode(Long id) {
		Code deletedTarget = codeRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(NotFoundErrorCode.NOT_FOUND_CODE));
		if (BasicCodeInfo.isIn(deletedTarget.getGroupCode(), deletedTarget.getCode())) {
			return;
		}
		codeRepository.delete(deletedTarget);
	}

}
