package pl.master.thesis.keyTypingObjects;

public class KeyHoldingTime {

	private String key;
	private long holdTime;
	private long pressedTime;

	public KeyHoldingTime(String key, long time, long pressedTime) {
		this.key = key;
		this.holdTime = time;
		this.pressedTime = pressedTime;
	}

	public long getHoldTime() {
		return holdTime;
	}

	public String getKey() {
		return key;
	}

	public long getPressedTime() {
		return pressedTime;
	}

	@Override
	public String toString() {
		return "Key: " + key + " hold time: " + holdTime;
	}
}