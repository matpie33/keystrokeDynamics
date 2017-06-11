package pl.master.thesis.csvManipulation;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.xml.sax.SAXException;

import pl.master.thesis.database.UsersTableData;
import pl.master.thesis.keyTypingObjects.Digraph;
import pl.master.thesis.keyTypingObjects.InterKeyTime;
import pl.master.thesis.keyTypingObjects.KeyHoldingTime;
import pl.master.thesis.keyTypingObjects.WordKeystrokeData;
import pl.master.thesis.myOwnClassification.StatisticsCalculator;
import pl.master.thesis.neuralNetworkClassification.NeuralFeature;
import pl.master.thesis.neuralNetworkClassification.NeuralNetworkInput;
import pl.master.thesis.swingWorkers.AddUserDataOnlyWorker;

public class CSVProcessing {

	private String fileNameToSave;
	private String dummyUserName = "Użytkownik";
	private String dummyPassword = "Haslo";
	private AddUserDataOnlyWorker addUserDataWorker;
	private int samplesPerUserToTake;

	public CSVProcessing(String fileNameToSave, AddUserDataOnlyWorker userDataWorker,
			int samplesPerUser) {
		this.fileNameToSave = fileNameToSave;
		addUserDataWorker = userDataWorker;
		samplesPerUserToTake = samplesPerUser;
	}

	public void extractStatisticsFromCSVAndSave()
			throws ParserConfigurationException, SAXException, IOException, SQLException {

		Reader in = new FileReader("C:\\master_thesis\\keystroke_dataset.csv");
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withSkipHeaderRecord().parse(in);
		List<Double> interTimesList = new ArrayList<>();
		List<Double> holdTimesList = new ArrayList<>();
		List<NeuralNetworkInput> trainingInputs = new ArrayList<>();
		List<WordKeystrokeData> allWordsDataForUser = new ArrayList<>();
		List<WordKeystrokeData> allWordsData = new ArrayList<>();
		int i = 0;
		int currentId = -1;
		int sameIdCounter = 1;

		for (CSVRecord record : records) {
			if (i == 0) {
				i++;
				continue;
			}
			int id = getUserIdAndIncrementCounter(record, currentId);
			if (id == currentId) {
				sameIdCounter++;
			}
			else {
				System.out.println("!!!!" + sameIdCounter);
				sameIdCounter = 1;
				System.out.println("here is: " + id);
				UsersTableData userData = createUserData(id);
				addUserDataWorker.addData(userData, allWordsDataForUser);
				allWordsDataForUser.clear();
			}
			currentId = id;
			if (sameIdCounter > samplesPerUserToTake) {
				continue;
			}
			addInterAndHoldTimesElementsToList(record, interTimesList, holdTimesList);
			List<KeyHoldingTime> holdTimes = convertToHoldTimeObjectsList(holdTimesList);
			List<InterKeyTime> interKeyTimes = convertToInterKeyTimesObjectsList(interTimesList);
			WordKeystrokeData wordKeystrokeData = new WordKeystrokeData(false, holdTimes,
					interKeyTimes);
			wordKeystrokeData.setUserId(id);
			allWordsDataForUser.add(wordKeystrokeData);
			allWordsData.add(wordKeystrokeData);

			NeuralNetworkInput input = CSVProcessing
					.convertHoldTimesAndInterKeyTimesListsToNeuralInput(holdTimesList,
							interTimesList);
			input.setUserId(id);
			trainingInputs.add(input);
			interTimesList.clear();
			holdTimesList.clear();
		}

		System.out.println("in csv processing: " + allWordsDataForUser.size());
		CSVSaver saver2 = new CSVSaver(fileNameToSave);
		saver2.saveTemporaryWordData(allWordsData);
		addUserDataWorker.doInBackground();
	}

	private UsersTableData createUserData(int userId) {
		String answer = "answer";
		String question = "question";
		return new UsersTableData().setAnswer(answer).setQuestion(question)
				.setPassword(dummyPassword).setUserName(dummyUserName + userId).setUserId(userId);
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

	private int getUserIdAndIncrementCounter(CSVRecord record, int currentId) {
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
