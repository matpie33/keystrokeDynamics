package pl.master.thesis.panels;

import java.awt.GridBagConstraints;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JTextField;

import com.guimaker.row.RowMaker;

import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.guiElements.MyLabel;
import pl.master.thesis.helpers.PasswordMasker;
import pl.master.thesis.listeners.ActionListeners;
import pl.master.thesis.others.ElementsMaker;
import pl.master.thesis.others.FieldsVerifier;
import pl.master.thesis.strings.FormsLabels;
import pl.master.thesis.strings.Prompts;

public class PanelSummary extends BasicPanel {
	
	private static final long serialVersionUID = 3706383803572619289L;
			
	private final String DATE_SEPARATOR="-";
	private final String LABEL_FROM_VALUE_SEPARATOR=": ";
	
	public PanelSummary(MainWindow fra){		
		super(fra);
	}
		
	public void showFieldsValues(Map <JTextField, MyLabel> hmap){

		panel.getPanel().removeAll();	
		MyLabel title = ElementsMaker.createLabel (Prompts.TITLE_SUMMARY);	
		JButton btnConnect = ElementsMaker.createButton(Prompts.BTN_CONTINUE, 
				ActionListeners.createListenerConnect(this, hmap));
		
		panel.addRow(RowMaker.createUnfilledRow(GridBagConstraints.CENTER, title));
		addValuesFromTextFieldsAndLabels(hmap);		
		panel.addRow(RowMaker.createUnfilledRow(GridBagConstraints.CENTER, btnBack,btnConnect));

	}	

	private void addValuesFromTextFieldsAndLabels(Map<JTextField, MyLabel> hmap) {
		for (Map.Entry<JTextField, MyLabel> entry: hmap.entrySet()){
			
			MyLabel label = entry.getValue();
			JTextField textField = entry.getKey();
			
			if (label.getText().matches(FormsLabels.DAY+"|"+
					FormsLabels.MONTH+"|"+FormsLabels.YEAR+"|"+
					FormsLabels.REPEAT_PASSWORD)) continue;
			
			String text="";			
			
			if (label.getText().equals(FormsLabels.DATE_OF_BIRTH)){
				JTextField day = FieldsVerifier.findTextField(FormsLabels.DAY, hmap);
				JTextField month = FieldsVerifier.findTextField(FormsLabels.MONTH, hmap);
				JTextField year = FieldsVerifier.findTextField(FormsLabels.YEAR, hmap);
				text=day.getText()+DATE_SEPARATOR+month.getText()+DATE_SEPARATOR+year.getText();				
			}
			else if (label.getText().equals(FormsLabels.PASSWORD)){
				text=PasswordMasker.maskPassword();
			}
			else text=textField.getText();
			
			MyLabel newLabel = new MyLabel (label.getText()+LABEL_FROM_VALUE_SEPARATOR+text);
			panel.addRow(RowMaker.createHorizontallyFilledRow(newLabel));
			
		}
		
	}
	
				
	
}


