package programmers.team6.domain.vacation.repository.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class UpdatedResultsTest {

	@Test
	void 전체가_다_업데이트가_아닌지_검증() {
		UpdatedResults<Integer> updatedResults = new UpdatedResults<>(new JdbcUpdated(new int[] {1, 0, 1}),
			List.of(1, 2, 3));

		assertThat(updatedResults.isHasAnyNonUpdated()).isTrue();

		UpdatedResults<Integer> updatedResults2 = new UpdatedResults<>(new JdbcUpdated(new int[] {1, 1, 1}),
			List.of(1, 2, 3));
		assertThat(updatedResults2.isHasAnyNonUpdated()).isFalse();
	}

	@Test
	void 업데이트된_내역_조회() {
		UpdatedResults<Integer> updatedResults = new UpdatedResults<>(new JdbcUpdated(new int[] {1, 0, 1}),
			List.of(1, 2, 3));

		List<Integer> updateded = updatedResults.updatedItems();

		assertThat(updateded).containsAll(List.of(1, 3));
	}

	@Test
	void 업데이트되지_않은_내역조회() {
		UpdatedResults<Integer> updatedResults = new UpdatedResults<>(new JdbcUpdated(new int[] {1, 0, 1}),
			List.of(1, 2, 3));

		List<Integer> nonUpdatedItems = updatedResults.nonUpdatedItems();

		assertThat(nonUpdatedItems).containsAll(List.of(2));
	}
}