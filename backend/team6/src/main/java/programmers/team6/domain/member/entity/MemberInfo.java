package programmers.team6.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import programmers.team6.global.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfo extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(mappedBy = "memberInfo")
	private Member member;

	@Column(nullable = false)
	private String birth;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	public void setMember(Member member) {
		this.member = member;
		if (member.getMemberInfo() != this) {
			member.setMemberInfo(this);
		}
	}

	@Builder
	public MemberInfo(String birth, String email, String password) {
		this.birth = birth;
		this.email = email;
		this.password = password;
	}
}
