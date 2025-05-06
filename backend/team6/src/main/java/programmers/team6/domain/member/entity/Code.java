package programmers.team6.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources.Chain.Strategy;

@Entity
@Table(
	name = "code",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"group_code", "code"})
	}
)
public class Code {

	@Id
	@Column(name = "code_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "group_code", nullable = false)
	private String groupCode;

	@Column(nullable = false)
	private String code;

	@Column(nullable = false)
	private String name;

}
