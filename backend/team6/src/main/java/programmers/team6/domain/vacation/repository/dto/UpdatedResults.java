package programmers.team6.domain.vacation.repository.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class UpdatedResults<T> {

	private final JdbcUpdated updated;
	private final List<T> values;

	public UpdatedResults(JdbcUpdated updated, List<T> values) {
		this.updated = Objects.requireNonNull(updated);
		this.values = new ArrayList<>(values);
	}

	public boolean isAllUpdated() {
		return updated.isAllUpdated();
	}

	public boolean isHasAnyNonUpdated() {
		return !updated.isAllUpdated();
	}

	public List<T> updatedItems() {
		List<T> updatedItems = new ArrayList<>();
		for (int i = 0; i < updated.size(); i++) {
			if (updated.isUpdated(i)) {
				updatedItems.add(values.get(i));
			}
		}
		return updatedItems;
	}

	public List<T> nonUpdatedItems() {
		List<T> updatedItems = new ArrayList<>();
		for (int i = 0; i < updated.size(); i++) {
			if (updated.isNonUpdated(i)) {
				updatedItems.add(values.get(i));
			}
		}
		return updatedItems;
	}

}
