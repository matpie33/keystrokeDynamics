package pl.master.thesis.others;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import pl.master.thesis.buttons.MyLabel;

public class FieldsVerifier {
	
	public static String verifyFields (Map <JTextField, MyLabel> map){
		
		JTextField password = findTextField(Strings.HASLO,map);
		JTextField retypePassword = findTextField(Strings.POTWIERDZ_HASLO,map);
		JTextField email = findTextField(Strings.ADRES_EMAIL,map);
		JTextField days = findTextField(Strings.DZIEN,map);		
		JTextField months = findTextField(Strings.MIESIAC,map);
		JTextField years = findTextField(Strings.ROK,map);
		
		String errorText="";
		if (!DateVerifier.isInteger (days.getText()) || !DateVerifier.isInteger (months.getText()) || 
				!DateVerifier.isInteger(years.getText()) ){
			errorText+="Data nie zawiera liczby."+"\n";
		}
		else if (!DateVerifier.checkDate(days,months,years)){
			errorText+="Niepoprawna data"+"\n";
		}							
		
		List <String> list = findBadValues(map);
		
		if (!list.isEmpty()){
			String concatenated = concatenateFieldNames(list);
			errorText+=concatenated+"\n";
		}
		if (!email.getText().matches("[a-zA-Z]+@[a-zA-Z]+\\.[a-zA-Z]+")){
			errorText+="Niepoprawny adres e-mail. Musz¹ w nim wystêpowaæ znaki: @ oraz ."+"\n";
		}
		if (password.getText().length()<8 || password.getText().length()>30){
			errorText+="Has³o powinno zawieraæ od 8 do 30 znaków."+"\n";
		}
		if (!retypePassword.getText().equals(password.getText())){
			errorText+="Powtórzone has³o nie zgadza siê z poprzednim has³em."+"\n";
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
					!label.equals(Strings.DATA_URODZENIA)){
				list.add(entries.getValue().getText());
			}
		}
		System.out.println(list);
		return list;
	}
	
	private static String concatenateFieldNames (List <String> list){
		String e="Nie wype³niono ";
		if (list.size()==1)
			e+= "pola "+list.get(0)+"\n";	
		else{
			e+= "pól ";
				for (int i=0; i<list.size(); i++){
					if (i<list.size()-1)
						e+=list.get(i)+", ";
					else
						e+=list.get(i);
					
				}				
		}
		return e;
	}
	
	

}
