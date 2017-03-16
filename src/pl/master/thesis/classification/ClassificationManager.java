package pl.master.thesis.classification;

import java.util.List;

import pl.master.thesis.keyTypingObjects.WordKeystrokeData;
import pl.master.thesis.others.DataSaver;

public class ClassificationManager {

	private DataDivider dataDivider;
	private double percentOfConsecutiveKeysHold;
	private StatisticsCalculator statisticsCalculator;
	private DataSaver dataSaver;
	private Classifier classifier;
	
	public ClassificationManager (){
		percentOfConsecutiveKeysHold=0;
		dataDivider = new DataDivider();
		statisticsCalculator = new StatisticsCalculator();
		dataSaver = new DataSaver();
		classifier = new Classifier();
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
		classifier.saveInputData(testDataArray, trainingDataArray);
		classifier.compare();
	}
			
	private KeystrokeDataArray convertListOfKeystrokeDataToArrays(List <WordKeystrokeData> data){
		KeystrokeDataArray dataArray = new KeystrokeDataArray();
		dataArray.convertWordKeystrokeDataToArrays(data);
		return dataArray;
	}
	
	
	public void saveDataToFile(String username){
		DataStatistics d = statisticsCalculator.calculate(dataDivider.getWholeData(), username);
		dataSaver.saveDataToFile(d);
	}
	
}
