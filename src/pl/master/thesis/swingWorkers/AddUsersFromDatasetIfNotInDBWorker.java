package pl.master.thesis.swingWorkers;

import java.sql.Connection;
import java.sql.SQLException;

import pl.master.thesis.database.SqlStatements;
import pl.master.thesis.database.UsersTableData;

public class AddUsersFromDatasetIfNotInDBWorker extends ConnectionSwingWorker {
	private int usersInDataset;
	private boolean usersFromDatasetAreInDatabase;

	public AddUsersFromDatasetIfNotInDBWorker(int usersInDataset) {
		this.usersInDataset = usersInDataset;
		usersFromDatasetAreInDatabase = false;
	}

	@Override
	protected void doSqlStatements(Connection connection) throws SQLException {
		System.out.println("Checking how many users there is");
		System.out.println("last user is: " + SqlStatements.getLastUserId(connection));
		String usersSize = SqlStatements.getLastUserId(connection);
		int usersSizeInt = Integer.parseInt(usersSize);
		if (usersSizeInt >= usersInDataset - 1) {
			System.out
					.println(String.format("Found more then %s users, skipping.", usersInDataset));
			return;
		}
		System.out.println(
				String.format("not enough users found %s; adding from dataset then", usersSizeInt));
		for (int i = 0; i < usersInDataset; i++) {
			String userName = "username" + i;
			String password = "password" + i;
			String question = "question" + i;
			String answer = "answer" + i;
			int id = i;
			UsersTableData usersData = new UsersTableData().setAnswer(answer).setQuestion(question)
					.setUserId(id).setUserName(userName).setPassword(password);
			SqlStatements.addUser(connection, usersData);
		}

		connection.close();
	}
}
