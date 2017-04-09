package pl.master.thesis.others;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

import pl.master.thesis.guiElements.MyLabel;
import pl.master.thesis.keyEventHandler.KeyEventHandler;
import pl.master.thesis.keyTypingObjects.WordType;
import pl.master.thesis.listeners.MouseListeners;
import pl.master.thesis.strings.FormsLabels;

public class FieldsInitializer {

	private List<String> defaultValues;
	private Map<JTextField, MyLabel> hmap;
	private final String OR = "|";
	private Map<JTextField, MyLabel> welcomeHmap;

	public FieldsInitializer(KeyEventHandler handler) {
		createDefaultValues();
		createMapWithTextFieldsAndLabels(handler);
	}

	private List<String> createDefaultValues() {
		defaultValues = new ArrayList<String>();
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

	private void createMapWithTextFieldsAndLabels(KeyEventHandler handler) {

		hmap = new LinkedHashMap<JTextField, MyLabel>();
		int maxFieldCharacters = 15;
		int maxDateFieldCharacters = 4;

		for (int i = 0; i < defaultValues.size(); i++) {
			String str = defaultValues.get(i);
			JTextField textField;
			MyLabel label = new MyLabel(str);
			if (str.matches(FormsLabels.PASSWORD + "|" + FormsLabels.REPEAT_PASSWORD))
				textField = new JPasswordField("", maxFieldCharacters);
			else if (str
					.matches(FormsLabels.DAY + "|" + FormsLabels.MONTH + "|" + FormsLabels.YEAR))
				textField = new JTextField("", maxDateFieldCharacters);
			else
				textField = new JTextField("", maxFieldCharacters);
			textField.addMouseListener(MouseListeners.notStartedWithTabKeyIfClicked(handler));
			hmap.put(textField, label);
		}
		welcomeHmap = new LinkedHashMap<JTextField, MyLabel>();
		welcomeHmap.put(new JTextField(15), new MyLabel(FormsLabels.LOGIN));
		welcomeHmap.put(new JPasswordField(15), new MyLabel(FormsLabels.PASSWORD));
		initializeFieldsWithDefaultValues();

	}

	public void initializeFieldsWithDefaultValues() {

		int i = 0;
		for (JTextField field : hmap.keySet()) {
			String defaultValue = defaultValues.get(i);
			field.setText(defaultValue);
			i++;
		}

	}

	public void initializeWelcomePanelFields() {
		for (JTextField field : welcomeHmap.keySet()) {
			field.setText("");
		}
	}

	public Map<JTextField, MyLabel> getFieldsToLabelMap() {
		return hmap;
	}

	public Map<JTextField, MyLabel> getWelcomeFieldsToLabelMap() {
		return welcomeHmap;
	}

	public WordType getTextFieldType(JTextField field) {
		String labelsText = "";
		if (hmap.containsKey(field)) {
			labelsText = hmap.get(field).getText();
		}
		else if (welcomeHmap.containsKey(field)) {
			labelsText = welcomeHmap.get(field).getText();
		}

		if (labelsText.matches(FormsLabels.PASSWORD + "|" + FormsLabels.REPEAT_PASSWORD)) {
			return WordType.PASSWORD;
		}
		else if (labelsText
				.matches(FormsLabels.DAY + OR + FormsLabels.MONTH + OR + FormsLabels.YEAR)) {
			return WordType.DATE;
		}
		else if (labelsText.equals(FormsLabels.EMAIL_ADDRESS)) {
			return WordType.EMAIL;
		}
		else if (labelsText.equals(FormsLabels.USERNAME)) {
			return WordType.USERNAME;
		}
		else
			return WordType.OTHER;
	}

}
