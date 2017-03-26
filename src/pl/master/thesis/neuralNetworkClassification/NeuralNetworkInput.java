package pl.master.thesis.neuralNetworkClassification;

public class NeuralNetworkInput {

	private double meanInterTime;
	private double meanHoldTime;
	private double isTabbed;
	private int userId;

	public double getMeanInterTime() {
		return meanInterTime;
	}

	public double getMeanHoldTime() {
		return meanHoldTime;
	}

	public double getIsTabbed() {
		return isTabbed;
	}

	public int getUserId() {
		return userId;
	}

	public NeuralNetworkInput(double interTime, double holdTime, boolean moreThan50PercentTabs) {
		meanInterTime = interTime;
		meanHoldTime = holdTime;
		this.isTabbed = translateToInt(moreThan50PercentTabs);
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
		return "interTime: " + meanInterTime + " hold time: " + meanHoldTime + " isTabbed "
				+ isTabbed;
	}

}
