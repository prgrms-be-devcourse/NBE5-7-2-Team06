package programmers.team6.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import programmers.team6.global.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfo extends BaseEntity {

	@Id
	@MapsId
	@OneToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(nullable = false)
	private int birth;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

}
