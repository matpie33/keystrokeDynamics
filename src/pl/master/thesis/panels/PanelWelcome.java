package pl.master.thesis.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import pl.master.thesis.buttons.MyButton;
import pl.master.thesis.buttons.MyLabel;
import pl.master.thesis.dialogs.MyDialog;
import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.others.ElementsMaker;
import pl.master.thesis.others.MainPanel;
import pl.master.thesis.others.MyColors;
import pl.master.thesis.strings.Prompts;
import pl.master.thesis.swingWorkers.UserCheckWorker;

public class PanelWelcome extends BasicPanel {
	
	private static final long serialVersionUID = 1L;
	private JPasswordField passField;
	private JTextField loginField;	
	private MainPanel panel;
	
	public PanelWelcome (final MainWindow frame) {
		
		super(frame);
		panel = new MainPanel(MyColors.DARK_GREEN);
		JTextArea hello = ElementsMaker.createWelcomeMessage(Prompts.WELCOME_PROMPT);
		
		MyLabel loginLabel = ElementsMaker.createLabel (Prompts.LABEL_LOGIN);
		MyLabel passLabel = ElementsMaker.createLabel(Prompts.PASSWORD_LABEL);
		
		loginField = new JTextField(15);		
		passField = new JPasswordField(15);
		btnContinue.setText(Prompts.BTN_CREATE_ACCOUNT);		
		MyButton btnCreate = createButtonCreate();
			
		panel.createRow(hello);
		panel.createRow(GridBagConstraints.WEST,1,btnContinue);
		panel.createRow(loginLabel,loginField);
		panel.createRow(passLabel,passField);
		panel.createRow(GridBagConstraints.EAST,1,btnCreate);

	}
	
	
	private MyButton createButtonCreate(){
		MyButton btnCreate = new MyButton (Prompts.BTN_LOGIN);
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
	
	
	public void addErrorLabel(){
		panel.createRow(GridBagConstraints.CENTER,1,errorLabel);
	}
			
	public void setErrorText (String errorText){
		errorLabel.setText(errorText);
		repaint();
		revalidate();
		isErrorShowing=true;
	}
	
	public String getUserName (){
		return loginField.getText();
	}
	
	
	public char [] getPassword(){
		return passField.getPassword();
	}
	
	public MainPanel getPanel (){
		return panel;
	}
	
		

}
	