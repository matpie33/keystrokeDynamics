package pl.master.thesis.swingWorkers;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

import pl.master.thesis.database.SqlStatements;
import pl.master.thesis.dialogs.MyDialog;
import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.panels.BasicPanel;
import pl.master.thesis.panels.PanelWelcome;
import pl.master.thesis.strings.FormsLabels;
import pl.master.thesis.strings.Prompts;

public class UserCheckWorker extends ConnectionSwingWorker {
	
		private boolean userExists;
		private PanelWelcome panel;
		private MainWindow frame;
		private MyDialog dialog;
		
		public UserCheckWorker(PanelWelcome panel, MyDialog dialog){	
			this.panel = panel;
			this.frame = ((BasicPanel)panel).getParentFrame();
			this.dialog = dialog;			
		}
		
		@Override
		public void done(){
        	dialog.dispose();		
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
				panel.setText(error);								
				panel.addErrorLabel();				

			}
		}	
						
	}

