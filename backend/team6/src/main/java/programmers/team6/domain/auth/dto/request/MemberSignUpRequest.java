package programmers.team6.domain.auth.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MemberSignUpRequest(

	@NotBlank(message = "이름은 필수입니다.")
	String name,

	@Email(message = "이메일 형식이 아닙니다.")
	@NotBlank(message = "이메일은 필수입니다.")
	String email,

	@NotNull(message = "부서 선택은 필수입니다.")
	Long dept,

	@NotBlank(message = "직위 선택은 필수입니다.")
	String position,

	@NotNull(message = "입사 날짜는 필수입니다.")
	LocalDateTime joinDate,

	@NotBlank(message = "생년월일 선택은 필수입니다.")
	String birth,

	@NotBlank(message = "비밀번호 입력은 필수입니다.")
	@Pattern(
		regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$",
		message = "비밀번호는 영문자, 숫자, 특수문자를 포함해 8자 이상이어야 합니다."
	)
	String password

) {
}
