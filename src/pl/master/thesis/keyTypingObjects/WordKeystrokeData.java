package pl.master.thesis.keyTypingObjects;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class WordKeystrokeData {

	private List<InterKeyTime> interKeyTimes;
	private List<KeyHoldingTime> holdTimes;
	private String word = "";
	private int twoConsecutiveKeysHold;
	private int numberOfInterKeyTimesBetweenTwoDifferentKeys;
	private WordType wordType;

	private boolean startedWithTab;

	public WordKeystrokeData(String firstLetter, boolean startedWithTab) {
		word += firstLetter;
		twoConsecutiveKeysHold = 0;
		interKeyTimes = new ArrayList<>();
		holdTimes = new ArrayList<>();
		this.startedWithTab = startedWithTab;
	}

	public WordKeystrokeData(boolean startedWithTab, List<KeyHoldingTime> keyHolds,
			List<InterKeyTime> interKeyTimes) {
		this.interKeyTimes = interKeyTimes;
		this.holdTimes = keyHolds;
	}

	public WordKeystrokeData(String firstLetter) {
		this(firstLetter, false);
	}

	public void addInterKeyTime(InterKeyTime inter) {
		interKeyTimes.add(inter);
	}

	public void addKeyHoldTime(KeyHoldingTime holdTime) {
		holdTimes.add(holdTime);
	}

	public void addLetter(String letter) {
		word += letter;
	}

	public void increaseTwoConsecutiveKeysHold() {
		twoConsecutiveKeysHold++;
	}

	public void closeAndCalculate() {
		System.out.println("current word: " + getInterKeyTimes());
		recalculateNumberOfInterKeyTimes();
		removeOutliers();
	}

	private void recalculateNumberOfInterKeyTimes() {
		numberOfInterKeyTimesBetweenTwoDifferentKeys = interKeyTimes.size();
		for (InterKeyTime inter : interKeyTimes) {
			if (inter.isItTimeBetweenSameKey()) {
				numberOfInterKeyTimesBetweenTwoDifferentKeys--;
			}
		}
	}

	private void removeOutliers() {
		double[] arrayInterKeyTime = new double[interKeyTimes.size()];
		int i = 0;
		for (InterKeyTime interKeyTime : interKeyTimes) {
			arrayInterKeyTime[i] = interKeyTime.getInterKeyTime();
			i++;
		}

		double[] arrayHoldTimes = new double[holdTimes.size()];
		i = 0;
		for (KeyHoldingTime k : holdTimes) {
			arrayHoldTimes[i] = k.getHoldTime();
			i++;
		}

		DescriptiveStatistics d = new DescriptiveStatistics(arrayInterKeyTime);
		double medianInterKeyTime = d.getPercentile(50.0);
		double standDeviationInterKeyTime = d.getStandardDeviation();
		DescriptiveStatistics d2 = new DescriptiveStatistics(arrayHoldTimes);
		double medianHoldTime = d2.getPercentile(50.0);
		double standDeviationHoldTime = d2.getStandardDeviation();

		for (InterKeyTime interTime : interKeyTimes) {
			if ((interTime.getInterKeyTime() > medianInterKeyTime + 3 * standDeviationInterKeyTime)
					|| (interTime.getInterKeyTime() < medianInterKeyTime
							- 3 * standDeviationInterKeyTime)) {
			}
		}

	}

	public int getNumberOfInterKeyTimesBetweenTwoDifferentKeys() {
		return numberOfInterKeyTimesBetweenTwoDifferentKeys;
	}

	public List<InterKeyTime> getInterKeyTimes() {
		return interKeyTimes;
	}

	public List<KeyHoldingTime> getHoldTimes() {
		return holdTimes;
	}

	public boolean isStartedWithTab() {
		return startedWithTab;
	}

	public String getWord() {
		return word;
	}

	public WordType getType() {
		return wordType;
	}

	public void setType(WordType type) {
		if (wordType != null) {
			return;
		}
		wordType = type;
	}

	@Override
	public String toString() {
		return "Word: " + word + " type: " + wordType + " interKeyTimes: " + interKeyTimes
				+ " hold times: " + holdTimes;
	}

	public boolean isEmpty() {
		return word.isEmpty();
	}

}
