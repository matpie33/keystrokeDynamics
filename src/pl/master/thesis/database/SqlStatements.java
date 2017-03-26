package pl.master.thesis.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import pl.master.thesis.keyTypingObjects.PreprocessedKeystrokeData;

public class SqlStatements {

	public static int addUser(Connection connection, String username, String password,
			String question, String answer) throws SQLException {
		PreparedStatement statement = null;
		String query = "INSERT INTO Users (Username,Password, Question, Answer) " + "VALUES ('"
				+ username + "','" + password + "','" + question + "','" + answer + "')";
		int userId = executeStatementAndGetGeneratedId(query, connection);
		return userId;
	}

	public static boolean userExists(Connection connection, String username, char[] password)
			throws SQLException {
		String passwordd = new String(password);
		Statement s = null;
		String query = "SELECT * " + "FROM Users " + "WHERE username='" + username
				+ "' AND password='" + passwordd + "'";

		s = connection.createStatement();

		ResultSet r = s.executeQuery(query);
		// connection.close();
		return r.next();
	}

	public static void addTypingData(Connection connection, List<PreprocessedKeystrokeData> words,
			int userId, boolean tabbed) throws SQLException {

		PreparedStatement statement = null;
		String wordToUserMapQuery = String
				.format("INSERT INTO APP.WORDS_LETTERS (USERID) VALUES (%d)", userId, tabbed);
		int wordId = executeStatementAndGetGeneratedId(wordToUserMapQuery, connection);
		String wordDataQuery = "INSERT INTO WORDS VALUES(%s)";

		for (PreprocessedKeystrokeData singleWord : words) {
			String values = getWordDataAsString(singleWord, wordId);
			statement = connection.prepareStatement(String.format(wordDataQuery, values));
			statement.executeUpdate();
			statement.close();
		}

	}

	private static int executeStatementAndGetGeneratedId(String query, Connection connection)
			throws SQLException {
		PreparedStatement statement = connection.prepareStatement(query,
				Statement.RETURN_GENERATED_KEYS);
		statement.executeUpdate();
		ResultSet generatedKeys = statement.getGeneratedKeys();
		int id = 0;
		if (generatedKeys.next()) {
			id = generatedKeys.getInt(1);
		}
		generatedKeys.close();
		statement.close();
		return id;
	}

	private static String getWordDataAsString(PreprocessedKeystrokeData singleWord, int wordId) {
		String values = "";
		values += "'" + singleWord.getInterKeyTime().getInterKeyTime() + "',";
		values += "'" + singleWord.getKey1HoldingTime().getHoldTime() + "',";
		values += "'" + singleWord.getKey2HoldingTime().getHoldTime() + "',";
		values += "'" + singleWord.getKey1HoldingTime().getKey() + "',";
		values += "'" + singleWord.getKey2HoldingTime().getKey() + "',";
		values += wordId + ",";
		values += singleWord.isKeysPressedTogether();
		return values;
	}

}
