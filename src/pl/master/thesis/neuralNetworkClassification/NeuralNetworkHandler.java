package pl.master.thesis.neuralNetworkClassification;

import java.util.List;

import org.deeplearning4j.eval.Evaluation;
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
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import pl.master.thesis.crossValidation.AcceptingStrategy;
import pl.master.thesis.crossValidation.TrainingAndTestSet;

public class NeuralNetworkHandler {

	private int numberOfInputNeurons = 21;
	private int numberOfUsers;
	private MultiLayerConfiguration configuration;
	private AcceptingStrategy acceptingStrategy;
	private MultiLayerNetwork model;
	private DataSet trainingData;

	public NeuralNetworkHandler(AcceptingStrategy acceptingStrategy, int numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
		this.acceptingStrategy = acceptingStrategy;
	}

	// TODO use it in NeuralNetworkClassifier

	public void initiateNetwork(int hiddenLayerNeurons) {
		long seed = 6;
		int iterations = 1000;

		configuration = new NeuralNetConfiguration.Builder().seed(seed).iterations(iterations)
				.activation(Activation.TANH).weightInit(WeightInit.XAVIER_UNIFORM).learningRate(0.5)
				.list()
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
				.backprop(true).pretrain(true).build();
	}

	public List<Boolean> learnAndEvaluateDataSet(TrainingAndTestSet set) {
		model = new MultiLayerNetwork(configuration);
		model.init();
		model.setListeners(new ScoreIterationListener(100));
		trainingData = set.getTrainingSet();
		DataSet testData = set.getTestSet();
		normalizeData(trainingData, testData);
		System.out.println("training set size: " + trainingData.numExamples());
		System.out.println("test set size: " + testData.numExamples());
		model.fit(trainingData);

		return evaluateTestSet(testData);
		// evaluate the model on the test set

	}

	public List<Boolean> evaluateTestSet(DataSet testSet) {
		INDArray output = model.output(testSet.getFeatureMatrix());
		// System.out.println("output is here");
		// System.out.println(output);
		Evaluation e = new Evaluation(numberOfUsers);
		e.eval(testSet.getLabels(), output);
		System.out.println(e.stats());

		// System.out.println("result of learning: " + output);
		// System.out.println("test set output: " + testData.getLabels());
		List<Boolean> rowsCorrectClassification = acceptingStrategy
				.isUserAccepted(testSet.getLabels(), output);
		System.out.println("accept list: " + rowsCorrectClassification);
		return rowsCorrectClassification;
	}

	public List<Boolean> evaluateImposterSet(DataSet testSet, int otherUserId) {
		normalizeData(trainingData, testSet);
		INDArray output = model.output(testSet.getFeatureMatrix());
		System.out.println("output is here");
		System.out.println(output);

		List<Boolean> rowsCorrectClassification = acceptingStrategy
				.isUserDeniedAsOtherUser(output, otherUserId);
		System.out.println("accept list: " + rowsCorrectClassification);
		return rowsCorrectClassification;
	}

	private void normalizeData(DataSet trainingData, DataSet testData) {
		DataNormalization normalizer = new NormalizerStandardize();
		normalizer.fit(trainingData); // Collect the statistics (mean/stdev)
										// from the training data. This does not
										// modify the input data
		normalizer.transform(trainingData);
		normalizer.transform(testData); // This is using statistics calculated
		// from the *training* set
	}

}
