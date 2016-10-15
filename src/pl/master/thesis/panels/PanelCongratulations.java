package pl.master.thesis.panels;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import pl.master.thesis.buttons.MyLabel;
import pl.master.thesis.frame.MainWindow;

public class PanelCongratulations extends BasicPanel  {

	private String string = "Gratulacje, pomyœlnie za³o¿ono konto.";

	public PanelCongratulations(MainWindow fram) {
		super(fram);
		c.anchor=GridBagConstraints.CENTER;
		
		JLabel congrats = new MyLabel(string);		
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
