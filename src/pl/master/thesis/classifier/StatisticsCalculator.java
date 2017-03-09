package pl.master.thesis.classifier;

import java.util.List;

import pl.master.thesis.keyTypingObjects.InterKeyTime;
import pl.master.thesis.keyTypingObjects.KeyHoldingTime;
import pl.master.thesis.keyTypingObjects.WordKeystrokeData;

public class StatisticsCalculator {
	
	public DataStatistics calculate(List <WordKeystrokeData> wordKeystrokeDatas){
		
		double tabsPercent = 0;
		double meanInterKeyTime = 0;
		double meanHoldTime = 0;
		int interKeyTimeAmount = 0;
		int holdTimeAmount = 0;
		for (WordKeystrokeData word: wordKeystrokeDatas){
			meanInterKeyTime += sumInterKeyTimesInWord(word);
			interKeyTimeAmount += word.getInterKeyTimes().size();
			meanHoldTime += sumHoldTimesInWord(word);
			holdTimeAmount += word.getHoldTimes().size();
			if (word.isStartedWithTab()){
				tabsPercent++;
			}
		}
		System.out.println("number of tabs: "+tabsPercent);
		System.out.println("amount: "+wordKeystrokeDatas.size());
		meanInterKeyTime = meanInterKeyTime / (double)interKeyTimeAmount;
		meanHoldTime = meanHoldTime / (double) holdTimeAmount;
		tabsPercent = tabsPercent/ (double)(wordKeystrokeDatas.size()-1);
		
		System.out.println("mean inter time is: "+meanInterKeyTime);
		System.out.println("mean hold time is: "+meanHoldTime);
		System.out.println("tabs percent: "+tabsPercent);
		
		
		return new DataStatistics(meanInterKeyTime, meanHoldTime, tabsPercent);
		
	}
	
	private long sumInterKeyTimesInWord (WordKeystrokeData word){
		long sum = 0;
		for (InterKeyTime interTime: word.getInterKeyTimes()){
			sum+=interTime.getInterKeyTime();
		}
		return sum;
	}
	
	private long sumHoldTimesInWord (WordKeystrokeData word){
		long sum = 0;
		System.out.println("new hold times: "+word.getHoldTimes());
		for (KeyHoldingTime holdTime: word.getHoldTimes()){
			sum+=holdTime.getHoldTime();
		}
		return sum;
	}

}
