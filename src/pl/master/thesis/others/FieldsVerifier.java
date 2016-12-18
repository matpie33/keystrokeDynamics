package pl.master.thesis.others;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import pl.master.thesis.guiElements.MyLabel;
import pl.master.thesis.strings.FormsLabels;
import pl.master.thesis.strings.InputVerifierMessages;

public class FieldsVerifier {
	
	public static String verifyFields (Map <JTextField, MyLabel> map){
		
		JTextField password = findTextField(FormsLabels.HASLO,map);
		JTextField retypePassword = findTextField(FormsLabels.POTWIERDZ_HASLO,map);
		JTextField email = findTextField(FormsLabels.ADRES_EMAIL,map);
		JTextField days = findTextField(FormsLabels.DZIEN,map);		
		JTextField months = findTextField(FormsLabels.MIESIAC,map);
		JTextField years = findTextField(FormsLabels.ROK,map);
		
		String errorText="";
		if (!DateVerifier.isInteger (days.getText()) || !DateVerifier.isInteger (months.getText()) || 
				!DateVerifier.isInteger(years.getText()) ){
			errorText+=InputVerifierMessages.DATE_IS_NOT_A_NUMBER+"\n";
		}
		else if (!DateVerifier.checkDate(days,months,years)){
			errorText+=InputVerifierMessages.DATE_INCORRECT_VALUE+"\n";
		}							
		
		List <String> list = findBadValues(map);
		
		if (!list.isEmpty()){
			String concatenated = concatenateFieldNames(list);
			errorText+=concatenated+"\n";
		}
		if (!email.getText().matches("[a-zA-Z]+@[a-zA-Z]+\\.[a-zA-Z]+")){
			errorText+=InputVerifierMessages.EMAIL_INCORRECT+"\n";
		}
		if (password.getText().length()<8 || password.getText().length()>30){
			errorText+=InputVerifierMessages.PASSWORD_INCORRECT+"\n";
		}
		if (!retypePassword.getText().equals(password.getText())){
			errorText+=InputVerifierMessages.REPEATED_PASSWORD_INCORRECT+"\n";
		}
		
		return errorText;
		
	}

	public static JTextField findTextField(String text, Map <JTextField, MyLabel> map) {
		
		for (Map.Entry<JTextField, MyLabel> entries: map.entrySet()){
			if (entries.getValue().getText().equals(text)){
				return entries.getKey();
			}
		}
		return new JTextField();

	}
	
	private static List <String> findBadValues (Map <JTextField,MyLabel> map){
		List <String> list = new ArrayList <String> ();
		for (Map.Entry<JTextField, MyLabel> entries: map.entrySet()){
			String label = entries.getValue().getText();
			String fieldText=entries.getKey().getText();
			if (fieldText.equals(label) && 
					!label.equals(FormsLabels.DATA_URODZENIA)){
				list.add(entries.getValue().getText());
			}
		}
		System.out.println(list);
		return list;
	}
	
	private static String concatenateFieldNames (List <String> list){
		String e=InputVerifierMessages.INPUT_NOT_PROVIDED;
		if (list.size()==1)
			e+= InputVerifierMessages.FIELD_SINGULAR + " " + list.get(0);	
		else{
			e+= InputVerifierMessages.FIELD_PLURAR+": ";
				for (int i=0; i<list.size(); i++){
					if (i<list.size()-1)
						e+=list.get(i)+", ";
					else
						e+=list.get(i);
					
				}				
		}
		e+=".";
		return e;
	}
	
	

}
