package pl.master.thesis.swingWorkers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.JTextField;

import pl.master.thesis.database.SqlStatements;
import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.guiElements.MyLabel;
import pl.master.thesis.keyTypingObjects.WordKeystrokeData;
import pl.master.thesis.myOwnClassification.ClassificationManager;
import pl.master.thesis.neuralNetworkClassification.NeuralNetworkInput;
import pl.master.thesis.others.FieldsVerifier;
import pl.master.thesis.panels.BasicPanel;
import pl.master.thesis.panels.PanelSummary;
import pl.master.thesis.strings.FormsLabels;
import pl.master.thesis.strings.Prompts;

public class AddUserWorker extends ConnectionSwingWorker {

	private PanelSummary panel;
	private Map<JTextField, MyLabel> hmap;
	private MainWindow frame;

	public AddUserWorker(PanelSummary panel, Map<JTextField, MyLabel> hmap) {
		this.panel = panel;
		this.hmap = hmap;
		frame = ((BasicPanel) panel).getParentFrame();
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
			int userId = SqlStatements.addUser(connection, userName, password, question, answer);
			ClassificationManager manager = frame.getKeyEventHandler().getClassifier();
			List<WordKeystrokeData> data = manager.getTypingData();
			for (WordKeystrokeData word : data) {
				SqlStatements.addTypingData(connection, manager.convertDataForDbNeeds(word), userId,
						word.isStartedWithTab());
			}
			connection.commit();
			List<NeuralNetworkInput> neuralInputs = manager.learnData(userId);
			manager.saveData(neuralInputs);
			connection.close();
			frame.nextPanel();
			frame.clearData();
			panel.closeDialog();
		}
		catch (SQLException e1) { // TODO do rollback IF ANY SQL EXCEPTION
									// OCCURS
			panel.closeDialog();
			frame.previousPanel();
			if (e1.getSQLState() == "23505") {
				panel.showShortMessageDialog(Prompts.ERROR_SQL_USER_EXISTS);
			}
			else if (e1.getSQLState() == "XJ040") {
				panel.showShortMessageDialog(Prompts.ERROR_SQL_DATABASE_IN_USE);
			}
			else {
				panel.showShortMessageDialog(Prompts.ERROR_SQL_UNKNOWN_ERROR);
				e1.printStackTrace();
			}

		}

		frame.getKeyEventHandler().clear();

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
