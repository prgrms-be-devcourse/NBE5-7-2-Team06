package programmers.team6.domain.member.entity;

import java.io.Serializable;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Code {
	@Id
	@EmbeddedId
	private CodeId codeId;
	private String groupName;
	private String codeName;

	private static class CodeId implements Serializable {
		private Integer groupId;
		private Integer codeId;
	}
}
