package pl.master.thesis.features;

public class Feature {

	private String name;
	private double value;
	private double weight;

	public Feature(String name, double value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public double getValue() {
		return value;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "Feature: " + name + " value: " + value + " weight " + weight;
	}

}
