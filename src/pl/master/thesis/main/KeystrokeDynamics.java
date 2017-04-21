package pl.master.thesis.main;

import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pl.master.thesis.csvManipulation.CSVProcessing;
import pl.master.thesis.frame.MainWindow;

public class KeystrokeDynamics {

	public static void main(String arg[])
			throws ParserConfigurationException, SAXException, IOException, InterruptedException {

		DeepLearning4jUsing.use();
		CSVProcessing.extractStatisticsFromCSVAndSave();

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
