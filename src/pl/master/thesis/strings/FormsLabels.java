package pl.master.thesis.strings;

import java.util.HashMap;
import java.util.Map;

public class FormsLabels {
	
	public static String DZIEN = "Dzień";
	public static String MIESIAC = "Miesiąc";
	public static String ROK = "Rok";
	public static String IMIE = "Imię";
	public static String NAZWISKO = "Nazwisko";
	public static String DATA_URODZENIA = "Data urodzenia";
	public static String ADRES_EMAIL = "Adres e-mail";
	public static String NAZWA_UZYTKOWNIKA = "Nazwa użytkownika";
	public static String HASLO = "Hasło";
	public static String POTWIERDZ_HASLO = "Potwierdź hasło";
	public static String PYTANIE_POMOCNICZE = "Pytanie pomocnicze";
	public static String ODPOWIEDZ = "Odpowiedź";
	
	
	public static Map <String, String> defaultValues;
	
	public static Map <String, String> getDefaultValues(){
		
		if (defaultValues==null){
			defaultValues = new HashMap <String, String> ();
			defaultValues.put(IMIE, "Jacek");
			defaultValues.put(ADRES_EMAIL, "Spaaw@poczta.fm");
			defaultValues.put(NAZWA_UZYTKOWNIKA, "Kamilo123");
			defaultValues.put(NAZWISKO, "Nowak");
			defaultValues.put(DZIEN, "14");
			defaultValues.put(MIESIAC, "6");
			defaultValues.put(ROK, "1984");
			defaultValues.put(HASLO, "degdras11");
			defaultValues.put(POTWIERDZ_HASLO, "degdras11");
			defaultValues.put(PYTANIE_POMOCNICZE, "Ile masz zwierząt w domu");
			defaultValues.put(ODPOWIEDZ, "3");			
		}
		
		return defaultValues;
	}

}
