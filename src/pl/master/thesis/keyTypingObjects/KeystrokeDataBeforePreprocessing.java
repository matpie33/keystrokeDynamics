package pl.master.thesis.keyTypingObjects;

import java.util.ArrayList;
import java.util.List;

import pl.master.thesis.dataConverters.KeystrokeDataToArrayConverter;
import pl.master.thesis.dataConverters.WordToDigraphsConverter;

public class KeystrokeDataBeforePreprocessing {

	private List<DigraphTimingData> digraphFeatures;
	private List<InterKeyTime> interKeyTimes;
	private List<KeyHoldingTime> holdTimes;
	private WordToDigraphsConverter converter;
	private KeystrokeDataToArrayConverter dataToArrayConverter;

	public KeystrokeDataBeforePreprocessing() {
		interKeyTimes = new ArrayList<>();
		holdTimes = new ArrayList<>();
		converter = new WordToDigraphsConverter();
		dataToArrayConverter = new KeystrokeDataToArrayConverter();
		digraphFeatures = new ArrayList<>();
	}

	public List<InterKeyTime> getInterKeyTimes() {
		return interKeyTimes;
	}

	public List<KeyHoldingTime> getUsernameHoldTimes() {
		return holdTimes;
	}

	public void addInterKeyTime(InterKeyTime inter) {
		interKeyTimes.add(inter);
	}

	public void addUsernameHoldingTime(Digraph d, KeyHoldingTime hold) {
		for (DigraphTimingData time : digraphFeatures) {
			if (time.getDigraph().equals(d)) {
				if (!time.hasKey1Set()) {
					time.setKey1HoldTime(hold);
				}
				else if (!time.hasKey2Set()) {
					time.setKey2HoldTime(hold);
				}
			}
		}
		holdTimes.add(hold);
	}

}
