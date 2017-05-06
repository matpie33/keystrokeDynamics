package pl.master.thesis.database;

import java.util.List;

import pl.master.thesis.keyTypingObjects.WordKeystrokeData;

public class UsersTableAndWordsTableData {

	private UsersTableData userData;
	private List<WordKeystrokeData> data;

	public UsersTableAndWordsTableData(UsersTableData userData, List<WordKeystrokeData> data) {
		this.userData = userData;
		this.data = data;
	}

	public UsersTableData getUserData() {
		return userData;
	}

	public List<WordKeystrokeData> getData() {
		return data;
	}

}
