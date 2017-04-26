package pl.master.thesis.neuralNetworkClassification;

public class NeuralNetworkOutput {

	private double probability;
	private int id;

	public NeuralNetworkOutput(double probability, int id) {
		this.probability = probability;
		this.id = id;
	}

	public double getProbability() {
		return probability;
	}

	public int getId() {
		return id;
	}

}
