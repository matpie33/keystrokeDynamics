package pl.master.thesis.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import pl.master.thesis.buttons.MyButton;
import pl.master.thesis.buttons.MyLabel;
import pl.master.thesis.dialogs.MyDialog;
import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.strings.Prompts;
import pl.master.thesis.swingWorkers.UserCheckWorker;

public class PanelWelcome extends BasicPanel {
	
	private static final long serialVersionUID = 1L;
	private JPasswordField passField;
	private JTextField loginField;		
	
	public PanelWelcome (final MainWindow frame) {
		
		super(frame);
		
		JTextArea hello = createWelcomeMessage();
		
		MyLabel loginLabel = new MyLabel (Prompts.LABEL_LOGIN);
		MyLabel passLabel = new MyLabel(Prompts.PASSWORD_LABEL);
		
		loginField = new JTextField(15);		
		passField = new JPasswordField(15);
		btnContinue.setText(Prompts.BTN_CREATE_ACCOUNT);		
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
		
		hello.setForeground(Color.WHITE);
		hello.setText(Prompts.WELCOME_PROMPT);
		hello.setLineWrap(true);
		hello.setWrapStyleWord(true);
		hello.setEditable(false);
		hello.setOpaque(false);
		hello.setHighlighter(null);
		
		return hello;
	}
	
	private MyButton createButtonCreate(){
		MyButton btnCreate = new MyButton (frame,Prompts.BTN_LOGIN);
		final PanelWelcome panel = this;
	
		btnCreate.addActionListener(new ActionListener (){
			@Override
			public void actionPerformed (ActionEvent e){
				MyDialog dialog = new MyDialog(panel);
				dialog.createWaitingPanel();
				SwingWorker s = new UserCheckWorker(panel,dialog);
				s.execute();
				dialog.setVisible(true);
			}
		});
		return btnCreate;
	}
	
	private JTextField addFieldFocusListener(final JTextField field){
		FocusAdapter a = new FocusAdapter(){
			@Override
			public void focusGained(FocusEvent e){
				if (isErrorShowing) 
					setErrorText(Prompts.MESSAGE_CORRECTION_NEEDED);				
			}
		};
		field.addFocusListener(a);
		return field;
	}
	
	public void removeError (){
		
		GridBagLayout g = (GridBagLayout)getLayout();
		g.removeLayoutComponent(errorLabel);
		remove(errorLabel);
		repaint();
//		revalidate(); //TODO dont revalidate intentionally
		isErrorShowing=false;
	}
	
	public boolean isErrorShowing(){
		return isErrorShowing;
	}
	
	public void setText (String text){
		errorLabel.setText(text);
		isErrorShowing = true;
	}
	
	public void addErrorLabel(){
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridy=2;
		c.gridwidth=2;
		c.insets= new Insets(0,0,10,0);
					
		moveElementsDown(1, c.gridy);			
		add(errorLabel,c);
	}
			
	private void setErrorText (String errorText){
		errorLabel.setText(errorText);
		repaint();
		revalidate();
		isErrorShowing=false;
	}
	
	public String getUserName (){
		return loginField.getText();
	}
	
	
	public char [] getPassword(){
		return passField.getPassword();
	}
	
		

}
	