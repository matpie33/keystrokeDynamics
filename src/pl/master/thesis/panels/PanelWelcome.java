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
	private ProgressMonitor p;
	private JPasswordField pass;
	private JTextField login;	
	private ConnectionListener db;
	
	class Con extends ConnectionSwingWorker {
		private boolean exists;
		
		@Override
		public void done(){
//			p.setProgress(0);
        	db.close();			
		}
		
		@Override
		protected void doSqlStatements(Connection con) throws SQLException{
			exists = SqlStatements.userExists(con,login.getText(), pass.getPassword());
		}
		
		@Override
		protected void doOtherThings (){
			if (exists){
				
				if (isErrorShowing)	removeError();	
				frame.gotoPanel(MainWindow.CONGRATULATIONS_PANEL);    				
				System.out.println("exists");
			}
			else {
				String error = Strings.USER_OR_PASS_INCORRECT;
				errorLabel.setText(error);
				
				if (isErrorShowing==true){							
//					return;
				}
				else isErrorShowing=true;
				
				GridBagConstraints cc = new GridBagConstraints();
				
				cc.gridy=2;
				cc.gridwidth=2;
				cc.insets= new Insets(0,0,10,0);
							
				moveElementsDown(1,cc.gridy);
				add(errorLabel,cc);

			}
		}
		
		
	}
	
	public PanelWelcome (final MainWindow frame) {
		
		super(frame);
		final BasicPanel pan = this;
		new Font ("Verdana", Font.PLAIN,20);
		
		JTextArea hello = new JTextArea ();
		String s = "Witam w moim programie. Je�li masz ju� za�o�one konto, zaloguj si� korzystaj�c z poni�szych p�l."
				+ "W przeciwnym przypadku za�� nowe konto u�ywaj�c odpowiedniego przycisku.";
		
		hello.setForeground(Color.WHITE);
		hello.setText(s);
		hello.setLineWrap(true);
		hello.setWrapStyleWord(true);
		hello.setEditable(false);
		hello.setOpaque(false);
		hello.setHighlighter(null);
		
		MyLabel login = new MyLabel ("login");
		this.login = new JTextField(15);
		MyLabel password = new MyLabel("password");
		pass = new JPasswordField(15);
		
		addFieldFocusListener(this.login);
		addFieldFocusListener(pass);
		
		p = new ProgressMonitor (pan, "Connecting to database...", "", 0, 100);
		btnContinue.setText("Create account");
		
		MyButton btnCreate = new MyButton (frame,"Log in");
		db=new ConnectionListener(this, new Con());
		btnCreate.addActionListener(db);
		btnCreate.addActionListener(new ActionListener (){
			@Override
			public void actionPerformed (ActionEvent e){
				db.setSwingWorker(new Con());
			}
		});
		
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
		add(login,c);		
		
		c.gridx=1;
		add(this.login,c);
		
		c.gridy=3;
		c.gridx=0;
		add(password,c);
		
		c.gridx=1;
		add(pass,c);
		
		c.gridy=4;
		add(btnCreate,c);		
			
	}
	
	private JTextField addFieldFocusListener(final JTextField field){
		FocusAdapter a = new FocusAdapter(){
			@Override
			public void focusGained(FocusEvent e){
				if (isErrorShowing) {
//					removeError();
					setErrorText("Prosz� wprowadzi� poprawki.");
				}
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
	