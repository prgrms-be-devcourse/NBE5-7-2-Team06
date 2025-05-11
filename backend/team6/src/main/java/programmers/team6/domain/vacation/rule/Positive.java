package programmers.team6.domain.vacation.rule;

public final class Positive {

	public static final int MIN_VALUE = 0;

	private final int value;

	public Positive(int value) {
		this.value = requirePositive(value);
	}

	private static int requirePositive(int value) {
		if (value < MIN_VALUE) {
			throw new IllegalArgumentException("해당 값을 음수가 될 수 없습니다.");
		}
		return value;
	}

	public int toInt() {
		return value;
	}
}
