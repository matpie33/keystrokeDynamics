package pl.master.thesis.neuralNetworkClassification;

public class NeuralFeature {
	private double minValue;
	private double maxValue;
	private double variance;
	private double mean;

	public double getMinValue() {
		return minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public double getVariance() {
		return variance;
	}

	public double getMean() {
		return mean;
	}

	public NeuralFeature minimum(double min) {
		minValue = min;
		return this;
	}

	public NeuralFeature maximum(double max) {
		maxValue = max;
		return this;
	}

	public NeuralFeature variance(double variance) {
		this.variance = variance;
		return this;
	}

	public NeuralFeature mean(double mean) {
		this.mean = mean;
		return this;
	}

}
