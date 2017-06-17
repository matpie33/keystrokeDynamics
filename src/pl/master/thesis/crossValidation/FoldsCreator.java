package pl.master.thesis.crossValidation;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.nd4j.linalg.dataset.DataSet;

import pl.master.thesis.classificatorEvaluation.ClassificationEvaluator;
import pl.master.thesis.neuralNetworkClassification.ModelParameters;
import pl.master.thesis.neuralNetworkClassification.NeuralNetworkHandler;

public class FoldsCreator {

	private int numberOfFolds = 3; // known as k
	private List<Fold> folds;
	private List<Integer> indexesOfFolds;
	private int numberOfUsers;
	private int lastUserIdPlusOne;
	private int samplesPerUser;

	public FoldsCreator(ModelParameters parameters, int numberOfFolds) {
		numberOfUsers = parameters.getNumberOfClasses();
		samplesPerUser = parameters.getSamplesPerClass();
		this.numberOfFolds = numberOfFolds;
		lastUserIdPlusOne = parameters.getLastClassIdPlusOne();
	}

	public void createFolds(DataSet allData) {
		if (folds != null) {
			return;
		}

		folds = new ArrayList<>();

		int numberOfExamples = allData.numExamples();
		System.out.println("number of examples " + numberOfExamples);
		List<Integer> indexesOfExamples = generateListOfIndexes(numberOfExamples);
		List<Fold> folds = new ArrayList<>();
		int freeSamplesPerUser = samplesPerUser;
		for (int i = 0; i < numberOfFolds; i++) {
			System.out.println("in fold no. " + i);
			int userSamplesPerFold = samplesPerUser / numberOfFolds;
			Fold fold = new Fold();
			for (int j = 0; j < numberOfUsers; j++) {
				System.out.println("Taking samples from user: " + j);

				int freeSamplesPerPreviousUser = freeSamplesPerUser;
				freeSamplesPerUser = samplesPerUser - userSamplesPerFold * i;
				System.out
						.println("Previous user has: " + freeSamplesPerPreviousUser + " samples.");
				System.out.println("Next users have: " + freeSamplesPerUser + " samples.");
				for (int k = 0; k < userSamplesPerFold; k++) {
					System.out.println("Taking " + k + "-th sample from user.");
					Random r = new Random();
					int rangeStart = j * freeSamplesPerPreviousUser;
					int indexFromIndexesList = rangeStart + r.nextInt(freeSamplesPerUser);
					System.out.println("range start: " + rangeStart + "; range end: "
							+ (rangeStart + freeSamplesPerUser));
					System.out.println("examples left to take: " + indexesOfExamples.size());

					int indexFromExamplesList = indexesOfExamples.get(indexFromIndexesList);
					indexesOfExamples.remove(indexFromIndexesList);
					DataSet randomRow = allData.get(indexFromExamplesList);
					fold.addRowFromDataSet(randomRow);
					freeSamplesPerUser--;
				}

			}
			System.out.println();
			fold.mergeRowsAndGet();
			folds.add(fold);
		}
		System.out.println("after, the size is: " + indexesOfExamples.size());
		List<Integer> foldsIndexes = generateListOfIndexes(numberOfFolds);
		while (!indexesOfExamples.isEmpty()) {
			Random r = new Random();
			int index = r.nextInt(foldsIndexes.size());
			int indexOfFoldGettingMoreData = foldsIndexes.get(index);
			foldsIndexes.remove(index);
			System.out.println("fold number: " + indexOfFoldGettingMoreData
					+ " gets one more row: index: " + indexesOfExamples.get(0));
			folds.get(indexOfFoldGettingMoreData)
					.addRowFromDataSet(allData.get(indexesOfExamples.get(0)));
			indexesOfExamples.remove(0);
		}
		this.folds = folds;
		saveToFile();
		System.out.println("created folds: " + folds);
	}

	private void saveToFile() {
		PrintWriter pw;
		try {
			pw = new PrintWriter("output.txt");
			pw.println(folds);
			pw.close();
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private List<Integer> generateListOfIndexes(int numberOfIndexes) {
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < numberOfIndexes; i++) {
			list.add(i);
		}
		return list;
	}

	public List<Double> nextRound(int hiddenLayerNeurons, AcceptingStrategy acceptingStrategy) {
		NeuralNetworkHandler neuralNetworkHandler = new NeuralNetworkHandler(acceptingStrategy,
				lastUserIdPlusOne);
		neuralNetworkHandler.initiateNetwork(hiddenLayerNeurons);
		indexesOfFolds = generateListOfIndexes(numberOfFolds);
		System.out.println("Next round .........................................");
		List<Double> classificationErrors = new ArrayList<>();
		for (int i = 0; i < numberOfFolds; i++) {
			System.out.println();
			System.out.println("Fold number: " + (i + 1));
			TrainingAndTestSet nextSets = getNextRandomTrainingAndTestSets();
			// System.out.println(nextSets);
			List<Boolean> rowsCorrectClassification = neuralNetworkHandler.learnAndEvaluateDataSet(nextSets);
			double error = ClassificationEvaluator
					.calculateClassificationError(rowsCorrectClassification);
			System.out.println("the error: " + error);
			classificationErrors.add(error);
		}
		return classificationErrors;
	}

	private TrainingAndTestSet getNextRandomTrainingAndTestSets() {
		Random r = new Random();
		int i = r.nextInt(indexesOfFolds.size());
		int indexOfRandomlyChosenTestSet = indexesOfFolds.get(i);
		indexesOfFolds.remove(i);
		Fold randomFold = folds.get(indexOfRandomlyChosenTestSet);
		DataSet testSet = randomFold.mergeRowsAndGet();
		List<DataSet> otherDataSets = new ArrayList<>();

		for (int j = 0; j < folds.size(); j++) {
			if (j == indexOfRandomlyChosenTestSet) {
				continue;
			}
			Fold fold = folds.get(j);
			otherDataSets.add(fold.mergeRowsAndGet());
		}
		DataSet trainingSet = DataSet.merge(otherDataSets);
		TrainingAndTestSet t = new TrainingAndTestSet(trainingSet, testSet);
		System.out.println("******************************");
		System.out.println("Next: ");
		// System.out.println(t);

		return t;

	}

}
