package pl.master.thesis.neuralNetworkClassification;

import java.io.File;
import java.util.List;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;

public class NeuralNetworkClassifier {
	
	private NeuralNetwork neuralNetwork;
	private int numberOfInputNeurons = 3;
	private int numberOfHiddenNeurons = 2;
	private int numberOfOutputNeurons = 1;
	private String fileName;
	
	public NeuralNetworkClassifier (){
		
		fileName = "neurons.txt";
		File file = new File(fileName);
		if (file.exists()){
			neuralNetwork = NeuralNetwork.createFromFile(fileName);
			printWeights(neuralNetwork.getWeights());
		}
		else{
			neuralNetwork = new MultiLayerPerceptron (numberOfInputNeurons, numberOfHiddenNeurons, 
					numberOfOutputNeurons);
		}
		
	}
	
	private void printWeights(Double [] weights){
		for (double d: weights){
			System.out.println(d);
		}
	}
	
	public void learn (List <NeuralNetworkInput> data){
		DataSet trainingSet = new DataSet(numberOfInputNeurons, numberOfOutputNeurons);
		for (NeuralNetworkInput inputData: data){
			int isTabsMoreThan50Percent = translateToInt(inputData.isMoreThan50PercentTabs());
			trainingSet.addRow(new DataSetRow(new double [] {inputData.getMeanHoldTime(), 
					inputData.getMeanInterTime(), isTabsMoreThan50Percent }, 
					new double [] {inputData.getUserId()})); 
		}
		
		neuralNetwork.learn(trainingSet);
		neuralNetwork.save(fileName);
	}
	
	private int translateToInt(boolean booleanValue){
		if (booleanValue){
			return 1;
		}
		else
			return 0;
	}
	
}
