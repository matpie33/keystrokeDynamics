package pl.master.thesis.neuralNetworkClassification;

public class ModelParameters {

	private int numberOfClasses;
	private int samplesPerClass;
	private int lastClassIdPlusOne;

	public ModelParameters(int numClasses, int sampPerClass, int lastClassIdMinusOne) {
		numberOfClasses = numClasses;
		samplesPerClass = sampPerClass;
		this.lastClassIdPlusOne = lastClassIdMinusOne;
	}

	public int getLastClassIdPlusOne() {
		return lastClassIdPlusOne;
	}

	public int getNumberOfClasses() {
		return numberOfClasses;
	}

	public int getSamplesPerClass() {
		return samplesPerClass;
	}

}
