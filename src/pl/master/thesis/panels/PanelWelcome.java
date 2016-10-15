package pl.master.thesis.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import pl.masater.thesis.listeners.ConnectionListener;
import pl.master.thesis.buttons.MyButton;
import pl.master.thesis.buttons.MyLabel;
import pl.master.thesis.database.ConnectionSwingWorker;
import pl.master.thesis.database.Database;
import pl.master.thesis.database.SqlStatements;
import pl.master.thesis.dialogs.WaitingDialog;
import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.others.Strings;

public class PanelWelcome extends BasicPanel {
	
	private static final long serialVersionUID = 1L;
	private JPasswordField passField;
	private JTextField loginField;	
	private ConnectionListener db;
	
	class Con extends ConnectionSwingWorker {
		private boolean exists;
		
		@Override
		public void done(){
        	db.close();			
		}
		
		@Override
		protected void doSqlStatements(Connection con) throws SQLException{
			exists = SqlStatements.userExists(con,loginField.getText(), passField.getPassword());
		}
		
		@Override
		protected void doOtherThings (){
			if (exists){				
				if (isErrorShowing)	removeError();	
				frame.gotoPanel(MainWindow.CONGRATULATIONS_PANEL); 
			}
			else {
				String error = Strings.USER_OR_PASS_INCORRECT;
				errorLabel.setText(error);
				
				if (!isErrorShowing)						
					isErrorShowing=true;				
				addErrorLabel();				

			}
		}
		
		private void addErrorLabel(){
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridy=2;
			c.gridwidth=2;
			c.insets= new Insets(0,0,10,0);
						
			moveElementsDown(1,c.gridy);
			add(errorLabel,c);
		}
		
		
	}
	
	public PanelWelcome (final MainWindow frame) {
		
		super(frame);
		
		JTextArea hello = createWelcomeMessage();
		
		MyLabel loginLabel = new MyLabel ("login");
		MyLabel passLabel = new MyLabel("has³o");
		
		loginField = new JTextField(15);		
		passField = new JPasswordField(15);
		btnContinue.setText("Utworz konto");		
		MyButton btnCreate = createButtonCreate();
		
		addFieldFocusListener(loginField);
		addFieldFocusListener(passField);			
		
		c.insets=fieldInsets;
		c.fill=GridBagConstraints.HORIZONTAL;
//		c.anchor=GridBagConstraints.CENTER';
		c.gridwidth=2;		
		add(hello,c);		
		
		c.gridwidth=1;
		c.gridy=1;
		c.fill=GridBagConstraints.NONE;
		c.anchor=GridBagConstraints.CENTER;
		add(btnContinue,c);	
				
		c.gridy=2;
		add(loginLabel,c);		
		
		c.gridx=1;
		add(loginField,c);
		
		c.gridy=3;
		c.gridx=0;
		add(passLabel,c);
		
		c.gridx=1;
		add(passField,c);
		
		c.gridy=4;
		add(btnCreate,c);		
			
	}
	
	private JTextArea createWelcomeMessage(){
		JTextArea hello = new JTextArea ();
		String s = "Witam w moim programie. Jeœli masz ju¿ za³o¿one konto, zaloguj siê korzystaj¹c z poni¿szych pól."
				+ "W przeciwnym przypadku za³ó¿ nowe konto u¿ywaj¹c odpowiedniego przycisku.";
		
		hello.setForeground(Color.WHITE);
		hello.setText(s);
		hello.setLineWrap(true);
		hello.setWrapStyleWord(true);
		hello.setEditable(false);
		hello.setOpaque(false);
		hello.setHighlighter(null);
		
		return hello;
	}
	
	private MyButton createButtonCreate(){
		MyButton btnCreate = new MyButton (frame,"Zaloguj sie");
		db=new ConnectionListener(this, new Con());
		btnCreate.addActionListener(db);
		btnCreate.addActionListener(new ActionListener (){
			@Override
			public void actionPerformed (ActionEvent e){
				db.setSwingWorker(new Con());
			}
		});
		return btnCreate;
	}
	
	private JTextField addFieldFocusListener(final JTextField field){
		FocusAdapter a = new FocusAdapter(){
			@Override
			public void focusGained(FocusEvent e){
				if (isErrorShowing) 
					setErrorText("Proszê wprowadziæ poprawki.");				
			}
		};
		field.addFocusListener(a);
		return field;
	}
	
	private void removeError (){
		
		GridBagLayout g = (GridBagLayout)getLayout();
		g.removeLayoutComponent(errorLabel);
		remove(errorLabel);
		repaint();
//		revalidate(); //TODO dont revalidate intentionally
		isErrorShowing=false;
	}
			
	private void setErrorText (String errorText){
		errorLabel.setText(errorText);
		repaint();
		revalidate();
		isErrorShowing=false;
	}
	
	
		

}
	