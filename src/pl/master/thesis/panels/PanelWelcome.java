package pl.master.thesis.panels;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;

import com.guimaker.row.RowMaker;

import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.guiElements.MyButton;
import pl.master.thesis.guiElements.MyLabel;
import pl.master.thesis.listeners.KeyListeners;
import pl.master.thesis.others.ElementsMaker;
import pl.master.thesis.strings.Prompts;
import pl.master.thesis.swingWorkers.UserCheckWorker;

public class PanelWelcome extends BasicPanel {
	
	private static final long serialVersionUID = 1L;
	private JPasswordField passField;
	private JTextField loginField;	
	
	public PanelWelcome (final MainWindow frame) {
		
		super(frame);
		JTextArea hello = ElementsMaker.createWelcomeMessage(Prompts.WELCOME_PROMPT);
		
		MyLabel loginLabel = ElementsMaker.createLabel (Prompts.LABEL_LOGIN);
		MyLabel passLabel = ElementsMaker.createLabel(Prompts.PASSWORD_LABEL);
		
		loginField = new JTextField(15);		
		passField = new JPasswordField(15);
		ActionListener action = createButtonCreate();
		KeyListeners.addKeyBindings(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), (AbstractAction)action,
				passField, loginField);
		KeyListeners.addListeners(KeyListeners.removeErrorWhenTyped(this), loginField, passField);
			
		MyButton btnCreate = ElementsMaker.createButton (Prompts.BTN_LOGIN, action);
		
		btnContinue.setText(Prompts.BTN_CREATE_ACCOUNT);	
			
		panel.addRow(RowMaker.createHorizontallyFilledRow( hello));
		panel.addRow(RowMaker.createUnfilledRow(GridBagConstraints.WEST, btnContinue));
		panel.addRow(RowMaker.createHorizontallyFilledRow(loginLabel, loginField).
				fillHorizontallySomeElements(loginField));
		panel.addRow(RowMaker.createHorizontallyFilledRow(passLabel, passField).
				fillHorizontallySomeElements(passField));
		panel.addRow(RowMaker.createUnfilledRow(GridBagConstraints.CENTER, btnCreate));
		panel.addRow(RowMaker.createUnfilledRow(GridBagConstraints.EAST, errorLabel));

	}
	
	
	private AbstractAction createButtonCreate(){
		
		final PanelWelcome panel = this;
	
		return new AbstractAction (){
			@Override
			public void actionPerformed (ActionEvent e){
				
				SwingWorker s = new UserCheckWorker(panel);
				s.execute();
				panel.showConnectingDialog();
			}
		};
		
	}	
	
	
	
		
	public void addErrorLabel(){		
		errorLabel.setVisible(true);
	}
			
	public void setErrorText (String errorText){
		errorLabel.setText(errorText);
		isErrorShowing=true;
	}
	
	public String getUserName (){
		return loginField.getText();
	}
	
	
	public char [] getPassword(){
		return passField.getPassword();
	}
	
	
	
		

}
	