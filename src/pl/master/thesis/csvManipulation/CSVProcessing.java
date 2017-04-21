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

import pl.master.thesis.neuralNetworkClassification.NeuralNetworkInput;

public class CSVProcessing {

	public static void extractStatisticsFromCSVAndSave()
			throws ParserConfigurationException, SAXException, IOException {

		Reader in = new FileReader("C:\\master_thesis\\keystroke_dataset.csv");
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withSkipHeaderRecord().parse(in);
		List<Double> interKeyTimeValuesInRow = new ArrayList<>();
		List<Double> meanHoldTimeValuesInRow = new ArrayList<>();
		List<NeuralNetworkInput> testInputs = new ArrayList<>();
		List<NeuralNetworkInput> trainingInputs = new ArrayList<>();
		int i = 0;
		int currentId = 0;
		int sameIdCounter = 0;

		for (CSVRecord record : records) {
			if (i == 0) {
				i++;
				continue;
			}
			int columnUDIndex = 5;
			int columnHIndex = 3;
			while (columnUDIndex < record.size()) {
				String cellValue = record.get(columnUDIndex);
				double cellDouble = Double.parseDouble(cellValue);
				if (cellDouble > 0) {
					interKeyTimeValuesInRow.add(cellDouble);
				}
				else {
					double cellDDValue = Double.parseDouble(record.get(columnUDIndex - 1));
					interKeyTimeValuesInRow.add(cellDDValue);
				}
				columnUDIndex += 3;

				double holdTime = Double.parseDouble(record.get(columnHIndex));
				meanHoldTimeValuesInRow.add(holdTime);
				columnHIndex += 3;
			}
			double holdTime = Double.parseDouble(record.get(columnHIndex));
			meanHoldTimeValuesInRow.add(holdTime);
			double meanHoldTime = 0;
			for (double hold : meanHoldTimeValuesInRow) {
				meanHoldTime += hold;
			}
			meanHoldTime /= meanHoldTimeValuesInRow.size();

			double meanInterTime = 0;
			for (double inter : interKeyTimeValuesInRow) {
				meanInterTime += inter;
			}
			meanInterTime /= interKeyTimeValuesInRow.size();
			String subjectId = record.get(0);
			int id = Integer.parseInt(subjectId.substring(2));
			if (id == currentId) {
				sameIdCounter++;
			}
			else {
				sameIdCounter = 0;
			}
			currentId = id;
			System.out.println("id: " + id);
			System.out.println("subjectId: " + subjectId + " hold times: ");

			System.out.println(meanHoldTimeValuesInRow);
			NeuralNetworkInput input = new NeuralNetworkInput(meanInterTime, meanHoldTime, false);
			input.setUserId(id);
			if (sameIdCounter < 5) {
				testInputs.add(input);
			}
			else {
				trainingInputs.add(input);
			}

			interKeyTimeValuesInRow.clear();
			meanHoldTimeValuesInRow.clear();
		}

		CSVSaver saver = new CSVSaver("testData.txt");
		saver.save(testInputs);
		CSVSaver saver2 = new CSVSaver("trainingData.txt");
		saver2.save(trainingInputs);
	}
}
