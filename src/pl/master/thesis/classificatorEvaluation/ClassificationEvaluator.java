package pl.master.thesis.classificatorEvaluation;

import java.util.ArrayList;
import java.util.List;

import org.nd4j.linalg.dataset.DataSet;

import pl.master.thesis.crossValidation.AcceptingStrategyBasedOnThreshold;
import pl.master.thesis.crossValidation.TrainingAndTestSet;
import pl.master.thesis.neuralNetworkClassification.NeuralNetworkHandler;

public class ClassificationEvaluator {

	private ImposterAndGenuineDataSetDivider dataSetDivider;
	private int numberOfUsers;
	private NeuralNetworkHandler neuralNetworkHandler;

	public ClassificationEvaluator(DataSet allData, int numberOfUsers, int samplesPerUser,
			int hiddenLayerNeurons, int lastUserIdPlusOne) {
		this.numberOfUsers = numberOfUsers;
		dataSetDivider = new ImposterAndGenuineDataSetDivider(allData, numberOfUsers,
				samplesPerUser);
		neuralNetworkHandler = new NeuralNetworkHandler(new AcceptingStrategyBasedOnThreshold(),
				lastUserIdPlusOne);
		neuralNetworkHandler.initiateNetwork(hiddenLayerNeurons);
	}

	public void evaluateClassificator() {
		List<DataSet> impostersSamples = new ArrayList<>();
		List<Boolean> allAuthenticUsersClassifications = new ArrayList<>();
		List<Boolean> allImpostersClassifications = new ArrayList<>();
		for (int i = 0; i < numberOfUsers; i++) {
			System.out.printf("user %d is authentic now", i);
			dataSetDivider.generateSets(i);
			DataSet imposters = dataSetDivider.getImposterSamples();
			impostersSamples.add(imposters);
			DataSet trainingSet = dataSetDivider.getTrainingSet();
			DataSet testSet = dataSetDivider.getTestSet();
			// trainingSetSamples.add(trainingSet);
			// testSetSamples.add(testSet);
			TrainingAndTestSet trainingAndTest = new TrainingAndTestSet(trainingSet, testSet);
			List<Boolean> classification = neuralNetworkHandler
					.learnAndEvaluateDataSet(trainingAndTest);
			allAuthenticUsersClassifications.addAll(classification);

			// DataSet imposters = DataSet.merge(impostersSamples);
			List<Boolean> classificationForImposters = neuralNetworkHandler
					.evaluateImposterSet(imposters, i);
			allImpostersClassifications.addAll(classificationForImposters);

		}

		double frrError = calculateClassificationError(allAuthenticUsersClassifications);
		System.out.println("FRR: " + frrError);

		double farError = calculateClassificationError(allImpostersClassifications);
		System.out.println("FAR: " + farError);
		// DataSet trainingSet = DataSet.merge(trainingSetSamples);
		// DataSet testSet = DataSet.merge(testSetSamples);

	}

	public static double calculateClassificationError(List<Boolean> classificationCorrectness) {
		int numberOfWrongClassifications = 0;
		for (boolean b : classificationCorrectness) {
			if (!b) {
				numberOfWrongClassifications++;
			}
		}
		return (double) numberOfWrongClassifications / (double) classificationCorrectness.size();
	}

}
