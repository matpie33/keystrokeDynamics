package pl.master.thesis.main;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			throws ParserConfigurationException, SAXException, IOException, InterruptedException {
		DeepLearning4jUsing.use();
		DataSaver d = new DataSaver();
		d.readFromXml("usersData/abacad.xml");

		Reader in = new FileReader("C:\\master_thesis\\keystroke_dataset.csv");
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withSkipHeaderRecord().parse(in);
		List<Double> interKeyTimeValuesInRow = new ArrayList<>();
		List<Double> meanHoldTimeValuesInRow = new ArrayList<>();
		List<NeuralNetworkInput> testInputs = new ArrayList<>();
		List<NeuralNetworkInput> trainingInputs = new ArrayList<>();

		Map<List<String>, Integer> listListStrings = new HashMap<>();
		int i = 0;
		int currentId = 0;
		int sameIdCounter = 0;

		for (CSVRecord record : records) {
			List<String> row = new ArrayList<>();
			if (i == 0) {
				i++;
				continue;
			}

			int index = 3;
			while (index < record.size()) {
				row.add(record.get(index));
				index++;

			}
			List<String> strings = new ArrayList<String>();

			for (int j = 0; j < row.size(); j++) {
				strings.add(row.get(j));
			}

			// System.out.println(row);
			// strings.addAll(row);
			String subjectIdd = record.get(0);
			int idd = Integer.parseInt(subjectIdd.substring(2));
			if (idd == currentId) {
				sameIdCounter++;
			}
			else {
				sameIdCounter = 0;
			}
			if (sameIdCounter > 10) {
				continue;
			}
			row.clear();
			currentId = idd;
			listListStrings.put(strings, idd);

		}

		PlainTextSaver saver = new PlainTextSaver("testData.txt");
		saver.save(listListStrings);
		PlainTextSaver saver2 = new PlainTextSaver("trainingData.txt");
		saver2.save(listListStrings);
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
