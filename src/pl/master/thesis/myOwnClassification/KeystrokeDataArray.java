package pl.master.thesis.myOwnClassification;

import java.util.ArrayList;
import java.util.List;

import pl.master.thesis.keyTypingObjects.InterKeyTime;
import pl.master.thesis.keyTypingObjects.KeyHoldingTime;
import pl.master.thesis.keyTypingObjects.WordKeystrokeData;

public class KeystrokeDataArray {
	
	private double [] arrayKeyHoldTimes;
	private double [] arrayInterKeyTimes;
	private StatisticsCalculator statisticsCalculator;
	
	public KeystrokeDataArray (){
		statisticsCalculator = new StatisticsCalculator ();
	}
	
	public void convertWordKeystrokeDataToArrays(List <WordKeystrokeData> wordKeystrokeData){
		List <Double> listHoldTimes = new ArrayList <>();
		List <Double> listInterKeyTimes = new ArrayList <> ();
		for (WordKeystrokeData data: wordKeystrokeData){
			for (KeyHoldingTime holdTime: data.getHoldTimes()){
				listHoldTimes.add((double)holdTime.getHoldTime());
			}
			for (InterKeyTime holdTime: data.getInterKeyTimes()){
				listInterKeyTimes.add((double)holdTime.getInterKeyTime());
			}
		}
		arrayKeyHoldTimes = new double [listHoldTimes.size()];
		arrayInterKeyTimes = new double [listInterKeyTimes.size()];
		fillArrayWithValuesFromList(arrayKeyHoldTimes, listHoldTimes);
		fillArrayWithValuesFromList(arrayInterKeyTimes, listInterKeyTimes);
	}
	
	private void fillArrayWithValuesFromList (double [] array, List <Double> list){
		int i = 0;
		for (Double d: list){
			array[i]=d.doubleValue();
			i++;
		}
	}
	
	public double [] getInterKeyTimes(){
		return arrayInterKeyTimes;
	}
	
	public double [] getHoldTimes(){
		return arrayKeyHoldTimes;
	}
	
	public void removeOutliers(){
		System.out.println("arrays size was hold: "+arrayKeyHoldTimes.length);
		System.out.println("arrays size was inter: "+arrayInterKeyTimes.length);
		arrayKeyHoldTimes = statisticsCalculator.removeOutliers(arrayKeyHoldTimes);
		arrayInterKeyTimes = statisticsCalculator.removeOutliers(arrayInterKeyTimes);
		System.out.println("arrays now hold: "+arrayKeyHoldTimes.length);
		System.out.println("arrays now inter: "+arrayInterKeyTimes.length);
	}
	
	

}
