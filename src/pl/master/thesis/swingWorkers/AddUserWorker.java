package pl.master.thesis.swingWorkers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.JTextField;

import pl.master.thesis.database.SqlStatements;
import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.guiElements.MyLabel;
import pl.master.thesis.others.FieldsVerifier;
import pl.master.thesis.panels.BasicPanel;
import pl.master.thesis.strings.FormsLabels;
import pl.master.thesis.strings.Prompts;

public class AddUserWorker extends ConnectionSwingWorker{
	
	private BasicPanel panel;
	private Map <JTextField, MyLabel> hmap;
	private MainWindow frame;
	
	public AddUserWorker (BasicPanel panel,  Map<JTextField, MyLabel> hmap){
		this.panel = panel;
		this.hmap = hmap;
		frame = ((BasicPanel)panel).getParentFrame();
	}
	
		@Override
		protected void doSqlStatements (Connection connection) throws SQLException{			
			
			tryToAddUserToDB(connection);						
		}
		
		private void tryToAddUserToDB(Connection connection){
			String userName = FieldsVerifier.findTextField(FormsLabels.USERNAME, hmap).getText();
			String password = FieldsVerifier.findTextField(FormsLabels.PASSWORD, hmap).getText();
			String question = FieldsVerifier.findTextField(FormsLabels.RECOVERY_QUESTION, hmap).getText();
			String answer = FieldsVerifier.findTextField(FormsLabels.ANSWER, hmap).getText();
			try{
				SqlStatements.addUser(connection, userName, password,question,answer);				
				frame.nextPanel();
				panel.closeDialog();
			}
			catch (SQLException e1) { //TODO do rollback IF ANY SQL EXCEPTION OCCURS
				panel.closeDialog();
				frame.previousPanel();
				
				if (e1.getSQLState()=="23505"){
					panel.showShortMessageDialog(Prompts.ERROR_SQL_USER_EXISTS);
					
				}
				else if (e1.getSQLState()=="XJ040"){
					panel.showShortMessageDialog(Prompts.ERROR_SQL_DATABASE_IN_USE);
				}
				else{
					panel.showShortMessageDialog(Prompts.ERROR_SQL_UNKNOWN_ERROR);
					e1.printStackTrace();
				}
				
			}
		}
		
		@Override
		public void done(){
			panel.closeDialog();
		}
	}

