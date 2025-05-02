package programmers.team6.domain.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import programmers.team6.global.entity.BaseEntity;

@Entity
public class MemberInfo extends BaseEntity {

	@Id
	private Long memberId;

	@OneToOne
	@MapsId
	@JoinColumn(name = "member_id")
	private Member member;

	private int birth;

	private String email;

	private String password;

}
