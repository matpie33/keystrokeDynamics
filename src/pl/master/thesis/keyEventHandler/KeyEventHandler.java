package pl.master.thesis.keyEventHandler;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTextField;

import pl.master.thesis.keyTypingObjects.Digraph;
import pl.master.thesis.keyTypingObjects.InterKeyTime;
import pl.master.thesis.keyTypingObjects.KeyHoldingTime;
import pl.master.thesis.keyTypingObjects.WordKeystrokeData;
import pl.master.thesis.keyTypingObjects.WordType;
import pl.master.thesis.myOwnClassification.ClassificationManager;
import pl.master.thesis.others.FieldsInitializer;

public class KeyEventHandler {

	private int keysTyped;
	private long lastKeyReleasedTime;
	private long lastKeyPressedTime;
	private long maxBreakTime = 2_000;
	private double meanTypeSpeed;
	private int breaks;
	private int errors;
	private Map<String, Long> currentlyPressedKeys;
	private List<KeyHoldingTime> keyHoldingTime;
	private String lastKeyPressed;
	private final String TAB_KEY = "Tab";
	private WordKeystrokeData currentlyTypedWordData;
	private ClassificationManager classifier;
	private boolean focusLost;
	private FieldsInitializer textFieldsInformationHolder;

	public KeyEventHandler() {

		currentlyTypedWordData = new WordKeystrokeData("", false);
		classifier = new ClassificationManager();
		keyHoldingTime = new ArrayList<>();
		currentlyPressedKeys = new HashMap<>();
		lastKeyPressed = "";
		focusLost = true;
	}

	public void setFieldsInitializer(FieldsInitializer fieldsInitializer) {
		textFieldsInformationHolder = fieldsInitializer;
	}

	public void stopTimerAndCalculateSpeed() {

		long sum = 0;
		for (KeyHoldingTime l : keyHoldingTime) { // TODO think about it, do we
													// not duplicate times in
													// worst case?
			sum += l.getHoldTime();
		}
		double mean = ((double) keysTyped * 60D / ((double) sum / (1_000_000_000D)));
		if (sum == 0) {
			return;
		}
		meanTypeSpeed *= breaks++;
		meanTypeSpeed += mean;
		meanTypeSpeed /= breaks;
	}

	public void recordKeyPress(KeyEvent e) {
		// TODO handle right alt properly: it sends alt + ctrl scancodes
		// together
		String currentKey = getKeyName(e.getKeyCode());
		if (currentKey.equals(TAB_KEY)) {
			if (e.getComponent() instanceof JTextField) {
				JTextField f = (JTextField) e.getComponent();
				WordType type = textFieldsInformationHolder.getTextFieldType(f);
				savePreviousWordAndWatchNext("", true, type);
			}
			return;
		}

		if (currentlyPressedKeys.containsKey(currentKey)) {
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
			recordMistypedKey();
			return;
		}

		long interKeyTime = calculateInterKeyTime();
		addKeyToCurrentlyPressedList(currentKey);
		currentlyTypedWordData.addLetter(currentKey);
		if (!lastKeyPressed.isEmpty()) {
			InterKeyTime inter = new InterKeyTime(new Digraph(lastKeyPressed, currentKey),
					interKeyTime);
			saveInterKeyTime(inter);
		}
		lastKeyPressed = currentKey;

	}

	private void saveInterKeyTime(InterKeyTime interKeyTime) {
		currentlyTypedWordData.addInterKeyTime(interKeyTime);
	}

	private long calculateInterKeyTime() {
		long lastKeyEventTime = Math.max(lastKeyPressedTime, lastKeyReleasedTime);
		lastKeyPressedTime = System.nanoTime();
		return lastKeyPressedTime - lastKeyEventTime;
	}

	private void addKeyToCurrentlyPressedList(String currentKey) {
		currentlyPressedKeys.put(currentKey, lastKeyPressedTime);
		if (currentlyPressedKeys.size() > 1) {
			currentlyTypedWordData.increaseTwoConsecutiveKeysHold();
		}
		keysTyped++;
	}

	private String getKeyName(int keyCode) {
		return KeyEvent.getKeyText(keyCode);
	}

	public void recordKeyRelease(KeyEvent e) {
		lastKeyReleasedTime = System.nanoTime();
		int keyCode = e.getKeyCode();
		String keyName = getKeyName(keyCode);
		removeKeyFromCurrentlyPressed(keyName);
	}

	private void removeKeyFromCurrentlyPressed(String keyName) {
		long pressTime = currentlyPressedKeys.get(keyName) != null
				? currentlyPressedKeys.get(keyName) : 0;
		if (pressTime == 0) {
			return;
		}
		long holdingTime = lastKeyReleasedTime - pressTime;
		currentlyPressedKeys.remove(keyName);
		if (!keyName.equals(TAB_KEY)) {
			saveHoldingTime(new KeyHoldingTime(keyName, holdingTime, pressTime));
		}
	}

	private void saveHoldingTime(KeyHoldingTime holdTime) {
		currentlyTypedWordData.addKeyHoldTime(holdTime);
	}

	private void recordMistypedKey() {
		errors++;
	}

	public double getMeanTypeSpeed() {
		return meanTypeSpeed;
	}

	public int getErrors() {
		return errors;
	}

	public int getNumberOfTypedKeys() {
		return keysTyped;
	}

	public void textFieldClicked(Component c) {
		if (focusLost && !currentlyTypedWordData.isEmpty()) {
			if (c instanceof JTextField) {
				JTextField f = (JTextField) c;
				WordType type = textFieldsInformationHolder.getTextFieldType(f);
				savePreviousWordAndWatchNext("", false, type);
			}
		}

	}

	private void savePreviousWordAndWatchNext(String currentKey, boolean startedWithTab,
			WordType type) {
		focusLost = false;
		classifier.addKeystrokeData(currentlyTypedWordData);
		if (!currentlyPressedKeys.isEmpty()) {
			String[] keysPressed = currentlyPressedKeys.keySet().toArray(new String[] {});
			int i = 0;
			while (!currentlyPressedKeys.isEmpty()) {
				String key = keysPressed[i];
				removeKeyFromCurrentlyPressed(key);
				i++;
			}
		}
		currentlyTypedWordData.closeAndCalculate();
		currentlyTypedWordData.setType(type);
		currentlyTypedWordData = new WordKeystrokeData(currentKey, startedWithTab);
		clear();
	}

	public void show() {
		// TODO remove it later
		classifier.compareData();
	}

	public void done() {
		System.out.println("donee");
		savePreviousWordAndWatchNext("", false, WordType.OTHER);
	}

	public void focusLost() {
		focusLost = true;
	}

	public ClassificationManager getClassifier() {
		return classifier;
	}

	public void clear() {
		lastKeyPressed = "";
		lastKeyPressedTime = 0;
		lastKeyReleasedTime = 0;
		keyHoldingTime.clear();
		currentlyPressedKeys.clear();
		currentlyTypedWordData = new WordKeystrokeData("");
	}

}
