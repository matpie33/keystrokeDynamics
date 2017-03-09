package pl.master.thesis.keyTypingObjects;

import java.util.ArrayList;
import java.util.List;

public class WordKeystrokeData {
	
	private List <InterKeyTime> interKeyTimes;
	private List <KeyHoldingTime> holdTimes;
	private String word = "";
	private int twoConsecutiveKeysHold;
	private int numberOfInterKeyTimesBetweenTwoDifferentKeys;
	private WordType wordType;
	
	private boolean startedWithTab;
	
	public WordKeystrokeData(String firstLetter, boolean startedWithTab){
		word+=firstLetter;
		twoConsecutiveKeysHold = 0;
		interKeyTimes = new ArrayList <>();
		holdTimes = new ArrayList <>();
		this.startedWithTab = startedWithTab;
	}
	
	public WordKeystrokeData(String firstLetter){
		this(firstLetter, false);
	}

	public void addInterKeyTime(InterKeyTime inter){
		if (!inter.isItTimeBetweenSameKey()){
			interKeyTimes.add(inter);
		}
	}
	
	public void addKeyHoldTime(KeyHoldingTime holdTime){
		holdTimes.add(holdTime);
	}
	
	public void addLetter(String letter){
		word+=letter;
	}
	
	public void increaseTwoConsecutiveKeysHold(){
		twoConsecutiveKeysHold++;
	}
	
	public void closeAndCalculate(){
		numberOfInterKeyTimesBetweenTwoDifferentKeys = interKeyTimes.size();
		for (InterKeyTime inter: interKeyTimes){
			if (inter.isItTimeBetweenSameKey()){
				numberOfInterKeyTimesBetweenTwoDifferentKeys--;
			}
		}
	}
	
	public int getNumberOfInterKeyTimesBetweenTwoDifferentKeys(){
		return numberOfInterKeyTimesBetweenTwoDifferentKeys;
	}
	
	public List<InterKeyTime> getInterKeyTimes() {
		return interKeyTimes;
	}

	public List<KeyHoldingTime> getHoldTimes() {
		return holdTimes;
	}

	public boolean isStartedWithTab(){
		return startedWithTab;
	}
	
	public String getWord(){
		return word;
	}
	
	public WordType getType(){
		return wordType;
	}
	
	public void setType(WordType type){
		if (wordType != null){
			return;
		}
		wordType = type;
	}
	
	@Override
	public String toString(){
		System.out.println("inters: "+interKeyTimes);
		System.out.println("holds: "+holdTimes);
		return "Word: "+word+" type: "+wordType;
	}
	
}
