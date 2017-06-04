package pl.master.thesis.swingWorkers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.derby.shared.common.error.DerbySQLIntegrityConstraintViolationException;
import org.xml.sax.SAXException;

import pl.master.thesis.dataConverters.WordToDigraphsConverter;
import pl.master.thesis.database.SqlStatements;
import pl.master.thesis.database.UsersTableData;
import pl.master.thesis.guiElements.MyLabel;
import pl.master.thesis.keyTypingObjects.WordKeystrokeData;
import pl.master.thesis.main.MainWindow;
import pl.master.thesis.myOwnClassification.ClassificationManager;
import pl.master.thesis.neuralNetworkClassification.NeuralNetworkInput;
import pl.master.thesis.others.FieldsVerifier;
import pl.master.thesis.panels.PanelSummary;
import pl.master.thesis.strings.FormsLabels;
import pl.master.thesis.strings.Prompts;

public class AddUserWorker extends ConnectionSwingWorker {

	private Map<JTextField, MyLabel> hmap;
	private MainWindow frame;

	public AddUserWorker(PanelSummary panel, Map<JTextField, MyLabel> hmap) {
		this.panel = panel;
		this.hmap = hmap;
		frame = panel.getParentFrame();
		frame.addCloseListener(database);
	}

	@Override
	protected void doSqlStatements(Connection connection) throws SQLException {
		tryToAddUserToDB(connection);
	}

	private void tryToAddUserToDB(Connection connection) {
		String userName = FieldsVerifier.findTextField(FormsLabels.USERNAME, hmap).getText();
		String password = FieldsVerifier.findTextField(FormsLabels.PASSWORD, hmap).getText();
		String question = FieldsVerifier.findTextField(FormsLabels.RECOVERY_QUESTION, hmap)
				.getText();
		String answer = FieldsVerifier.findTextField(FormsLabels.ANSWER, hmap).getText();
		try {
			connection.setAutoCommit(false);
			ClassificationManager manager = frame.getKeyEventHandler().getClassifier();
			List<WordKeystrokeData> data = manager.getTypingData();
			String userIds = SqlStatements.getLastUserId(connection);
			int userId = Integer.parseInt(userIds) + 1;
			manager.setNumberOfUsers(userId + 1);
			UsersTableData userData = new UsersTableData().setAnswer(answer).setPassword(password)
					.setQuestion(question).setUserId(userId).setUserName(userName);

			panel.setMessage(Prompts.ADDING_USER_TYPING_DATA_PROMPT);
			AddUserWorker.addUserAndHisTypingDataToDatabase(connection, userData, data,
					manager.getWordToSimpleObjectConverter());
			connection.commit();
			panel.setMessage(Prompts.LEARNING_NEURAL_NETWORK_PROMPT);
			List<NeuralNetworkInput> neuralInputs;
			try {
				neuralInputs = manager.learnData(userId);
				manager.saveData(neuralInputs);
			}
			catch (IOException | InterruptedException | ParserConfigurationException
					| SAXException e) {
				panel.showShortMessageDialog(Prompts.ERROR_LEARNING_DATASET + e.getMessage());
				e.printStackTrace();
			}

			frame.nextPanel();
			frame.clearData();
			panel.closeDialog();
		}
		catch (DerbySQLIntegrityConstraintViolationException ex) {
			try {
				connection.rollback();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			panel.closeDialog();
			frame.previousPanel();
			panel.showShortMessageDialog(Prompts.ERROR_SQL_USER_EXISTS);
		}
		catch (SQLException e1) {
			try {
				System.out.println("rollbacked");
				connection.rollback();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			panel.closeDialog();
			frame.previousPanel();
			if (e1.getSQLState() == "XJ040") {
				panel.showShortMessageDialog(Prompts.ERROR_SQL_DATABASE_IN_USE);
			}
			else {
				panel.showShortMessageDialog(Prompts.ERROR_SQL_UNKNOWN_ERROR);
				e1.printStackTrace();
			}
		}
		finally {
			try {
				connection.close();
				System.out.println("did I close it?");
				System.out.println(connection.isClosed());
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}

		frame.getKeyEventHandler().clear();

	}

	public static void addUserAndHisTypingDataToDatabase(Connection connection,
			UsersTableData userData, List<WordKeystrokeData> data,
			WordToDigraphsConverter manager) throws SQLException {
		SqlStatements.addUser(connection, userData);
		System.out.println("User id: " + userData.getUserId());
		for (int i = 0; i < data.size(); i++) {
			WordKeystrokeData word = data.get(i);
			SqlStatements.addTypingData(connection, manager.convertSingleWordToSimplestData(word),
					userData.getUserId(), word.isStartedWithTab());
		}
	}

	@Override
	public void done() {
		panel.closeDialog();
		try {
			get(); // this line can throw InterruptedException or
					// ExecutionException
		}
		catch (ExecutionException e) {
			e.printStackTrace();
		}
		catch (InterruptedException ie) {
			// TODO handle the case where the background task was interrupted as
			// you want to
		}
	}
}
