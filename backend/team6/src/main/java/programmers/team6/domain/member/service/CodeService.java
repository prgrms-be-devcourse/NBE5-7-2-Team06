package programmers.team6.domain.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.member.dto.CodeCreateRequest;
import programmers.team6.domain.member.dto.CodeReadResponse;
import programmers.team6.domain.member.entity.Code;
import programmers.team6.domain.member.enums.CodeExceptionMessage;
import programmers.team6.domain.member.exception.CodeException;
import programmers.team6.domain.member.repository.CodeRepository;
import programmers.team6.domain.member.util.mapper.CodeMapper;

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
		Code code = codeRepository.findById(id).orElseThrow(() -> new CodeException(CodeExceptionMessage.EMPTY_CODE));

		code.updateCode(codeCreateRequest.groupCode(), codeCreateRequest.code(), codeCreateRequest.name());
	}

	public void deleteCode(Long id) {
		codeRepository.deleteById(id);
	}

}
