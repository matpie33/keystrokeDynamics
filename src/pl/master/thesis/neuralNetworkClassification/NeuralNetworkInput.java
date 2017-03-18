package pl.master.thesis.neuralNetworkClassification;

public class NeuralNetworkInput {
	
	private double meanInterTime;
	private double meanHoldTime;
	private boolean moreThan50PercentTabs;
	private int userId;
	
	public double getMeanInterTime() {
		return meanInterTime;
	}
	public double getMeanHoldTime() {
		return meanHoldTime;
	}
	public boolean isMoreThan50PercentTabs() {
		return moreThan50PercentTabs;
	}
	
	public int getUserId(){
		return userId;
	}
	
	public NeuralNetworkInput (double interTime, double holdTime, boolean moreThan50PercentTabs){
		meanInterTime = interTime;
		meanHoldTime = holdTime;
		this.moreThan50PercentTabs = moreThan50PercentTabs;
	}
	

}
