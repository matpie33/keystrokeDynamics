package pl.master.thesis.main;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.BasicConfigurator;
import org.xml.sax.SAXException;

import pl.master.thesis.csvManipulation.CSVProcessing;
import pl.master.thesis.dataConverters.WordDataToSimpleObjectConverter;
import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.swingWorkers.AddUserDataOnlyWorker;
import pl.master.thesis.swingWorkers.AddUsersFromDatasetIfNotInDBWorker;
import pl.master.thesis.swingWorkers.GetKeystrokeDataFromDBWorker;

public class KeystrokeDynamics {

	public static void main(String arg[]) throws ParserConfigurationException, SAXException,
			IOException, InterruptedException, SQLException {

		BasicConfigurator.configure();

		// doTestLearning();
		// addUsersFromDatasetToDatabase();
		getUsersKeystrokeData();

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

	private static void getUsersKeystrokeData() throws SQLException {
		GetKeystrokeDataFromDBWorker dbWorker = new GetKeystrokeDataFromDBWorker("testData.txt");
		dbWorker.doInBackground();
	}

	private static void addUsersFromDatasetToDatabase() throws SQLException {
		AddUsersFromDatasetIfNotInDBWorker user = new AddUsersFromDatasetIfNotInDBWorker(56);
		user.doInBackground();
	}

	private static void doTestLearning() throws ParserConfigurationException, SAXException,
			IOException, InterruptedException, SQLException {
		String fileName = "trainingData.txt";
		if (new File(fileName).exists()) {
			new File(fileName).delete();
		}

		new CSVProcessing(fileName,
				new AddUserDataOnlyWorker(new WordDataToSimpleObjectConverter()))
						.extractStatisticsFromCSVAndSave();
		DeepLearning4jUsing.use();
	}

}
