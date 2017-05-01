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
import pl.master.thesis.myOwnClassification.StatisticsCalculator;
import pl.master.thesis.neuralNetworkClassification.NeuralFeature;
import pl.master.thesis.neuralNetworkClassification.NeuralNetworkInput;

public class CSVProcessing {

	private String fileNameToSave;
	private String dummyUserName = "Użytkownik";
	private String dummyPassword = "Haslo";

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
		List<WordKeystrokeData> allWordsDataForUser = new ArrayList<>();
		int i = 0;
		int currentId = 0;
		int sameIdCounter = 0;

		for (CSVRecord record : records) {
			if (i == 0) {
				i++;
				continue;
			}
			int id = getUserIdAndIncrementCounter(record, currentId, sameIdCounter);
			if (id == currentId) {
				sameIdCounter++;
			}
			else if (sameIdCounter != 0) {
				sameIdCounter = 0;

			}
			currentId = id;
			if (sameIdCounter > 20) {
				continue;
			}
			addInterAndHoldTimesElementsToList(record, interTimesList, holdTimesList);
			List<KeyHoldingTime> holdTimes = convertToHoldTimeObjectsList(holdTimesList);
			List<InterKeyTime> interKeyTimes = convertToInterKeyTimesObjectsList(interTimesList);
			WordKeystrokeData wordKeystrokeData = new WordKeystrokeData(false, holdTimes,
					interKeyTimes);
			allWordsDataForUser.add(wordKeystrokeData);

			NeuralNetworkInput input = CSVProcessing
					.convertHoldTimesAndInterKeyTimesListsToNeuralInput(holdTimesList,
							interTimesList);
			input.setUserId(id);
			trainingInputs.add(input);

			interTimesList.clear();
			holdTimesList.clear();
		}

		CSVSaver saver2 = new CSVSaver(fileNameToSave);
		saver2.save(trainingInputs);
	}

	public static NeuralNetworkInput convertHoldTimesAndInterKeyTimesListsToNeuralInput(
			List<Double> holdTimesList, List<Double> interTimesList) {
		double meanHoldTime = StatisticsCalculator.getMean(holdTimesList);
		double meanInterTime = StatisticsCalculator.getMean(interTimesList);

		double minHoldTime = StatisticsCalculator.getMinValue(holdTimesList);
		double minInterTime = StatisticsCalculator.getMinValue(interTimesList);

		double maxHoldTime = StatisticsCalculator.getMaxValue(holdTimesList);
		double maxInterTime = StatisticsCalculator.getMaxValue(interTimesList);

		double varianceHoldTime = StatisticsCalculator.getVariance(holdTimesList, meanHoldTime);
		double varianceInterTime = StatisticsCalculator.getVariance(interTimesList, meanInterTime);

		NeuralFeature holdTime = new NeuralFeature().minimum(minHoldTime).maximum(maxHoldTime)
				.mean(meanHoldTime).variance(varianceHoldTime);

		NeuralFeature interKeyTime = new NeuralFeature().minimum(minInterTime).maximum(maxInterTime)
				.mean(meanInterTime).variance(varianceInterTime);

		NeuralNetworkInput input = new NeuralNetworkInput(interKeyTime, holdTime, false);
		return input;

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

	private int getUserIdAndIncrementCounter(CSVRecord record, int currentId, int sameIdCounter) {
		String subjectId = record.get(0);
		int id = Integer.parseInt(subjectId.substring(2)) - 2;
		return id;
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
