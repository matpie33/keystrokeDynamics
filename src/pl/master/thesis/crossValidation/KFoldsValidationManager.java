package pl.master.thesis.crossValidation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.nd4j.linalg.dataset.DataSet;

import pl.master.thesis.myOwnClassification.StatisticsCalculator;
import pl.master.thesis.neuralNetworkClassification.ModelParameters;

public class KFoldsValidationManager {

	private Map<Integer, Double> numberOfNeuronsToClassificationErrorMapping;
	private final int minimalNeurons = 10;
	private final int maximalNeurons = 200;
	private final int differenceBetweenNumberOfNeurons = 10;
	private FoldsCreator foldsCreator;
	private FileWriter fw;

	public KFoldsValidationManager(DataSet dataSet, AcceptingStrategy acceptStrategy,
			ModelParameters params, int numberOfFolds) {
		numberOfNeuronsToClassificationErrorMapping = new LinkedHashMap<>();
		foldsCreator = new FoldsCreator(params, numberOfFolds);
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
			saveInfo("Number of neurons in hidden layer: " + numberOfNeurons);
			System.out.println("Checking for: " + numberOfNeurons + " number of neurons");
			List<Double> classificationErrors = foldsCreator.nextRound(numberOfNeurons,
					acceptingStrategy);
			double meanError = StatisticsCalculator.getMean(classificationErrors);
			saveInfo("Classification errors: ");
			saveInfo(convertDoublesTo3DecimalPlaces(classificationErrors).toString());
			saveInfo("Mean error: " + String.format("%.3f", meanError));
			saveInfo("");
			numberOfNeuronsToClassificationErrorMapping.put(numberOfNeurons, meanError);
		}
		int indexOfMinValue = getBestStructure();
		try {
			fw.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<String> convertDoublesTo3DecimalPlaces(List<Double> doubles) {
		List<String> strings = new ArrayList<>();
		for (double d : doubles) {
			strings.add(String.format("%.3f", d));
		}
		return strings;
	}

	public void saveInfo(String line) {
		try {
			fw = new FileWriter("Badania.txt", true);
			fw.write(line);
			fw.write(System.getProperty("line.separator"));
			fw.close();
		}
		catch (IOException ioex) {
			ioex.printStackTrace();
		}
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
