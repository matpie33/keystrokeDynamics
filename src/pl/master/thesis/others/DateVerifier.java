package pl.master.thesis.others;

import java.util.Calendar;
import java.util.List;

import javax.swing.JTextField;

public class DateVerifier {

	public static boolean isLeapYear(String selectedItem) {
		try{
			int year=Integer.valueOf(selectedItem); 
			return ((year%4==0  && year%100!=0) || (year%400==0));
		}
		catch (NumberFormatException nex){
			return false;
		}
					
				
	}
	
	public static boolean isInteger (String text){
		try {
			Integer.parseInt(text);
			return true;
			
		}
		catch (NumberFormatException ex){
			return false;
		}
	}
	
	public static boolean checkDate(JTextField days, JTextField months,
			JTextField years) {
		
		String day = days.getText();
		String month = months.getText();
		String year = years.getText();
		Calendar c = Calendar.getInstance();
		c.getTime();
		int u = c.get(Calendar.YEAR);
		int minAge=10;
		int maxAge=100;
		
		int intDay = Integer.parseInt(day);
		int intMonth = Integer.parseInt(month);
		int intYear = Integer.parseInt(year);
		
		if (intMonth>12 || intYear<u-maxAge || intYear>u-minAge){
			System.out.println((intYear<u-maxAge) +" "+ (intYear>u-minAge));
			
			return false;}
		
		if (month.matches("1|3|5|7|8|10|12") ){
			System.out.println("dni 31");
			return intDay <= 31;
		}
		else if (month.equals("2")){
			System.out.println("luty");
			return (isLeapYear(year) && intDay<=29 || !isLeapYear(year) && intDay<=28);					
		}
		else
			System.out.println("dni 30");
			return intDay<=30;
		
	}
	
}
