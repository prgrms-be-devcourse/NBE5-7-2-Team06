package programmers.team6.domain.vacation.repository.dto;

import java.util.Arrays;
import java.util.stream.IntStream;

public final class JdbcUpdated {
	private static final int SUCCESS = 1;

	private final int[] updated;

	public JdbcUpdated(int[] updated) {
		this.updated = Arrays.copyOf(updated, updated.length);
	}

	public int updatedCount() {
		return (int)IntStream.range(0, updated.length).filter(this::isUpdated).count();
	}

	public boolean isUpdated(int index) {
		return updated[index] == SUCCESS;
	}

	public boolean isNonUpdated(int index) {
		return updated[index] != SUCCESS;
	}

	public int size() {
		return updated.length;
	}

	public boolean isAllUpdated() {
		return updatedCount() == updated.length;
	}
}
