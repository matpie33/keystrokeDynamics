package pl.master.thesis.myOwnClassification;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import pl.master.thesis.keyTypingObjects.InterKeyTime;
import pl.master.thesis.keyTypingObjects.KeyHoldingTime;
import pl.master.thesis.keyTypingObjects.WordKeystrokeData;
import pl.master.thesis.neuralNetworkClassification.NeuralFeature;
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
			List<Double> interKeyTimes = getInterKeyTimesList(word);
			List<Double> holdTimes = getHoldTimesList(word);
			double meanHoldTime = getMean(holdTimes);
			double minHoldTime = getMinValue(holdTimes);
			double maxHoldTime = getMaxValue(holdTimes);
			double varianceHoldTime = getVariance(holdTimes, meanHoldTime);

			NeuralFeature holdTime = new NeuralFeature().maximum(maxHoldTime).minimum(minHoldTime)
					.mean(meanHoldTime).variance(varianceHoldTime);

			double meanInterKeyTime = getMean(interKeyTimes);
			double minInterKeyTime = getMinValue(interKeyTimes);
			double maxInterKeyTime = getMaxValue(interKeyTimes);
			double varianceInterKeyTime = getVariance(interKeyTimes, meanInterKeyTime);

			NeuralFeature interKeyTime = new NeuralFeature().maximum(maxInterKeyTime)
					.minimum(minInterKeyTime).mean(meanInterKeyTime).variance(varianceInterKeyTime);
			NeuralNetworkInput neuralInput = new NeuralNetworkInput(interKeyTime, holdTime,
					isTabbed);
			neuralInputs.add(neuralInput);
		}

		return neuralInputs;
	}

	private List<Double> getInterKeyTimesList(WordKeystrokeData word) {
		List<Double> listOfInterKeyTimes = new ArrayList<>();
		for (InterKeyTime interTime : word.getInterKeyTimes()) {
			listOfInterKeyTimes.add((double) interTime.getInterKeyTime() / 1_000_000_000D);
		}
		return listOfInterKeyTimes;
	}

	private List<Double> getHoldTimesList(WordKeystrokeData word) {
		List<Double> listOfHoldTimes = new ArrayList<>();
		for (KeyHoldingTime holdTime : word.getHoldTimes()) {
			listOfHoldTimes.add((double) holdTime.getHoldTime() / 1_000_000_000D);
		}
		return listOfHoldTimes;
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

	public static double getVariance(List<Double> list, double meanValue) {
		double temp = 0;
		for (double d : list) {
			temp += (d - meanValue) * (d - meanValue);
		}
		return temp / list.size();
	}

	public static double getMaxValue(List<Double> list) {
		double max = 0;
		for (double d : list) {
			if (d > max) {
				max = d;
			}
		}
		return max;
	}

	public static double getMinValue(List<Double> list) {
		double min = Double.MAX_VALUE;
		for (double d : list) {
			if (d < min) {
				min = d;
			}
		}
		return min;
	}

	public static int getIndexOfMinValue(List<Double> list) {
		int indexOfMin = 0;
		double min = Double.MAX_VALUE;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) < min) {
				indexOfMin = i;
			}
		}
		return indexOfMin;
	}

	public static double getMean(List<Double> list) {
		double mean = 0;
		for (double inter : list) {
			mean += inter;
		}
		mean /= list.size();
		return mean;
	}

}
