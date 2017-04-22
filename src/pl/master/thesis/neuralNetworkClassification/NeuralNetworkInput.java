package pl.master.thesis.neuralNetworkClassification;

public class NeuralNetworkInput {

	private NeuralFeature interKeyTime;
	private NeuralFeature holdTime;

	private double isTabbed;
	private int userId;

	public NeuralNetworkInput(NeuralFeature interTime, NeuralFeature holdTime,
			boolean moreThan50PercentTabs) {
		interKeyTime = interTime;
		this.holdTime = holdTime;
		this.isTabbed = translateToInt(moreThan50PercentTabs);
	}

	public NeuralFeature getInterKeyTime() {
		return interKeyTime;
	}

	public NeuralFeature getHoldTime() {
		return holdTime;
	}

	public double getIsTabbed() {
		return isTabbed;
	}

	public int getUserId() {
		return userId;
	}

	private double translateToInt(boolean booleanValue) {
		if (booleanValue) {
			return 1;
		}
		else
			return 0;
	}

	public void setUserId(int id) {
		userId = id;
	}

	public String toString() {
		return "interTime: " + interKeyTime.getMean() + " hold time: " + holdTime.getMean()
				+ " isTabbed " + isTabbed;
	}

}
