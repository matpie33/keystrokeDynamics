package pl.master.thesis.savers;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PlainTextSaver {

	private String fileName;

	public PlainTextSaver() {
		fileName = "data.txt";
	}

	public PlainTextSaver(String filename) {
		this.fileName = filename;
	}

	public void save(Map<List<String>, Integer> data) {
		try {
			FileWriter fw = new FileWriter(fileName, true);
			for (List<String> row : data.keySet()) {
				for (String element : row) {
					fw.write(String.format(Locale.US, "%s, ", element));

				}
				fw.write(translateIdToBinaryRepresentation(data.get(row), 56));
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
		// System.out.println("binary representation for: " + id + ": " +
		// binaryRepresentation);
		return binaryRepresentation;
	}

}
