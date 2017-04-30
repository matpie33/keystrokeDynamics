package pl.master.thesis.swingWorkers;

import java.sql.Connection;
import java.sql.SQLException;

import pl.master.thesis.csvManipulation.CSVSaver;
import pl.master.thesis.database.SqlStatements;

public class GetKeystrokeDataFromDBWorker extends ConnectionSwingWorker {

	private String fileToSave;

	public GetKeystrokeDataFromDBWorker(String fileName) {
		fileToSave = fileName;
	}

	@Override
	protected void doSqlStatements(Connection connection) throws SQLException {
		CSVSaver csv = new CSVSaver(fileToSave);
		csv.save(SqlStatements.getAllUsersKeystrokeData(connection));
	}

}
