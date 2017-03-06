package pl.master.thesis.classifier;

import java.util.List;

import pl.master.thesis.keyTypingObjects.WordKeystrokeData;

public class MyOwnClassifier {

	private DataDivider dataDivider;
	private double percentOfConsecutiveKeysHold;
	
	public MyOwnClassifier (){
		percentOfConsecutiveKeysHold=0;
		dataDivider = new DataDivider();
	}
	
	public void addKeystrokeData (WordKeystrokeData newData){
		dataDivider.addWordKeystrokeData(newData);
	}
	
	public void showData(){
		dataDivider.divideDataToTestAndTrainingSet();
	}
	
	public void calculate(List <WordKeystrokeData> wordKeystrokeDatas){
		int tabs = 0;
		for (WordKeystrokeData data: wordKeystrokeDatas){
			if (data.isStartedWithTab()){
				tabs++;
			}
		}
		System.out.println("tabs pressed: "+tabs);
	}
	
}
