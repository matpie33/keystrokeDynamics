package pl.master.thesis.panels;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.guiElements.MyButton;
import pl.master.thesis.guiElements.MyLabel;
import pl.master.thesis.listeners.ActionListeners;
import pl.master.thesis.listeners.FocusListeners;
import pl.master.thesis.others.ElementsMaker;
import pl.master.thesis.strings.FormsLabels;
import pl.master.thesis.strings.Prompts;

public class PanelData extends BasicPanel{

	private Map <JTextField, MyLabel> hmap;	
	
	public PanelData(final MainWindow frame, final PanelSummary summaryPanel, final Map <JTextField,MyLabel> hmap, 
			final List <String> strings){
		
		super(frame);	
		this.hmap = hmap;
		List <JTextField> fields = new ArrayList <JTextField>();
		fields.addAll(hmap.keySet());	
		
		JPanel datePanel = createDatePanel();	
		MyLabel title = ElementsMaker.createLabel (Prompts.TITLE_DATA);
		JButton exampleInput = ElementsMaker.createButton("", 
				ActionListeners.createExampleInputListener(hmap));
			 //TODO REMOVE IT WHEN APPLICATION IS FINISHED	
			
		MyButton btnContinue = ElementsMaker.createButton(Prompts.BTN_CONTINUE, 
				ActionListeners.
//				createNextPanelListener(getParentFrame()));
				createListenerGoToNextPanel(this, summaryPanel));
				
		for (int i=0; i<fields.size();i++){
			JTextField field = fields.get(i);
			field.addFocusListener(FocusListeners.createListenerDefaultValueIfEmpty(field));	
		}
	
		panel.createRow(GridBagConstraints.WEST, 1, exampleInput);
		panel.createRow(GridBagConstraints.CENTER, 1, title);	
		addTextFieldsAndLabelsFromMap (hmap, datePanel);	
		panel.createRowOn2Sides(btnBack, btnContinue);
	
	}
	
	private JPanel createDatePanel (){
		
		JPanel datePanel = new JPanel();
		datePanel.setOpaque(false);
		datePanel.setLayout(new BorderLayout(10,0));
		
		JTextField days = ElementsMaker.createTextField(FormsLabels.DZIEN, 2);
		JTextField months = ElementsMaker.createTextField(FormsLabels.MIESIAC, 2);
		JTextField years = ElementsMaker.createTextField(FormsLabels.ROK, 4);
			
		datePanel.add(years,BorderLayout.EAST);
		datePanel.add(months,BorderLayout.CENTER);			
		datePanel.add(days,BorderLayout.WEST);
		return datePanel;
		
	}	
				
	private void addTextFieldsAndLabelsFromMap (Map <JTextField, MyLabel> hmap, JPanel datePanel){
		for (Map.Entry<JTextField, MyLabel> set: hmap.entrySet()) {
			
			MyLabel label = set.getValue();
			JTextField field = set.getKey();
			if (label.getText().equals(FormsLabels.DZIEN) || label.getText().equals(FormsLabels.MIESIAC) || 
					label.getText().equals(FormsLabels.ROK))
				continue;			
			
			if (label.getText().equals(FormsLabels.DATA_URODZENIA))								
				panel.createRow(label,datePanel);		
			else	panel.createRow(label, field);			
					
		}
	}		
		
}
