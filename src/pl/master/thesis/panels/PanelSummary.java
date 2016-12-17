package pl.master.thesis.panels;

import java.awt.GridBagConstraints;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JTextField;

import pl.master.thesis.buttons.MyLabel;
import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.listeners.ActionListeners;
import pl.master.thesis.others.ElementsMaker;
import pl.master.thesis.others.FieldsVerifier;
import pl.master.thesis.others.MainPanel;
import pl.master.thesis.others.MyColors;
import pl.master.thesis.strings.FormsLabels;
import pl.master.thesis.strings.Prompts;

public class PanelSummary extends BasicPanel {
	
	private static final long serialVersionUID = 3706383803572619289L;
	private Map <JTextField,MyLabel> hmap;
	private MainPanel panel;
	
	
	

	public PanelSummary(MainWindow fra, Map <JTextField, MyLabel> map){		
		super(fra);
		panel = new MainPanel(MyColors.DARK_BLUE);
		hmap=map;
		c.insets=verticalInsets;
		
//		connectionListener= new ConnectionListener (this, new con());		
	}
		
	public void showFieldsValues(){
	
		panel.getPanel().removeAll();	
		MyLabel title = ElementsMaker.createLabel (Prompts.TITLE_SUMMARY);		
		panel.createRow(GridBagConstraints.CENTER,1,title);		
		addValuesFromTextFieldsAndLabels(hmap);
		JButton btnConnect = ElementsMaker.createButton(Prompts.BTN_CONTINUE, 
				ActionListeners.createListenerConnect(this));
		panel.createRow(GridBagConstraints.CENTER, 1,btnBack,btnConnect);

	}	

	private void addValuesFromTextFieldsAndLabels(Map<JTextField, MyLabel> hmap) {
		System.out.println(hmap.size());
		for (Map.Entry<JTextField, MyLabel> entry: hmap.entrySet()){
			
			MyLabel label = entry.getValue();
			JTextField textField = entry.getKey();
			
			if (label.getText().matches(FormsLabels.DZIEN+"|"+
					FormsLabels.MIESIAC+"|"+FormsLabels.ROK+"|"+FormsLabels.HASLO+"|"+FormsLabels.POTWIERDZ_HASLO)) continue;
						
			String text="";
			if (label.getText().equals(FormsLabels.DATA_URODZENIA)){
				JTextField day = FieldsVerifier.findTextField(FormsLabels.DZIEN, hmap);
				JTextField month = FieldsVerifier.findTextField(FormsLabels.MIESIAC, hmap);
				JTextField year = FieldsVerifier.findTextField(FormsLabels.ROK, hmap);
				text=day.getText()+"-"+month.getText()+"-"+year.getText();				
			}
			else text=textField.getText();
			
			MyLabel newL = new MyLabel (label.getText()+": "+text);
			panel.createRow(newL);
			
		}
		
	}
	
	public Map <JTextField, MyLabel> getMap (){
		return hmap;
	}
	
	public MainPanel getPanel (){
		return panel;
	}
		
	
}


