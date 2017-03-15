package pl.master.thesis.classifier;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import pl.master.thesis.features.Feature;
import pl.master.thesis.keyTypingObjects.InterKeyTime;
import pl.master.thesis.keyTypingObjects.KeyHoldingTime;
import pl.master.thesis.keyTypingObjects.WordKeystrokeData;
import pl.master.thesis.strings.FeaturesNames;

public class StatisticsCalculator {
	
	private final double MEDIAN_PERCENTILE = 50.0;
	
	public DataStatistics calculate(List <WordKeystrokeData> wordKeystrokeDatas, String userName){
		
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
		meanInterKeyTime = meanInterKeyTime / (double)interKeyTimeAmount;
		meanHoldTime = meanHoldTime / (double) holdTimeAmount;
		tabsPercent = tabsPercent/ (double)(wordKeystrokeDatas.size()-1);
		
		DataStatistics statistics = new DataStatistics(userName);
		statistics.addFeature(new Feature(FeaturesNames.INTER_KEY_TIME, meanInterKeyTime));
		statistics.addFeature(new Feature(FeaturesNames.HOLD_TIME, meanHoldTime));
		statistics.addFeature(new Feature(FeaturesNames.TABS_PRESSED_PERCENT, tabsPercent));
		
		return statistics;
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
		for (KeyHoldingTime holdTime: word.getHoldTimes()){
			sum+=holdTime.getHoldTime();
		}
		return sum;
	}
	
	public double [] removeOutliers(double [] arrayHoldTimes){
		DescriptiveStatistics d = new DescriptiveStatistics(arrayHoldTimes);
		double medianInterKeyTime = d.getPercentile(MEDIAN_PERCENTILE);
		double standDeviationInterKeyTime = d.getStandardDeviation();
		return removeOutliersForGivenMedianAndStdDev(arrayHoldTimes, standDeviationInterKeyTime, medianInterKeyTime);
	}
	
	private double [] removeOutliersForGivenMedianAndStdDev (double [] array, double stdev, double median){
		List <Double> arrayList = convertArrayOfDoublesToList(array);
		System.out.println("arraylist size now: "+arrayList.size());
		double treshold = 1.5;
		for (int i = 0; i < arrayList.size(); i++){
			Double interTime =  arrayList.get(i);
			if ((interTime>median + treshold*stdev) ||	(interTime< median - treshold*stdev)) {
				arrayList.remove(interTime);
				System.out.println("arraylist size now: "+arrayList.size());
			}			
		}
		
		return convertListOfDoubleToArray(arrayList);
	}
	
	private List <Double> convertArrayOfDoublesToList (double [] array){
		List <Double> list = new ArrayList<>();
		for (double d: array){
			list.add(d);
		}
		return list;
	}
	
	private double [] convertListOfDoubleToArray (List <Double> list){
		double [] array = new double [list.size()];
		int i = 0;
		for (Double d: list){
			array[i] = d;
			i++;
		}
		return array;
	}

}
