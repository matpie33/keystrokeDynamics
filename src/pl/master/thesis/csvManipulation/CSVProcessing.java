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

import pl.master.thesis.keyTypingObjects.Digraph;
import pl.master.thesis.keyTypingObjects.InterKeyTime;
import pl.master.thesis.keyTypingObjects.KeyHoldingTime;
import pl.master.thesis.keyTypingObjects.WordKeystrokeData;
import pl.master.thesis.neuralNetworkClassification.NeuralFeature;
import pl.master.thesis.neuralNetworkClassification.NeuralNetworkInput;

public class CSVProcessing {

	private String fileNameToSave;

	public CSVProcessing(String fileNameToSave) {
		this.fileNameToSave = fileNameToSave;
	}

	public void extractStatisticsFromCSVAndSave()
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
			List<KeyHoldingTime> holdTimes = convertToHoldTimeObjectsList(holdTimesList);
			List<InterKeyTime> interKeyTimes = convertToInterKeyTimesObjectsList(interTimesList);
			WordKeystrokeData wordKeystrokeData = new WordKeystrokeData(false, holdTimes,
					interKeyTimes);
			double meanHoldTime = countMean(holdTimesList);
			double meanInterTime = countMean(interTimesList);

			double minHoldTime = getMinValue(holdTimesList);
			double minInterTime = getMinValue(interTimesList);

			double maxHoldTime = getMaxValue(holdTimesList);
			double maxInterTime = getMaxValue(interTimesList);

			double varianceHoldTime = getVariance(holdTimesList, meanHoldTime);
			double varianceInterTime = getVariance(interTimesList, meanInterTime);

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

		CSVSaver saver2 = new CSVSaver(fileNameToSave);
		saver2.save(trainingInputs);
	}

	private void addInterAndHoldTimesElementsToList(CSVRecord record, List<Double> interTimesList,
			List<Double> holdTimesList) {
		int columnUDIndex = 5;
		int columnHIndex = 3;
		while (columnUDIndex < record.size()) {
			String cellValue = record.get(columnUDIndex);
			double cellDouble = Double.parseDouble(cellValue);
			InterKeyTime interKeyTime;
			long secondInNano = 1_000_000_000;
			if (cellDouble > 0) {
				interTimesList.add(cellDouble);
				interKeyTime = new InterKeyTime(new Digraph("", ""),
						(long) (cellDouble * secondInNano));
			}
			else {
				double cellDDValue = Double.parseDouble(record.get(columnUDIndex - 1));
				interTimesList.add(cellDDValue);
				interKeyTime = new InterKeyTime(new Digraph("", ""),
						(long) (cellDDValue * secondInNano));
			}
			columnUDIndex += 3;

			double holdTime = Double.parseDouble(record.get(columnHIndex));
			holdTimesList.add(holdTime);
			KeyHoldingTime holdTimeObject = new KeyHoldingTime("", (long) (holdTime * secondInNano),
					0);
			columnHIndex += 3;
		}
		double holdTime = Double.parseDouble(record.get(columnHIndex));
		holdTimesList.add(holdTime);
	}

	private double countMean(List<Double> list) {
		double mean = 0;
		for (double inter : list) {
			mean += inter;
		}
		mean /= list.size();
		return mean;
	}

	private int getUserIdAndIncrementCounter(CSVRecord record, int currentId, int sameIdCounter) {
		String subjectId = record.get(0);
		int id = Integer.parseInt(subjectId.substring(2)) - 1;
		if (id == currentId) {
			sameIdCounter++;
		}
		else {
			sameIdCounter = 0;
		}
		currentId = id;
		return id;
	}

	private double getMinValue(List<Double> list) {
		double min = Double.MAX_VALUE;
		for (double d : list) {
			if (d < min) {
				min = d;
			}
		}
		return min;
	}

	private double getMaxValue(List<Double> list) {
		double max = 0;
		for (double d : list) {
			if (d > max) {
				max = d;
			}
		}
		return max;
	}

	private double getVariance(List<Double> list, double meanValue) {
		double temp = 0;
		for (double d : list) {
			temp += (d - meanValue) * (d - meanValue);
		}
		return temp / list.size();
	}

	private List<KeyHoldingTime> convertToHoldTimeObjectsList(List<Double> holdTimeValues) {
		List<KeyHoldingTime> keyHoldingTimes = new ArrayList<>();
		for (Double d : holdTimeValues) {
			KeyHoldingTime time = new KeyHoldingTime("", (long) (d * 1_000_000_000), 0L);
			keyHoldingTimes.add(time);
		}
		return keyHoldingTimes;
	}

	private List<InterKeyTime> convertToInterKeyTimesObjectsList(List<Double> interKeyTimeValues) {
		List<InterKeyTime> interKeyTimes = new ArrayList<>();
		for (Double d : interKeyTimeValues) {
			InterKeyTime time = new InterKeyTime(new Digraph("", ""), (long) (d * 1_000_000_000));
			interKeyTimes.add(time);
		}
		return interKeyTimes;
	}

}
