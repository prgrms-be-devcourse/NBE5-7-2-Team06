package programmers.team6.domain.member.util.mapper;

import lombok.experimental.UtilityClass;
import programmers.team6.domain.member.dto.CodeCreateRequest;
import programmers.team6.domain.member.entity.Code;

@UtilityClass
public class CodeMapper {
	public static Code toCode(CodeCreateRequest codeCreateRequest) {
		return Code.builder()
			.groupCode(codeCreateRequest.groupCode())
			.code(codeCreateRequest.code())
			.name(codeCreateRequest.name())
			.build();
	}
}
