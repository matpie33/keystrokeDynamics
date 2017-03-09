package pl.master.thesis.classifier;

import pl.master.thesis.keyTypingObjects.WordKeystrokeData;

public class MyOwnClassifier {

	private DataDivider dataDivider;
	private double percentOfConsecutiveKeysHold;
	private StatisticsCalculator statisticsCalculator;
	
	public MyOwnClassifier (){
		percentOfConsecutiveKeysHold=0;
		dataDivider = new DataDivider();
		statisticsCalculator = new StatisticsCalculator();
	}
	
	public void addKeystrokeData (WordKeystrokeData newData){
		dataDivider.addWordKeystrokeData(newData);
	}
	
	public void showData(){
		dataDivider.divideDataToTestAndTrainingSet();
	}
	
	public void calculateStatisticsForSets(){
		dataDivider.divideDataToTestAndTrainingSet();
		DataStatistics test = statisticsCalculator.calculate(dataDivider.getTestData());
		DataStatistics training = statisticsCalculator.calculate(dataDivider.getTrainingData());
	}
	
}
