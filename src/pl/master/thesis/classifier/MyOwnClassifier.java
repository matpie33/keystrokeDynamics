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
		KeystrokeDataArray testDataArray = convertListOfKeystrokeDataToArrays(dataDivider.getTestData());
		KeystrokeDataArray trainingDataArray = convertListOfKeystrokeDataToArrays(dataDivider.getTrainingData());
		testDataArray.removeOutliers();
		trainingDataArray.removeOutliers();
		boolean same1 = testIfSamplesHaveSameMeans(testDataArray.getHoldTimes(), 
				trainingDataArray.getHoldTimes());
		boolean same2 = testIfSamplesHaveSameMeans(testDataArray.getInterKeyTimes(), 
				trainingDataArray.getInterKeyTimes());
		System.out.println("are same: "+same1+same2);
	}
			
	private KeystrokeDataArray convertListOfKeystrokeDataToArrays(List <WordKeystrokeData> data){
		KeystrokeDataArray dataArray = new KeystrokeDataArray();
		dataArray.convertWordKeystrokeDataToArrays(data);
		return dataArray;
	}
	
	private boolean testIfSamplesHaveSameMeans(double [] sample1, double [] sample2){
		TTest test = new TTest();
		double d = test.tTest(sample1, sample2);
		System.out.println("prawdopodobienstwo jest: "+d);
		
		return test.homoscedasticTTest(sample1, sample2, 0.05);
	}
	
	public void saveDataToFile(String username){
		DataStatistics d = statisticsCalculator.calculate(dataDivider.getWholeData(), username);
		dataSaver.saveDataToFile(d);
	}
	
}
