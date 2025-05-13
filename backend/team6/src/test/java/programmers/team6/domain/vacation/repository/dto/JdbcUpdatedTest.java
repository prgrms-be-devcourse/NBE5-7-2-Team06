package programmers.team6.domain.vacation.repository.dto;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JdbcUpdatedTest {

	@Test
	void 업데이트된_개수() {
		JdbcUpdated jdbcUpdated = new JdbcUpdated(new int[] {1, 0, 1});
		assertThat(jdbcUpdated.updatedCount()).isEqualTo(2);
	}

	@Test
	void 해당_아이템이_업데이트_확인() {
		JdbcUpdated jdbcUpdated = new JdbcUpdated(new int[] {1, 0, 1});

		assertThat(jdbcUpdated.isUpdated(0)).isTrue();
		assertThat(jdbcUpdated.isUpdated(1)).isFalse();
	}

	@Test
	void 해당_아이템이_업데이트_되지않았는지_확인() {
		JdbcUpdated jdbcUpdated = new JdbcUpdated(new int[] {1, 0, 1});

		assertThat(jdbcUpdated.isNonUpdated(1)).isTrue();
		assertThat(jdbcUpdated.isNonUpdated(0)).isFalse();
	}

	@Test
	void size() {
		int[] values = {1, 0, 1};
		JdbcUpdated jdbcUpdated = new JdbcUpdated(values);

		assertThat(jdbcUpdated.size()).isEqualTo(values.length);
	}

	@Test
	void 전체업데이트_확인() {
		JdbcUpdated jdbcUpdated = new JdbcUpdated(new int[] {1, 0, 1});

		assertThat(jdbcUpdated.isAllUpdated()).isFalse();

		JdbcUpdated jdbcUpdated2 = new JdbcUpdated(new int[] {1, 1, 1});
		assertThat(jdbcUpdated2.isAllUpdated()).isTrue();
	}
}