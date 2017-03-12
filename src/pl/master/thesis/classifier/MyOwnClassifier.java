package pl.master.thesis.classifier;

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
	
	public void showData(){
		dataDivider.divideDataToTestAndTrainingSet();
	}
	
	
	public void saveDataToFile(String username){
		System.out.println("will try to save");
		DataStatistics d = statisticsCalculator.calculate(dataDivider.getWholeData(), username);
		System.out.println("data stats: "+d);
		dataSaver.saveDataToFile(d);
	}
	
}
