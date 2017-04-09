package pl.master.thesis.keyTypingObjects;

import java.util.Objects;

public class Digraph {

	// Two consecutively typed keys

	private String firstKey;
	private String secondKey;

	public Digraph(String beforeKey, String afterKey) {
		firstKey = beforeKey;
		secondKey = afterKey;
	}

	public String getFirstKey() {
		return firstKey;
	}

	public String getSecondKey() {
		return secondKey;
	}

	@Override
	public String toString() {
		return "Digraph from: " + firstKey + " to " + secondKey;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Digraph)) {
			return false;
		}
		Digraph d = (Digraph) o;
		return Objects.equals(d.getFirstKey(), firstKey)
				&& Objects.equals(d.getSecondKey(), secondKey);

	}

	@Override
	public int hashCode() {
		return Objects.hash(firstKey, secondKey);
	}

	public boolean isSameKey() {
		return firstKey.equals(secondKey);
	}

}
