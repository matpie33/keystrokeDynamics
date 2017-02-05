package pl.master.thesis.panels;

import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JLabel;

import com.guimaker.row.RowMaker;

import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.guiElements.MyLabel;
import pl.master.thesis.listeners.ActionListeners;
import pl.master.thesis.others.ElementsMaker;
import pl.master.thesis.strings.Prompts;
import pl.master.thesis.strings.TypingStatisticsFormat;
import pl.master.thesis.timing.Timing;

public class PanelCongratulations extends BasicPanel  {
	private JLabel speed;
	private JLabel errors;
	private Timing time;

	public PanelCongratulations(MainWindow frame, Timing time) {
		super(frame);
		this.time=time;
		
		JLabel congrats = new MyLabel(Prompts.ACCOUNT_CREATED_SUCCESSFULLY_PROMPT);	
		speed = new MyLabel();
		errors = new MyLabel();
		JButton btnHome = ElementsMaker.createButton(Prompts.BTN_GO_HOME, 
				ActionListeners.createGoHomeListener(frame));
						
		panel.addRow(RowMaker.createUnfilledRow(GridBagConstraints.CENTER, congrats));
		panel.addRow(RowMaker.createUnfilledRow(GridBagConstraints.CENTER, speed));
		panel.addRow(RowMaker.createUnfilledRow(GridBagConstraints.CENTER, errors));
		panel.addRow(RowMaker.createUnfilledRow(GridBagConstraints.CENTER, btnHome));
	}
		
	public void update(){
		time.stopTimerAndCalculateSpeed();
		speed.setText(String.format(TypingStatisticsFormat.TYPING_SPEED, time.getMeanTypeSpeed()));
		errors.setText(String.format(TypingStatisticsFormat.NUMBER_OF_ERRORS, time.getErrors(), 
				time.getNumberOfTypedKeys()));
	}

}
