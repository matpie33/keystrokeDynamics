package pl.master.thesis.panels;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import pl.master.thesis.buttons.MyLabel;
import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.strings.Prompts;

public class PanelCongratulations extends BasicPanel  {


	public PanelCongratulations(MainWindow fram) {
		super(fram);
		c.anchor=GridBagConstraints.CENTER;
		
		JLabel congrats = new MyLabel(Prompts.ACCOUNT_CREATED_SUCCESSFULLY_PROMPT);		
		add(congrats,c);
		
		c.gridy=1;
		add(btnContinue,c);
		
		
//		btnContinue.addActionListener(new ActionListener (){
//			@Override
//			public void actionPerformed (ActionEvent e){
//				frame.gotoPanel(MainWindow.welcomePanel);
//			}
//		});
	}

}
