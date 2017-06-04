package pl.master.thesis.swingWorkers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pl.master.thesis.dataConverters.WordToDigraphsConverter;
import pl.master.thesis.database.UsersTableAndWordsTableData;
import pl.master.thesis.database.UsersTableData;
import pl.master.thesis.keyTypingObjects.WordKeystrokeData;

public class AddUserDataOnlyWorker extends ConnectionSwingWorker {
	private List<UsersTableAndWordsTableData> allUsersInfoAndTypingData;
	private WordToDigraphsConverter converter;

	public AddUserDataOnlyWorker(WordToDigraphsConverter wordDataConverter) {
		this.converter = wordDataConverter;
		allUsersInfoAndTypingData = new ArrayList<>();
	}

	public void addData(UsersTableData userData, List<WordKeystrokeData> data) {
		UsersTableAndWordsTableData datas = new UsersTableAndWordsTableData(userData, data);
		allUsersInfoAndTypingData.add(datas);
	}

	@Override
	protected void doSqlStatements(Connection connection) throws SQLException {
		System.out.println("all users?: " + allUsersInfoAndTypingData.size());
		for (UsersTableAndWordsTableData user : allUsersInfoAndTypingData) {
			System.out.println("userid: " + user.getUserData().getUserId());
			AddUserWorker.addUserAndHisTypingDataToDatabase(connection, user.getUserData(),
					user.getData(), converter);
		}
		allUsersInfoAndTypingData.clear();

	}

}
