package pl.master.thesis.main;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.xml.sax.SAXException;

import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.neuralNetworkClassification.NeuralNetworkInput;
import pl.master.thesis.savers.DataSaver;
import pl.master.thesis.savers.PlainTextSaver;

public class KeystrokeDynamics {

	public static void main(String arg[])
			throws ParserConfigurationException, SAXException, IOException {
		DataSaver d = new DataSaver();
		d.readFromXml("usersData/abacad.xml");

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
			// if (sameIdCounter > 20) {
			// continue;
			// }
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

		PlainTextSaver saver = new PlainTextSaver("testData.txt");
		saver.save(testInputs);
		PlainTextSaver saver2 = new PlainTextSaver("trainingData.txt");
		saver2.save(trainingInputs);
		// RealMatrix mx = MatrixUtils.createRealMatrix(new double [][]{
		// {1,2,3,4,5},
		// {2,3,4,5,6},
		// {3,4,5,6,7}
		// });
		// RealMatrix cov = inverseSquareRootOfCovarianceMatrix(mx);
		//// testSquareMatrix(cov);
		// testInverseMatrix( MatrixUtils.createRealMatrix(new double [][]{
		// {1,4,5},
		// {2,12,4},
		// {3,22,5}
		// }));
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				MainWindow w = new MainWindow();
				// w.setResizable(false);
				w.setVisible(true);
			}
		});
		System.out.println("finished worin");

	}

}
