package pl.master.thesis.crossValidation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nd4j.linalg.dataset.DataSet;

import pl.master.thesis.myOwnClassification.StatisticsCalculator;

public class KFoldsValidationManager {

	private Map<Integer, Double> numberOfNeuronsToClassificationErrorMapping;
	private final int minimalNeurons = 10;
	private final int maximalNeurons = 100;
	private final int differenceBetweenNumberOfNeurons = 10;
	private FoldsCreator foldsCreator;

	public KFoldsValidationManager(DataSet dataSet, AcceptingStrategy acceptStrategy) {
		numberOfNeuronsToClassificationErrorMapping = new HashMap<>();
		foldsCreator = new FoldsCreator();
		foldsCreator.createFolds(dataSet);
		initiateMap();
		calculateErrorForEachNeuronsStructure(acceptStrategy);
	}

	private void initiateMap() {
		int newNumberOfNeurons = minimalNeurons;
		while (newNumberOfNeurons <= maximalNeurons) {
			numberOfNeuronsToClassificationErrorMapping.put(newNumberOfNeurons, 0.0D);
			newNumberOfNeurons += differenceBetweenNumberOfNeurons;
		}
	}

	private void calculateErrorForEachNeuronsStructure(AcceptingStrategy acceptingStrategy) {
		for (int numberOfNeurons : numberOfNeuronsToClassificationErrorMapping.keySet()) {
			System.out.println("::::::::::::::::::::::::::::::::::::::::::::::");
			System.out.println("Checking for: " + numberOfNeurons + " number of neurons");
			List<Double> classificationErrors = foldsCreator.nextRound(numberOfNeurons,
					acceptingStrategy);
			double meanError = StatisticsCalculator.getMean(classificationErrors);
			numberOfNeuronsToClassificationErrorMapping.put(numberOfNeurons, meanError);
		}
		int indexOfMinValue = getBestStructure();
	}

	private int getBestStructure() {
		double minimum = Double.MAX_VALUE;
		int numberOfNeurons = 0;
		for (Map.Entry<Integer, Double> entry : numberOfNeuronsToClassificationErrorMapping
				.entrySet()) {
			if (entry.getValue() < minimum) {
				minimum = entry.getValue();
				numberOfNeurons = entry.getKey();
			}
		}
		return numberOfNeurons;
	}

}
