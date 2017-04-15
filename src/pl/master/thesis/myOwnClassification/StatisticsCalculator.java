package pl.master.thesis.myOwnClassification;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import pl.master.thesis.keyTypingObjects.InterKeyTime;
import pl.master.thesis.keyTypingObjects.KeyHoldingTime;
import pl.master.thesis.keyTypingObjects.WordKeystrokeData;
import pl.master.thesis.neuralNetworkClassification.NeuralNetworkInput;

public class StatisticsCalculator {

	private final double MEDIAN_PERCENTILE = 50.0;

	public List<NeuralNetworkInput> transferToNeuralInput(
			List<WordKeystrokeData> wordKeystrokeDatas) {

		List<NeuralNetworkInput> neuralInputs = new ArrayList<>();
		for (WordKeystrokeData word : wordKeystrokeDatas) {
			if (word.getInterKeyTimes().isEmpty()) {
				System.out.println("inters size: " + word.getWord());
			}
			boolean isTabbed = word.isStartedWithTab();
			double meanInterKeyTime = sumInterKeyTimesInWord(word);
			int interKeyTimeAmount = word.getInterKeyTimes().size();
			double meanHoldTime = sumHoldTimesInWord(word);
			int holdTimeAmount = word.getHoldTimes().size();
			meanInterKeyTime = meanInterKeyTime / (double) interKeyTimeAmount;
			meanHoldTime = meanHoldTime / (double) holdTimeAmount;
			meanInterKeyTime /= 1_000_000_000;
			meanHoldTime /= 1_000_000_000;
			System.out.println(meanHoldTime);
			System.out.println(meanInterKeyTime);
			NeuralNetworkInput neuralInput = new NeuralNetworkInput(meanInterKeyTime, meanHoldTime,
					isTabbed);
			neuralInputs.add(neuralInput);
		}

		return neuralInputs;
	}

	private long sumInterKeyTimesInWord(WordKeystrokeData word) {
		long sum = 0;
		for (InterKeyTime interTime : word.getInterKeyTimes()) {
			sum += interTime.getInterKeyTime();
		}
		return sum;
	}

	private long sumHoldTimesInWord(WordKeystrokeData word) {
		long sum = 0;
		for (KeyHoldingTime holdTime : word.getHoldTimes()) {
			sum += holdTime.getHoldTime();
		}
		return sum;
	}

	public double[] removeOutliers(double[] arrayHoldTimes) {
		DescriptiveStatistics d = new DescriptiveStatistics(arrayHoldTimes);
		double medianInterKeyTime = d.getPercentile(MEDIAN_PERCENTILE);
		double standDeviationInterKeyTime = d.getStandardDeviation();
		return removeOutliersForGivenMedianAndStdDev(arrayHoldTimes, standDeviationInterKeyTime,
				medianInterKeyTime);
	}

	private double[] removeOutliersForGivenMedianAndStdDev(double[] array, double stdev,
			double median) {
		List<Double> arrayList = convertArrayOfDoublesToList(array);
		double treshold = 1.5;
		for (int i = 0; i < arrayList.size(); i++) {
			Double interTime = arrayList.get(i);
			if ((interTime > median + treshold * stdev)
					|| (interTime < median - treshold * stdev)) {
				arrayList.remove(interTime);
			}
		}

		return convertListOfDoubleToArray(arrayList);
	}

	private List<Double> convertArrayOfDoublesToList(double[] array) {
		List<Double> list = new ArrayList<>();
		for (double d : array) {
			list.add(d);
		}
		return list;
	}

	private double[] convertListOfDoubleToArray(List<Double> list) {
		double[] array = new double[list.size()];
		int i = 0;
		for (Double d : list) {
			array[i] = d;
			i++;
		}
		return array;
	}

}
