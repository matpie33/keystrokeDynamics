package pl.master.thesis.csvManipulation;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.xml.sax.SAXException;

import pl.master.thesis.neuralNetworkClassification.NeuralFeature;
import pl.master.thesis.neuralNetworkClassification.NeuralNetworkInput;

public class CSVProcessing {

	public static void extractStatisticsFromCSVAndSave()
			throws ParserConfigurationException, SAXException, IOException {

		Reader in = new FileReader("C:\\master_thesis\\keystroke_dataset.csv");
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withSkipHeaderRecord().parse(in);
		List<Double> interTimesList = new ArrayList<>();
		List<Double> holdTimesList = new ArrayList<>();
		List<NeuralNetworkInput> trainingInputs = new ArrayList<>();
		int i = 0;
		int currentId = 0;
		int sameIdCounter = 0;

		for (CSVRecord record : records) {
			if (i == 0) {
				i++;
				continue;
			}

			addInterAndHoldTimesElementsToList(record, interTimesList, holdTimesList);
			double meanHoldTime = countMean(holdTimesList);
			double meanInterTime = countMean(interTimesList);
			System.out.println("hold times list: " + holdTimesList);
			System.out.println("mean hold time: " + meanHoldTime);

			double minHoldTime = getMinValue(holdTimesList);
			double minInterTime = getMinValue(interTimesList);

			System.out.println("min hold time: " + minHoldTime);
			double maxHoldTime = getMaxValue(holdTimesList);
			double maxInterTime = getMaxValue(interTimesList);

			System.out.println("max hold time: " + maxHoldTime);
			double varianceHoldTime = getVariance(holdTimesList, meanHoldTime);
			double varianceInterTime = getVariance(interTimesList, meanInterTime);

			System.out.println("varianceHoldTime: " + varianceHoldTime);
			NeuralFeature holdTime = new NeuralFeature().minimum(minHoldTime).maximum(maxHoldTime)
					.mean(meanHoldTime).variance(varianceHoldTime);

			NeuralFeature interKeyTime = new NeuralFeature().minimum(minInterTime)
					.maximum(maxInterTime).mean(meanInterTime).variance(varianceInterTime);

			int id = getUserIdAndIncrementCounter(record, currentId, sameIdCounter);
			NeuralNetworkInput input = new NeuralNetworkInput(interKeyTime, holdTime, false);
			input.setUserId(id);
			trainingInputs.add(input);

			interTimesList.clear();
			holdTimesList.clear();
		}

		CSVSaver saver2 = new CSVSaver("trainingData.txt");
		saver2.save(trainingInputs);
	}

	private static void addInterAndHoldTimesElementsToList(CSVRecord record,
			List<Double> interTimesList, List<Double> holdTimesList) {
		int columnUDIndex = 5;
		int columnHIndex = 3;
		while (columnUDIndex < record.size()) {
			String cellValue = record.get(columnUDIndex);
			double cellDouble = Double.parseDouble(cellValue);
			if (cellDouble > 0) {
				interTimesList.add(cellDouble);
			}
			else {
				double cellDDValue = Double.parseDouble(record.get(columnUDIndex - 1));
				interTimesList.add(cellDDValue);
			}
			columnUDIndex += 3;

			double holdTime = Double.parseDouble(record.get(columnHIndex));
			holdTimesList.add(holdTime);
			columnHIndex += 3;
		}
		double holdTime = Double.parseDouble(record.get(columnHIndex));
		holdTimesList.add(holdTime);
	}

	private static double countMean(List<Double> list) {
		double mean = 0;
		for (double inter : list) {
			mean += inter;
		}
		mean /= list.size();
		return mean;
	}

	private static int getUserIdAndIncrementCounter(CSVRecord record, int currentId,
			int sameIdCounter) {
		String subjectId = record.get(0);
		int id = Integer.parseInt(subjectId.substring(2)) - 2;
		if (id == currentId) {
			sameIdCounter++;
		}
		else {
			sameIdCounter = 0;
		}
		currentId = id;
		return id;
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

	public static double getMaxValue(List<Double> list) {
		double max = 0;
		for (double d : list) {
			if (d > max) {
				max = d;
			}
		}
		return max;
	}

	public static double getVariance(List<Double> list, double meanValue) {
		double temp = 0;
		for (double d : list) {
			temp += (d - meanValue) * (d - meanValue);
		}
		return temp / list.size();
	}

}
