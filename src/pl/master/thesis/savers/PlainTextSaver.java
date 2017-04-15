package pl.master.thesis.savers;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import pl.master.thesis.neuralNetworkClassification.NeuralNetworkInput;

public class PlainTextSaver {

	private String fileName;

	public PlainTextSaver() {
		fileName = "data.txt";
	}

	public PlainTextSaver(String filename) {
		this.fileName = filename;
	}

	public void save(List<NeuralNetworkInput> data) {
		try {
			FileWriter fw = new FileWriter(fileName, true);
			for (NeuralNetworkInput singleData : data) {
				fw.write(String.format(Locale.US, " %f, %f, ", singleData.getMeanHoldTime(),
						singleData.getMeanInterTime()));
				fw.write(translateIdToBinaryRepresentation(singleData.getUserId(), 56));
				fw.write(System.getProperty("line.separator"));
			}
			fw.close();
		}
		catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}

	private String translateIdToBinaryRepresentation(int id, int numberOfClasses) {
		String binaryRepresentation = "";
		int i = 1;
		while (i < numberOfClasses) {
			if (i == id - 1) {
				binaryRepresentation += "1, ";
			}
			else {
				binaryRepresentation += "0, ";
			}
			i++;
		}
		if (i == id - 1) {
			binaryRepresentation += "1";
		}
		else {
			binaryRepresentation += "0";
		}
		System.out.println("binary representation for: " + id + ": " + binaryRepresentation);
		return binaryRepresentation;
	}

}
