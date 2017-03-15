package pl.master.thesis.swingWorkers;

import java.sql.Connection;
import java.sql.SQLException;

import pl.master.thesis.database.SqlStatements;
import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.panels.BasicPanel;
import pl.master.thesis.panels.PanelWelcome;
import pl.master.thesis.strings.Prompts;

public class UserCheckWorker extends ConnectionSwingWorker {
	
		private boolean userExists;
		private PanelWelcome panel;
		private MainWindow frame;
		
		public UserCheckWorker(PanelWelcome panel){	
			this.panel = panel;
			this.frame = ((BasicPanel)panel).getParentFrame();
		}
		
		@Override
		public void done(){
			panel.closeDialog();
		}
		
		@Override
		protected void doSqlStatements(Connection con) throws SQLException{
			userExists = SqlStatements.userExists(con,panel.getUserName(), panel.getPassword());
		}
		
		@Override
		protected void doOtherThings (){
			if (userExists){				
				if (panel.isErrorShowing())	panel.removeError();	
				
				frame.gotoPanel(MainWindow.CONGRATULATIONS_PANEL); 
			}
			else {
				String error = Prompts.USER_OR_PASS_INCORRECT;
				panel.setErrorText(error);								
				panel.addErrorLabel();				

			}
			panel.closeDialog();
		}	
						
	}

