package programmers.team6.domain.member.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberCreateRequest {

	@NotBlank(message = "이름은 필수입니다.")
	private String name;

	@NotBlank(message = "email은 필수입니다.")
	private String email;

	@NotBlank(message = "부서 선택은 필수입니다.")
	private Long dept;

	@NotBlank(message = "직위 선택은 필수입니다.")
	private String position;

	@NotBlank(message = "입사 날짜는 필수입니다.")
	private LocalDateTime joinDate;

	@NotBlank(message = "생년월일 선택은 필수입니다.")
	private String birth;

	@NotBlank(message = "비밀번호 입력은 필수입니다.")
	private String password;
}
