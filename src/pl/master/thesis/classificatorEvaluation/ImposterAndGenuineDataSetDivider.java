package pl.master.thesis.classificatorEvaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.nd4j.linalg.dataset.DataSet;

import pl.master.thesis.crossValidation.TrainingAndTestSet;

public class ImposterAndGenuineDataSetDivider {

	private int trainingAndTestSetSize = 200;
	private DataSet trainingSet;
	private DataSet testSet;
	private int userId;
	private int numberOfUsers;
	private List<Integer> notTakenUserIds;
	private int totalSamplesPerUser;
	private int samplesFromUserAsImposter = 5;
	private DataSet wholeSet;
	private DataSet imposterSamples;

	public ImposterAndGenuineDataSetDivider(DataSet allData, int numberOfUsers,
			int samplesPerUser) {
		wholeSet = allData;
		this.numberOfUsers = numberOfUsers;
		this.totalSamplesPerUser = samplesPerUser;
		initializeNotTakenUserIds();
	}

	private void initializeNotTakenUserIds() {
		notTakenUserIds = new ArrayList<>();
		for (int i = 0; i < numberOfUsers; i++) {
			notTakenUserIds.add(i);
		}
	}

	public void generateSets(int userId) {
		this.userId = userId;
		notTakenUserIds.remove(new Integer(userId));
		generateTrainingAndTestSet();
		generateImposterSamples();

	}

	private void generateTrainingAndTestSet() {
		List<DataSet> trainingRows = new ArrayList<>();
		// System.out.println("tttttt");
		// System.out.println(wholeSet);
		for (int i = userId * totalSamplesPerUser; i < userId * totalSamplesPerUser
				+ trainingAndTestSetSize; i++) {
			trainingRows.add(wholeSet.get(i));
		}
		// System.out.printf("training set: %s", trainingRows);
		trainingSet = DataSet.merge(trainingRows);
		List<DataSet> testRows = new ArrayList<>();
		for (int i = userId * totalSamplesPerUser + trainingAndTestSetSize; i < (userId + 1)
				* totalSamplesPerUser; i++) {
			testRows.add(wholeSet.get(i));
		}
		System.out.println("training set size: " + trainingRows.size());
		System.out.println("test set size: " + testRows.size());
		testSet = DataSet.merge(testRows);
		// System.out.printf("test set: %s", testRows);
		TrainingAndTestSet t = new TrainingAndTestSet(trainingSet, testSet);
		// System.out.println("###");
		// System.out.println(trainingSet);
		// System.out.println("&&&");
		// System.out.println(testSet);
	}

	private void generateImposterSamples() {
		List<DataSet> imposterSamples = new ArrayList<>();
		for (int userId : notTakenUserIds) {
			System.out.println("Imposter sample from user: " + userId);
			List<Integer> specificUserSamplesIds = new ArrayList<>();
			int rangeStart = userId * totalSamplesPerUser;
			for (int i = 0; i < totalSamplesPerUser; i++) {
				specificUserSamplesIds.add(rangeStart + i);
			}
			for (int i = 0; i < samplesFromUserAsImposter; i++) {
				Random r = new Random();
				int randomIndex = r.nextInt(specificUserSamplesIds.size());
				int sampleIndex = specificUserSamplesIds.get(randomIndex);
				specificUserSamplesIds.remove(randomIndex);
				DataSet imposterSample = wholeSet.get(sampleIndex);
				// System.out.println(imposterSample);
				imposterSamples.add(imposterSample);
				// System.out.println("getting sample: " + imposterSample);
			}

		}

		this.imposterSamples = DataSet.merge(imposterSamples);
		notTakenUserIds.add(userId);

	}

	public DataSet getTrainingSet() {
		return trainingSet;
	}

	public DataSet getTestSet() {
		return testSet;
	}

	public DataSet getImposterSamples() {
		return imposterSamples;
	}

}
