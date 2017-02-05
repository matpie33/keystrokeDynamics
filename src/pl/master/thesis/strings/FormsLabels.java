package pl.master.thesis.strings;

import java.util.HashMap;
import java.util.Map;

public class FormsLabels {
	
	public static final String DAY = "Dzień";
	public static final String MONTH = "Miesiąc";
	public static final String YEAR = "Rok";
	public static final String FIRST_NAME = "Imię";
	public static final String LAST_NAME = "Nazwisko";
	public static final String DATE_OF_BIRTH = "Data urodzenia";
	public static final String EMAIL_ADDRESS = "Adres e-mail";
	public static final String USERNAME = "Nazwa użytkownika";
	public static final String PASSWORD = "Hasło";
	public static final String REPEAT_PASSWORD = "Potwierdź hasło";
	public static final String RECOVERY_QUESTION = "Pytanie pomocnicze";
	public static final String ANSWER = "Odpowiedź";
	
	
	public static Map <String, String> defaultValues;
	
	public static Map <String, String> getDefaultValues(){
		
		if (defaultValues==null){
			defaultValues = new HashMap <String, String> ();
			defaultValues.put(FIRST_NAME, "Jacek");
			defaultValues.put(EMAIL_ADDRESS, "Spaaw@poczta.fm");
			defaultValues.put(USERNAME, "Kamilo123");
			defaultValues.put(LAST_NAME, "Nowak");
			defaultValues.put(DAY, "14");
			defaultValues.put(MONTH, "6");
			defaultValues.put(YEAR, "1984");
			defaultValues.put(PASSWORD, "degdras11");
			defaultValues.put(REPEAT_PASSWORD, "degdras11");
			defaultValues.put(RECOVERY_QUESTION, "Ile masz zwierząt w domu");
			defaultValues.put(ANSWER, "3");			
		}
		
		return defaultValues;
	}

}
