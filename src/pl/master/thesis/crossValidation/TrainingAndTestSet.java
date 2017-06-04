package pl.master.thesis.crossValidation;

import org.nd4j.linalg.dataset.DataSet;

public class TrainingAndTestSet {

	private DataSet testSet;
	private DataSet trainingSet;

	public TrainingAndTestSet(DataSet trainingSet, DataSet testSet) {
		this.testSet = testSet;
		this.trainingSet = trainingSet;
	}

	public DataSet getTestSet() {
		return testSet;
	}

	public DataSet getTrainingSet() {
		return trainingSet;
	}

	@Override
	public String toString() {
		return "training set is: " + trainingSet + "\nAnd the test set: " + testSet;
	}

}
