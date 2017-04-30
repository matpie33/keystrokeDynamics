package pl.master.thesis.swingWorkers;

import java.sql.Connection;
import java.sql.SQLException;

import pl.master.thesis.database.SqlStatements;

public class AddUsersFromDatasetWorker extends ConnectionSwingWorker {
	@Override
	protected void doSqlStatements(Connection connection) throws SQLException {
		System.out.println("last user is: " + SqlStatements.getLastUserId(connection));
		for (int i = 0; i < 56; i++) {
			String userName = "username" + i;
			String password = "password" + i;
			String question = "question" + i;
			String answer = "answer" + i;
			int id = i;
			SqlStatements.addUser(connection, userName, password, question, answer, id);
		}

		connection.close();
	}
}
