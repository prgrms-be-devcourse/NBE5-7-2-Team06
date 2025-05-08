package programmers.team6.domain.member.util.mapper;

import programmers.team6.domain.member.dto.MemberCreateRequest;
import programmers.team6.domain.member.entity.Code;
import programmers.team6.domain.member.entity.Dept;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.member.entity.MemberInfo;
import programmers.team6.domain.member.enums.Role;

public class MemberMapper {

	public static Member MemberCreateRequestToEntity(MemberCreateRequest memberCreateRequest, Dept dept,
		Code position, String encodedPassword) {

		MemberInfo memberInfo = MemberInfo.builder()
			.birth(memberCreateRequest.getBirth())
			.email(memberCreateRequest.getEmail())
			.password(encodedPassword)
			.build();

		Member member = Member.builder()
			.name(memberCreateRequest.getName())
			.dept(dept)
			.position(position)
			.joinDate(memberCreateRequest.getJoinDate())
			.role(Role.USER)
			.build();

		member.setMemberInfo(memberInfo);

		return member;
	}

}
