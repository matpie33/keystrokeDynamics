package pl.master.thesis.myOwnClassification;

import java.util.List;

import pl.master.thesis.csvManipulation.CSVSaver;
import pl.master.thesis.dataConverters.WordDataToSimpleObjectConverter;
import pl.master.thesis.keyTypingObjects.PreprocessedKeystrokeData;
import pl.master.thesis.keyTypingObjects.WordKeystrokeData;
import pl.master.thesis.neuralNetworkClassification.NeuralNetworkClassifier;
import pl.master.thesis.neuralNetworkClassification.NeuralNetworkInput;

public class ClassificationManager {

	private DataDivider dataDivider;
	private double percentOfConsecutiveKeysHold;
	private StatisticsCalculator statisticsCalculator;
	private NeuralNetworkClassifier classifier;
	private WordDataToSimpleObjectConverter converter;
	private CSVSaver plainTextSaver;

	public ClassificationManager() {
		plainTextSaver = new CSVSaver();
		percentOfConsecutiveKeysHold = 0;
		dataDivider = new DataDivider();
		statisticsCalculator = new StatisticsCalculator();
		classifier = new NeuralNetworkClassifier();
		converter = new WordDataToSimpleObjectConverter();
	}

	public void addKeystrokeData(WordKeystrokeData newData) {
		dataDivider.addWordKeystrokeData(newData);
	}

	public void compareData() {
		if (dataDivider.noData()) {
			return;
		}
		divideDataToSets();
		getAndCompareDataSets();
	}

	public void divideDataToSets() {
		dataDivider.divideDataToTestAndTrainingSet();
	}

	private void getAndCompareDataSets() {
		KeystrokeDataArray testDataArray = convertListOfKeystrokeDataToArrays(
				dataDivider.getTestData());
		KeystrokeDataArray trainingDataArray = convertListOfKeystrokeDataToArrays(
				dataDivider.getTrainingData());
		// classifier.saveInputData(testDataArray, trainingDataArray);
		// classifier.compare();
	}

	private KeystrokeDataArray convertListOfKeystrokeDataToArrays(List<WordKeystrokeData> data) {
		KeystrokeDataArray dataArray = new KeystrokeDataArray();
		dataArray.convertWordKeystrokeDataToArrays(data);
		return dataArray;
	}

	public List<NeuralNetworkInput> learnData(int userId) {
		System.out.println("learning");
		List<NeuralNetworkInput> inputs = statisticsCalculator
				.transferToNeuralInput(dataDivider.getWholeData());
		dataDivider.cleanData();
		classifyInputsAsUser(inputs, userId);
		classifier.learn(inputs);
		System.out.println("done learning");
		return inputs;
	}

	private void classifyInputsAsUser(List<NeuralNetworkInput> inputs, int userId) {
		for (NeuralNetworkInput input : inputs) {
			input.setUserId(userId);
		}
	}

	public List<NeuralNetworkInput> classifyUser() {
		List<NeuralNetworkInput> neuralInputs = statisticsCalculator
				.transferToNeuralInput(dataDivider.getWholeData());
		System.out.println("neural inputs: " + neuralInputs);
		for (NeuralNetworkInput neuralInput : neuralInputs) {
			classifier.classify(neuralInput);
		}
		dataDivider.cleanData();
		return neuralInputs;
	}

	public void cleanData() {
		dataDivider.cleanData();
	}

	public List<WordKeystrokeData> getTypingData() {
		return dataDivider.getWholeData();
	}

	public List<PreprocessedKeystrokeData> convertDataForDbNeeds(WordKeystrokeData wordsData) {
		return converter.convertSingleWordToSimplestData(wordsData);
	}

	public void saveData(List<NeuralNetworkInput> data) {
		// plainTextSaver.save(data);
	}

}
