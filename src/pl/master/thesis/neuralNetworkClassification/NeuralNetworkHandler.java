package pl.master.thesis.neuralNetworkClassification;

import java.util.List;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import pl.master.thesis.crossValidation.AcceptingStrategy;
import pl.master.thesis.crossValidation.TrainingAndTestSet;

public class NeuralNetworkHandler {

	private int numberOfInputNeurons = 8;
	private int numberOfUsers = 56;
	private MultiLayerConfiguration configuration;
	private AcceptingStrategy acceptingStrategy;

	public NeuralNetworkHandler(AcceptingStrategy acceptingStrategy) {
		this.acceptingStrategy = acceptingStrategy;
	}

	// TODO use it in NeuralNetworkClassifier

	public void initiateNetwork(int hiddenLayerNeurons) {
		long seed = 6;
		int iterations = 1000;
		hiddenLayerNeurons = 20;

		configuration = new NeuralNetConfiguration.Builder().seed(seed).iterations(iterations)
				.activation(Activation.TANH).weightInit(WeightInit.XAVIER).learningRate(0.1)
				.regularization(true).l2(1e-4).list()
				.layer(0,
						new DenseLayer.Builder().nIn(numberOfInputNeurons).nOut(hiddenLayerNeurons)
								.build())
				// .layer(1,
				// new
				// DenseLayer.Builder().nIn(hiddenLayer1Neurons).nOut(hiddenLayer2Neurons)
				// .build())
				.layer(1,
						new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
								.activation(Activation.SOFTMAX).nIn(hiddenLayerNeurons)
								.nOut(numberOfUsers).build())
				.backprop(true).pretrain(false).build();
	}

	public List<Boolean> learnDataSet(TrainingAndTestSet set) {
		MultiLayerNetwork model = new MultiLayerNetwork(configuration);
		model.init();
		model.setListeners(new ScoreIterationListener(100));
		DataSet trainingData = set.getTrainingSet();
		model.fit(trainingData);
		DataSet testData = set.getTestSet();

		// evaluate the model on the test set
		INDArray output = model.output(testData.getFeatureMatrix());
		System.out.println("result of learning: " + output);
		System.out.println("test set output: " + testData.getLabels());
		List<Boolean> rowsCorrectClassification = acceptingStrategy
				.isUserAccepted(testData.getLabels(), output);
		System.out.println("accept list: " + rowsCorrectClassification);
		return rowsCorrectClassification;
	}

}
