package pl.master.thesis.neuralNetworkClassification;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
	private final int maxLearningTime = 5000; // miliseconds
	private Timer timer;

	public NeuralNetworkClassifier() {

		fileName = "neurons.txt";
		File file = new File(fileName);
		if (file.exists()) {
			neuralNetwork = NeuralNetwork.createFromFile(fileName);
			printWeights(neuralNetwork.getWeights());
		}
		else {
			neuralNetwork = new MultiLayerPerceptron(numberOfInputNeurons, numberOfHiddenNeurons,
					numberOfOutputNeurons);
			System.out.println("new neural network");
		}

	}

	private void printWeights(Double[] weights) {
		for (double d : weights) {
		}
	}

	public void learn(List<NeuralNetworkInput> data) {
		DataSet trainingSet = new DataSet(numberOfInputNeurons, numberOfOutputNeurons);
		for (NeuralNetworkInput inputData : data) {
			System.out.println("input data: " + inputData);
			trainingSet
					.addRow(new DataSetRow(
							new double[] { inputData.getMeanHoldTime(),
									inputData.getMeanInterTime(), inputData.getIsTabbed() },
							new double[] { inputData.getUserId() }));
			System.out.println("user id: " + inputData.getUserId());
		}
		// scheduleMaxTimeForLearning();
		System.out.println("do actually learn");
		neuralNetwork.learn(trainingSet);
		stopTimer();
		neuralNetwork.save(fileName);
		System.out.println("saved");
	}

	private void scheduleMaxTimeForLearning() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				neuralNetwork.stopLearning();
				System.out.println("neural network timeout");
			}
		}, maxLearningTime);
	}

	private void stopTimer() {
		timer.cancel();
		timer = null;
	}

	public void classify(NeuralNetworkInput data) {

		neuralNetwork.setInput(data.getMeanHoldTime(), data.getMeanInterTime(), data.getIsTabbed());
		neuralNetwork.calculate();
		double output[] = neuralNetwork.getOutput();
	}

}
