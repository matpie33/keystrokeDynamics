package pl.masater.thesis.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingWorker;

import pl.master.thesis.database.ConnectionSwingWorker;
import pl.master.thesis.dialogs.WaitingDialog;
import pl.master.thesis.panels.BasicPanel;

public class ConnectionListener implements ActionListener{
	
	private WaitingDialog waitingDialog;
	private SwingWorker swingWorker;
	private BasicPanel panel;
	
	public ConnectionListener (BasicPanel panel, SwingWorker s){
		swingWorker=s;
		this.panel=panel;
		
		System.out.println("swing worker working");
	}
	
	@Override
	public void actionPerformed (ActionEvent e){
		
		waitingDialog = new WaitingDialog(panel);	
		swingWorker.execute();
		waitingDialog.setVisible(true);	
	}
	
	public void close(){
		waitingDialog.dispose();
	}
	
	public void setSwingWorker (SwingWorker s){
		swingWorker=s;
	}
	
	public void setText (String text){
		waitingDialog.setLabelText(text);
	}
	
	public void removeGif(){
		waitingDialog.removeGif();
	}
	
	public void addButton (JButton button){
		waitingDialog.addButton(button);
	}

}
