package pl.master.thesis.others;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

import pl.master.thesis.guiElements.MyLabel;
import pl.master.thesis.strings.FormsLabels;

public class FieldsInitializer {

	private List <String> defaultValues;
	Map <JTextField,MyLabel> hmap;
	
	public FieldsInitializer(){
		createDefaultValues();
		createMapWithTextFieldsAndLabels();
	}
	
	private List <String> createDefaultValues(){
		defaultValues = new ArrayList <String> ();
		defaultValues.add(FormsLabels.FIRST_NAME);
		defaultValues.add(FormsLabels.LAST_NAME);
		defaultValues.add(FormsLabels.DATE_OF_BIRTH);
		defaultValues.add(FormsLabels.DAY);
		defaultValues.add(FormsLabels.MONTH);
		defaultValues.add(FormsLabels.YEAR);
		defaultValues.add(FormsLabels.EMAIL_ADDRESS);
		defaultValues.add(FormsLabels.USERNAME);
		defaultValues.add(FormsLabels.PASSWORD);
		defaultValues.add(FormsLabels.REPEAT_PASSWORD);
		defaultValues.add(FormsLabels.RECOVERY_QUESTION);
		defaultValues.add(FormsLabels.ANSWER);
		return defaultValues;
	}	
	
	private void createMapWithTextFieldsAndLabels (){
		
		Map <JTextField,MyLabel> hmap = new LinkedHashMap <JTextField,MyLabel> (); 
		int maxFieldCharacters=15;
		int maxDateFieldCharacters = 4;
		
		for (int i=0; i<defaultValues.size();i++){		
			String str = defaultValues.get(i);
			JTextField textField;
			MyLabel label = new MyLabel (str);
			if (str.matches(FormsLabels.PASSWORD+"|"+FormsLabels.REPEAT_PASSWORD))
				textField = new JPasswordField ("", maxFieldCharacters);			
			else if (str.matches(FormsLabels.DAY+"|"+FormsLabels.MONTH+"|"+FormsLabels.YEAR))
				textField = new JTextField ("", maxDateFieldCharacters);				
			else textField = new JTextField ("", maxFieldCharacters);
			hmap.put(textField, label);
		}
		this.hmap = hmap;
		initializeFieldsWithDefaultValues();
		
	}
	
	public void initializeFieldsWithDefaultValues(){
			
		int i=0;
		for (JTextField field: hmap.keySet()){
			String defaultValue = defaultValues.get(i);
			field.setText(defaultValue);
			i++;
		}
		
	}
	
	public Map <JTextField, MyLabel> getFieldsToLabelMap(){
		return hmap;		
	}
	
}
