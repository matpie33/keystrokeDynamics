package pl.master.thesis.crossValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.nd4j.linalg.dataset.DataSet;

import pl.master.thesis.neuralNetworkClassification.NeuralNetworkHandler;

public class FoldsCreator {

	private int numberOfFolds = 3; // known as k
	private List<Fold> folds;
	private List<Integer> indexesOfFolds;

	// TODO manager class: has the list of possible hidden neurons, for each of
	// them it asks folds creator
	// to do next round and return the mean error, the manager holds map of:
	// neuron number -> error
	// then the manager chooses the least error

	public void createFolds(DataSet allData) {
		if (folds != null) {
			return;
		}

		folds = new ArrayList<>();

		int numberOfExamples = allData.numExamples();
		int foldSize = numberOfExamples / numberOfFolds;
		List<Integer> indexesOfExamples = generateListOfIndexes(numberOfExamples);
		List<Fold> folds = new ArrayList<>();
		for (int j = 0; j < numberOfFolds; j++) {
			Fold fold = new Fold();
			for (int n = 0; n < foldSize; n++) {
				Random r = new Random();
				int indexFromIndexesList = r.nextInt(indexesOfExamples.size());
				int indexFromExamplesList = indexesOfExamples.get(indexFromIndexesList);
				System.out.println("getting row number: " + indexFromExamplesList);
				indexesOfExamples.remove(indexesOfExamples.indexOf(indexFromExamplesList));
				System.out.println("indexes size after remove: " + indexesOfExamples.size());
				// indexes.remove(new Integer(index));
				DataSet randomRow = allData.get(indexFromExamplesList);
				fold.addRowFromDataSet(randomRow);
			}
			fold.mergeRowsAndGet();
			folds.add(fold);
			System.out.println("created fold: " + fold);
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
		System.out.println("folds done: " + folds);
		this.folds = folds;
	}

	private List<Integer> generateListOfIndexes(int numberOfIndexes) {
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < numberOfIndexes; i++) {
			list.add(i);
		}
		return list;
	}

	public List<Double> nextRound(int hiddenLayerNeurons, AcceptingStrategy acceptingStrategy) {
		NeuralNetworkHandler neuralNetworkHandler = new NeuralNetworkHandler(acceptingStrategy);
		neuralNetworkHandler.initiateNetwork(hiddenLayerNeurons);
		indexesOfFolds = generateListOfIndexes(numberOfFolds);
		System.out.println("Next round .........................................");
		List<Double> classificationErrors = new ArrayList<>();
		for (int i = 0; i < numberOfFolds; i++) {
			System.out.println();
			System.out.println("Fold number: " + (i + 1));
			TrainingAndTestSet nextSets = getNextRandomTrainingAndTestSets();
			System.out.println(nextSets);
			List<Boolean> rowsCorrectClassification = neuralNetworkHandler.learnDataSet(nextSets);
			classificationErrors.add(calculateClassificationError(rowsCorrectClassification));
		}
		return classificationErrors;
	}

	private double calculateClassificationError(List<Boolean> correctMatchesList) {
		int numberOfCorrectMatches = 0;
		for (boolean b : correctMatchesList) {
			if (b) {
				numberOfCorrectMatches++;
			}
		}
		return (double) numberOfCorrectMatches / (double) correctMatchesList.size();
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
		System.out.println(t);

		return t;

	}

}
