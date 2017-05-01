package pl.master.thesis.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pl.master.thesis.csvManipulation.CSVProcessing;
import pl.master.thesis.keyTypingObjects.PreprocessedKeystrokeData;
import pl.master.thesis.neuralNetworkClassification.NeuralNetworkInput;

public class SqlStatements {

	public static int addUser(Connection connection, String username, String password,
			String question, String answer, int id) throws SQLException {
		PreparedStatement statement = null;
		String query = "INSERT INTO Users (Username,Password, Question, Answer, Id) " + "VALUES ('"
				+ username + "','" + password + "','" + question + "','" + answer + "'," + id + ")";

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

		System.out.println("adding typing data");
		System.out.println(words);
		String wordDataQuery = "INSERT INTO WORDS (INTERKEYTIME, HOLDTIME1, HOLDTIME2,"
				+ "KEY1, KEY2, PRESSEDTOGETHER) VALUES(%s)";
		for (PreprocessedKeystrokeData singleWord : words) {
			String values = getWordDataAsString(singleWord);
			int wordId = executeStatementAndGetGeneratedId(String.format(wordDataQuery, values),
					connection);
			System.out.println(String.format(wordDataQuery, values));
			String wordToUserMapQuery = String.format(
					"INSERT INTO APP.WORDS_LETTERS (TABBED, USERID, WORDID) VALUES (%b, %d, %d)",
					tabbed, userId, wordId);
			executeStatementAndGetGeneratedId(wordToUserMapQuery, connection);
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

	private static String getWordDataAsString(PreprocessedKeystrokeData singleWord) {
		String values = "";
		values += "'" + singleWord.getInterKeyTime().getInterKeyTime() + "',";
		values += "'" + singleWord.getKey1HoldingTime().getHoldTime() + "',";
		values += "'" + singleWord.getKey2HoldingTime().getHoldTime() + "',";
		values += "'" + singleWord.getKey1HoldingTime().getKey() + "',";
		values += "'" + singleWord.getKey2HoldingTime().getKey() + "',";
		values += singleWord.isKeysPressedTogether();
		return values;
	}

	public static String getLastUserId(Connection connection) throws SQLException {
		String query = "SELECT ID FROM USERS ORDER BY ID DESC FETCH FIRST ROW ONLY";
		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery(query);
		return result.next() ? result.getString(1) : "0";
	}

	public static List<NeuralNetworkInput> getAllUsersKeystrokeData(Connection connection)
			throws SQLException {
		String query = "SELECT  WORDS.INTERKEYTIME, WORDS.HOLDTIME1, WORDS.HOLDTIME2, WORDS.KEY1, WORDS.KEY2, WORDS.WORDID,"
				+ "WORDS_LETTERS.USERID FROM WORDS INNER JOIN WORDS_LETTERS ON WORDS.WORDID = WORDS_LETTERS.WORDID "
				+ "ORDER BY USERID, WORDID";
		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery(query);
		List<NeuralNetworkInput> data = new ArrayList<>();
		List<Double> keyHoldTimes = new ArrayList<>();
		List<Double> interTimesList = new ArrayList<>();
		String wordId = "";
		String userId = "";
		while (result.next()) {

			if (!result.getString("WORDID").equals(wordId) && !keyHoldTimes.isEmpty()
					&& !interTimesList.isEmpty()) {
				NeuralNetworkInput input = CSVProcessing
						.convertHoldTimesAndInterKeyTimesListsToNeuralInput(keyHoldTimes,
								interTimesList);
				input.setUserId(Integer.parseInt(userId));
				data.add(input);
				keyHoldTimes = new ArrayList<>();
				interTimesList = new ArrayList<>();
			}
			assert (data != null);
			double second_in_nano = 1_000_000_000D;
			long interKeyTime = Long.parseLong(result.getString("INTERKEYTIME"));
			interTimesList.add((double) interKeyTime / second_in_nano);
			// data.addInterKeyTime(new InterKeyTime(new Digraph(key1, key2),
			// interKeyTime));

			long key1HoldTime = Long.parseLong(result.getString("HOLDTIME1"));
			long key2HoldTime = Long.parseLong(result.getString("HOLDTIME2"));
			keyHoldTimes.add((double) key1HoldTime / second_in_nano);
			keyHoldTimes.add((double) key2HoldTime / second_in_nano);
			// data.addKeyHoldTime(new KeyHoldingTime(key1, key1HoldTime, 0));
			// data.addKeyHoldTime(new KeyHoldingTime(key2, key2HoldTime, 0));
			wordId = result.getString("WORDID");
			userId = result.getString("USERID");
			System.out.println("userid: " + userId);

		}
		System.out.println(data);
		return data;

	}

}
