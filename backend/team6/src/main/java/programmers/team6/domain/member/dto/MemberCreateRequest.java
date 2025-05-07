package programmers.team6.domain.member.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberCreateRequest {

	@NotBlank
	private String name;

	@NotBlank
	private String email;

	@NotBlank
	private Long dept;

	@NotBlank
	private String position;

	@NotBlank
	private LocalDateTime joinDate;

	@NotBlank
	private String birth;

	@NotBlank
	private String password;
}
