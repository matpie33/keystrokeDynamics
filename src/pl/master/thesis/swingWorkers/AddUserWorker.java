package pl.master.thesis.swingWorkers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import pl.master.thesis.buttons.MyButton;
import pl.master.thesis.buttons.MyLabel;
import pl.master.thesis.database.SqlStatements;
import pl.master.thesis.dialogs.MyDialog;
import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.others.FieldsVerifier;
import pl.master.thesis.panels.BasicPanel;
import pl.master.thesis.strings.FormsLabels;
import pl.master.thesis.strings.Prompts;

public class AddUserWorker extends ConnectionSwingWorker{
	
	private MyDialog waitingDialog;	
	private BasicPanel panel;
	private Map <JTextField, MyLabel> hmap;
	private MainWindow frame;
	
	public AddUserWorker (BasicPanel panel, MyDialog wd, Map<JTextField, MyLabel> hmap){
		this.panel = panel;
		waitingDialog=wd;
		this.hmap = hmap;
		frame = ((BasicPanel)panel).getParentFrame();
	}
	
		@Override
		protected void doSqlStatements (Connection connection) throws SQLException{			
			
			tryToAddUserToDB(connection);						
		}
		
		private void tryToAddUserToDB(Connection connection){
			String userName = FieldsVerifier.findTextField(FormsLabels.NAZWA_UZYTKOWNIKA, hmap).getText();
			String password = FieldsVerifier.findTextField(FormsLabels.HASLO, hmap).getText();
			String question = FieldsVerifier.findTextField(FormsLabels.PYTANIE_POMOCNICZE, hmap).getText();
			String answer = FieldsVerifier.findTextField(FormsLabels.ODPOWIEDZ, hmap).getText();
			try{
				SqlStatements.addUser(connection, userName, password,question,answer);				
				frame.nextPanel();
				waitingDialog.dispose();
			}
			catch (SQLException e1) { //TODO do rollback IF ANY SQL EXCEPTION OCCURS
				if (e1.getSQLState()=="23505"){
					waitingDialog.setLabelText(Prompts.ERROR_SQL_USER_EXISTS);
					frame.gotoPanel(MainWindow.DATA_PANEL);
				}
				else if (e1.getSQLState()=="XJ040"){
					waitingDialog.setLabelText (Prompts.ERROR_SQL_DATABASE_IN_USE);
				}
				else{
					waitingDialog.setLabelText(Prompts.ERROR_SQL_UNKNOWN_ERROR);
					e1.printStackTrace();
				}
			}
		}
		
		@Override
		public void done(){
			waitingDialog.removeGif();
			JButton b = new MyButton(frame,"O.k.");
			b.addActionListener(new ActionListener (){
				@Override
				public void actionPerformed(ActionEvent e){
					waitingDialog.dispose();
				}
			});
			waitingDialog.addButton(b);
		}
	}

