package pl.master.thesis.crossValidation;

import java.util.ArrayList;
import java.util.List;

import org.nd4j.linalg.api.ndarray.INDArray;

public class AcceptingStrategyBasedOnThreshold implements AcceptingStrategy {

	private double treshold = 0.98;

	public List<Boolean> isUserAccepted(INDArray testSet, INDArray neuralNetworkOutput) {

		if (testSet.rows() != neuralNetworkOutput.rows()) {
			throw new IllegalArgumentException("first set has: " + testSet.rows() + ""
					+ " and the second: " + neuralNetworkOutput.rows() + " examples ");
		}
		List<Boolean> rowsCorrectClassification = new ArrayList<>();
		for (int i = 0; i < neuralNetworkOutput.rows(); i++) {
			INDArray rowFromNeuralNetwork = neuralNetworkOutput.getRow(i);
			INDArray rowFromTestSet = testSet.getRow(i);
			int idOfUserAccepted = getAcceptedUser(rowFromNeuralNetwork);
			boolean isThisCorrectClassified = doesUserClassifiedByNetworkMatchTheTestSetData(
					idOfUserAccepted, rowFromTestSet);
			rowsCorrectClassification.add(isThisCorrectClassified);

		}
		return rowsCorrectClassification;

	}

	public List<Boolean> isUserDeniedAsOtherUser(INDArray neuralNetworkOutput, int otherUserId) {

		List<Boolean> rowsCorrectClassification = new ArrayList<>();
		for (int i = 0; i < neuralNetworkOutput.rows(); i++) {
			INDArray rowFromNeuralNetwork = neuralNetworkOutput.getRow(i);
			int idOfUserAccepted = getAcceptedUser(rowFromNeuralNetwork);
			boolean isUserAcceptedAsOtherUser = idOfUserAccepted != otherUserId;
			rowsCorrectClassification.add(isUserAcceptedAsOtherUser);
		}
		return rowsCorrectClassification;

	}

	private int getAcceptedUser(INDArray row) {
		List<Integer> acceptedUsers = new ArrayList<>();
		for (int i = 0; i < row.columns(); i++) {
			double value = row.getDouble(i);
			if (value != 0.0 && value > treshold) {
				acceptedUsers.add(i);
				if (acceptedUsers.size() > 1) {
					return -1;
				}
			}
		}
		return acceptedUsers.isEmpty() ? -1 : acceptedUsers.get(0);
	}

	private boolean doesUserClassifiedByNetworkMatchTheTestSetData(int usersClassified,
			INDArray testSetOutputs) {
		int realUserId = -1;
		for (int i = 0; i < testSetOutputs.columns(); i++) {
			if (testSetOutputs.getDouble(i) != 0.0) {
				realUserId = i;
				break;
			}
		}
		return usersClassified == realUserId;
	}

}
