package pl.master.thesis.classifier;

import java.util.List;

import org.apache.commons.math3.stat.inference.TTest;

import pl.master.thesis.keyTypingObjects.WordKeystrokeData;
import pl.master.thesis.others.DataSaver;

public class MyOwnClassifier {

	private DataDivider dataDivider;
	private double percentOfConsecutiveKeysHold;
	private StatisticsCalculator statisticsCalculator;
	private DataSaver dataSaver;
	
	
	public MyOwnClassifier (){
		percentOfConsecutiveKeysHold=0;
		dataDivider = new DataDivider();
		statisticsCalculator = new StatisticsCalculator();
		dataSaver = new DataSaver();
	}
	
	public void addKeystrokeData (WordKeystrokeData newData){
		dataDivider.addWordKeystrokeData(newData);
	}
	
	public void compareData(){
		if (dataDivider.noData()){
			return;
		}
		divideDataToSets();
		getAndCompareDataSets();
	}
	
	public void divideDataToSets(){
		dataDivider.divideDataToTestAndTrainingSet();
	}
	
	private void getAndCompareDataSets(){
		KeystrokeDataArray testDataArray = convertListToArray(dataDivider.getTestData());
		KeystrokeDataArray trainingDataArray = convertListToArray(dataDivider.getTrainingData());
		boolean same1 = testIfSamplesHaveSameMeans(testDataArray.getHoldTimes(), 
				trainingDataArray.getHoldTimes());
		boolean same2 = testIfSamplesHaveSameMeans(testDataArray.getInterKeyTimes(), 
				trainingDataArray.getInterKeyTimes());
		System.out.println("same: "+same1 + same2);
	}
			
	private KeystrokeDataArray convertListToArray(List <WordKeystrokeData> data){
		KeystrokeDataArray dataArray = new KeystrokeDataArray();
		dataArray.convertWordKeystrokeDataToArrays(data);
		return dataArray;
	}
	
	private boolean testIfSamplesHaveSameMeans(double [] sample1, double [] sample2){
		TTest test = new TTest();
		double d = test.tTest(sample1, sample2);
		System.out.println("oduble: "+d);
		System.out.println(test.homoscedasticTTest(sample1, sample2));
		return test.homoscedasticTTest(sample1, sample2, 0.05);
	}
	
	public void saveDataToFile(String username){
		System.out.println("will try to save");
		DataStatistics d = statisticsCalculator.calculate(dataDivider.getWholeData(), username);
		System.out.println("data stats: "+d);
		dataSaver.saveDataToFile(d);
	}
	
}
