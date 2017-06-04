package pl.master.thesis.crossValidation;

import java.util.ArrayList;
import java.util.List;

import org.nd4j.linalg.dataset.DataSet;

public class Fold {

	private List<DataSet> rowsFromDataset;
	private DataSet mergedDataSet;

	public Fold() {
		rowsFromDataset = new ArrayList<>();
	}

	public void addRowFromDataSet(DataSet row) {
		rowsFromDataset.add(row);
	}

	public DataSet mergeRowsAndGet() {
		mergedDataSet = DataSet.merge(rowsFromDataset);
		return mergedDataSet;
	}

	@Override
	public String toString() {
		return "fold: " + rowsFromDataset;
	}

}
