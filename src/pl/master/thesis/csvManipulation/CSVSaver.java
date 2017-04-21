package pl.master.thesis.csvManipulation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import pl.master.thesis.neuralNetworkClassification.NeuralNetworkInput;

public class CSVSaver {

	private String fileName;

	public CSVSaver() {
		fileName = "data.txt";
	}

	public CSVSaver(String filename) {
		this.fileName = filename;
	}

	public void save(List<NeuralNetworkInput> data) {
		try {
			FileWriter fw = new FileWriter(fileName, true);
			for (NeuralNetworkInput singleData : data) {
				fw.write(String.format(Locale.US, " %f, %f, %d", singleData.getMeanHoldTime(),
						singleData.getMeanInterTime(), singleData.getUserId()));
				fw.write(System.getProperty("line.separator"));
			}
			fw.close();
		}
		catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}

}
