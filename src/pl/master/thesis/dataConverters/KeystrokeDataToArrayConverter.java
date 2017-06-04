package pl.master.thesis.dataConverters;

import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import pl.master.thesis.keyTypingObjects.DigraphTimingData;

public class KeystrokeDataToArrayConverter {

	private final int ELEMENTS_IN_ROW = 2;

	public RealMatrix convertPreprocessedDataToMatrix(List<DigraphTimingData> data) {
		double[] row = new double[ELEMENTS_IN_ROW];
		double[][] result = new double[data.size()][ELEMENTS_IN_ROW];
		int i = 0;
		for (DigraphTimingData element : data) {
			row[0] = element.getKey2HoldingTime().getHoldTime();
			row[1] = element.getKey1HoldingTime().getHoldTime();
			// row[2]=element.getKey2HoldingTime().getHoldTime();
			result[i] = row;
			i++;
		}
		return MatrixUtils.createRealMatrix(result);
	}

}
