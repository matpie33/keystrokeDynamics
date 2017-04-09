package pl.master.thesis.others;

import java.util.Calendar;

import javax.swing.JTextField;

import pl.master.thesis.strings.DateErrorMessages;

public class DateVerifier {

	private static final int MIN_AGE = 10;
	private static final int MAX_AGE = 100;
	private static String error;

	public static boolean isLeapYear(String selectedItem) {
		try {
			int year = Integer.valueOf(selectedItem);
			return ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0));
		}
		catch (NumberFormatException nex) {
			return false;
		}
	}

	public static boolean checkDate(JTextField days, JTextField months, JTextField years) {

		String day = days.getText();
		String month = months.getText();
		String year = years.getText();
		Calendar c = Calendar.getInstance();
		c.getTime();
		int u = c.get(Calendar.YEAR);

		int intDay = Integer.parseInt(day);
		int intMonth = Integer.parseInt(month);
		int intYear = Integer.parseInt(year);

		if (intMonth > 12) {
			error = DateErrorMessages.MONTH_ABOVE_12;
			return false;
		}
		if (intYear > u) {
			error = DateErrorMessages.BIRTH_YEAR_ABOVE_CURRENT_YEAR;
			return false;
		}
		if (u - intYear > MAX_AGE) {
			error = String.format(DateErrorMessages.INCORRECT_AGE, u - intYear);
			return false;
		}

		if (u - intYear < MIN_AGE) {
			error = String.format(DateErrorMessages.INCORRECT_AGE, u - intYear);
			return false;
		}

		if (validateFor31DayMonth(month, intDay) && validateForFebruary(month, intDay, year)
				&& validateMonthsNot31DayAndNotFebruary(intDay)) {
			return true;
		}
		else
			return false;

	}

	private static boolean validateFor31DayMonth(String month, int intDay) {
		if (month.matches("1|3|5|7|8|10|12")) {
			if (intDay > 31) {
				error = DateErrorMessages.MAX_DAY_SHOULD_BE_31;
			}
			return intDay <= 31;

		}
		return true;
	}

	private static boolean validateForFebruary(String month, int intDay, String year) {
		if (month.equals("2")) {
			if (isLeapYear(year)) {
				if (intDay > 29) {
					error = DateErrorMessages.MAX_DAY_SHOULD_BE_29;
					return false;
				}
			}
			else {
				if (intDay > 28) {
					error = DateErrorMessages.MAX_DAY_SHOULD_BE_28;
					return false;
				}
			}
		}
		return true;
	}

	private static boolean validateMonthsNot31DayAndNotFebruary(int intDay) {
		if (intDay > 30) {
			error = DateErrorMessages.MAX_DAY_SHOULD_BE_30;
		}
		return intDay <= 30;
	}

	public static String getErrorMessage() {
		return error;
	}

}
