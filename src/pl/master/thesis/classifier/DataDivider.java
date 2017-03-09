package pl.master.thesis.classifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.master.thesis.keyTypingObjects.WordKeystrokeData;

public class DataDivider {
	
	private List <WordKeystrokeData> testData;
	private List <WordKeystrokeData> trainingData;
	private List <WordKeystrokeData> wholeData;
	
	public DataDivider (){
		testData = new ArrayList <>();
		trainingData = new ArrayList <>();
		wholeData = new ArrayList <>();
	}
	
	
	public void addWordKeystrokeData(WordKeystrokeData newData){
		wholeData.add(newData);
	}
	
	public void divideDataToTestAndTrainingSet(){
		int datesCounter = 0;
		int passCounter = 0;
		for (WordKeystrokeData data: wholeData){
			switch (data.getType()){
			case PASSWORD:
				addDataBasedOnCounter(data, passCounter);
				passCounter++;
				break;
			case DATE:
				addDataBasedOnCounter(data, datesCounter);
				datesCounter++;
				break;
			case EMAIL:
				testData.add(data);
				break;
			case USERNAME:
				trainingData.add(data);
				break;
			case OTHER:
				randomlyAssignData(data);
				break;
			}
		}
		System.out.println("trainig data: "+trainingData);
		System.out.println("test data: "+testData);
	}
	
	private void addDataBasedOnCounter(WordKeystrokeData data, int counter){
		if (counter==0){
			testData.add(data);
		}
		else{
			trainingData.add(data);
		}
	}
	
	private void randomlyAssignData(WordKeystrokeData data){
		Random r = new Random();
		int max = 11;
		int rand = r.nextInt(max);
		if (rand < (max-1) / 2){
			testData.add(data);
		}
		else{
			trainingData.add(data);
		}
	}
	
	public List <WordKeystrokeData> getTestData(){
		return testData;
	}
	
	public List <WordKeystrokeData> getTrainingData(){
		return trainingData;
	}

}
