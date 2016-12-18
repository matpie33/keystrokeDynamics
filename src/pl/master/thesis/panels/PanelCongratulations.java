package pl.master.thesis.panels;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;

import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.guiElements.MyLabel;
import pl.master.thesis.strings.Prompts;

public class PanelCongratulations extends BasicPanel  {


	public PanelCongratulations(MainWindow frame) {
		super(frame);
		
		JLabel congrats = new MyLabel(Prompts.ACCOUNT_CREATED_SUCCESSFULLY_PROMPT);		
		panel.createRow(GridBagConstraints.CENTER,1, congrats);
	}

}
