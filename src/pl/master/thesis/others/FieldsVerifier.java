package pl.master.thesis.others;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JTextField;

import pl.master.thesis.guiElements.MyLabel;
import pl.master.thesis.strings.FormsLabels;
import pl.master.thesis.strings.WrongInputMessages;

public class FieldsVerifier {

	public static String verifyFields(Map<JTextField, MyLabel> map) {

		JTextField password = findTextField(FormsLabels.PASSWORD, map);
		JTextField retypePassword = findTextField(FormsLabels.REPEAT_PASSWORD, map);
		JTextField email = findTextField(FormsLabels.EMAIL_ADDRESS, map);
		JTextField days = findTextField(FormsLabels.DAY, map);
		JTextField months = findTextField(FormsLabels.MONTH, map);
		JTextField years = findTextField(FormsLabels.YEAR, map);

		String errorText = "";
		if (!isInteger(days.getText()) || !isInteger(months.getText())
				|| !isInteger(years.getText())) {
			errorText += WrongInputMessages.DATE_IS_NOT_A_NUMBER + "\n";
		}
		else if (!DateVerifier.checkDate(days, months, years)) {
			errorText += DateVerifier.getErrorMessage() + "\n";
		}

		List<String> list = findEmptyValues(map);

		if (!list.isEmpty()) {
			String concatenated = concatenateFieldNames(list);
			errorText += concatenated + "\n";
		}
		if (!email.getText()
				.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\."
						+ "[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")) {
			errorText += WrongInputMessages.EMAIL_INCORRECT + "\n";
		}
		if (password.getText().length() < 8 || password.getText().length() > 30) {
			errorText += WrongInputMessages.PASSWORD_INCORRECT + "\n";
		}
		if (!retypePassword.getText().equals(password.getText())) {
			errorText += WrongInputMessages.REPEATED_PASSWORD_INCORRECT + "\n";
		}

		return errorText;

	}

	private static boolean isInteger(String text) {
		try {
			Integer.parseInt(text);
			return true;

		}
		catch (NumberFormatException ex) {
			return false;
		}
	}

	public static JTextField findTextField(String text, Map<JTextField, MyLabel> map) {

		for (Map.Entry<JTextField, MyLabel> entries : map.entrySet()) {
			if (entries.getValue().getText().equals(text)) {
				return entries.getKey();
			}
		}
		return new JTextField();

	}

	private static List<String> findEmptyValues(Map<JTextField, MyLabel> map) {
		List<String> list = new ArrayList<String>();
		for (Map.Entry<JTextField, MyLabel> entries : map.entrySet()) {
			String label = entries.getValue().getText();
			String fieldText = entries.getKey().getText();
			if (fieldText.equals(label) && !label.equals(FormsLabels.DATE_OF_BIRTH)) {
				list.add(entries.getValue().getText());
			}
		}
		return list;
	}

	private static String concatenateFieldNames(List<String> list) {
		String e = WrongInputMessages.INPUT_NOT_PROVIDED;
		if (list.size() == 1)
			e += WrongInputMessages.FIELD_SINGULAR + " " + list.get(0);
		else {
			e += WrongInputMessages.FIELD_PLURAR + ": ";
			for (int i = 0; i < list.size(); i++) {
				if (i < list.size() - 1)
					e += list.get(i) + ", ";
				else
					e += list.get(i);

			}
		}
		e += ".";
		return e;
	}

}
