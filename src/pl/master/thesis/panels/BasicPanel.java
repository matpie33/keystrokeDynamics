package pl.master.thesis.panels;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.guimaker.panels.MainPanel;

import pl.master.thesis.dialogs.MyDialog;
import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.guiElements.ErrorLabel;
import pl.master.thesis.guiElements.MyButton;
import pl.master.thesis.listeners.ActionListeners;
import pl.master.thesis.others.ElementsMaker;
import pl.master.thesis.others.MyColors;
import pl.master.thesis.strings.Prompts;

public class BasicPanel {
	
	private static final long serialVersionUID = 1L;
	protected MyButton btnContinue;
	protected MyButton btnBack;
	protected MainWindow frame;
	protected Insets fieldInsets=new Insets(0,10,5,10);
	protected Insets verticalInsets=new Insets(0,20,5,20);	
	protected Insets labelInsets = new Insets (0,20,5,5); 

	protected boolean isErrorShowing;
	protected JLabel errorLabel;
	protected MainPanel panel;	
	private MyDialog dialog;
	
	public BasicPanel (MainWindow frame){		
		this(frame, MyColors.LIGHT_BLUE);
		errorLabel = new ErrorLabel();
		isErrorShowing=false;
		this.frame=frame;	
		btnContinue = ElementsMaker.createButton(Prompts.BTN_CONTINUE, 
				ActionListeners.createNextPanelListener(frame));
		btnBack = ElementsMaker.createButton(Prompts.BTN_GO_BACK, 
				ActionListeners.createPreviousPanelListener(frame));			
	}
	
	public BasicPanel (MainWindow frame, Color color ){
		panel = new MainPanel (color);
	}
		
	public MainWindow getParentFrame(){
		return frame;
	}	
	
	public JPanel getPanel (){
		return panel.getPanel();
	}
	
	public boolean isErrorShowing(){
		return isErrorShowing;
	}
	
	public void removeError(){
		isErrorShowing=false;
		errorLabel.setText("");
	}
	
	public void showShortMessageDialog(String text){
		dialog = new MyDialog(this);
		dialog.createShortMessageDialog(text);
		dialog.setVisible(true);
	}
	
	public void showLongMessageDialog(String text){
		dialog = new MyDialog(this);
		dialog.createLongMessageDialog(text);
		dialog.setVisible(true);
	}
	
	public void showConnectingDialog(){
		dialog = new MyDialog(this);
		dialog.createWaitingDialog();
		dialog.setVisible(true);
	}
	
	public void closeDialog(){
		dialog.dispose();
	}
	
}
