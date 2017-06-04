package pl.master.thesis.neuralNetworkClassification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.xml.sax.SAXException;

import pl.master.thesis.csvManipulation.CSVProcessing;
import pl.master.thesis.csvManipulation.CSVSaver;
import pl.master.thesis.dataConverters.WordToDigraphsConverter;
import pl.master.thesis.swingWorkers.AddUserDataOnlyWorker;

public class NeuralNetworkClassifier {

	private MultiLayerNetwork neuralNetwork;
	private int numberOfInputNeurons = 8;
	private int hiddenLayer1Neurons = 60;
	private int hiddenLayer2Neurons = 100;
	private int numberOfUsers = 56;
	private String neuralNetworkFileName = "neurons.txt";
	private String trainingSetFileName = "trainingData.txt";
	private CSVSaver csvSaver;
	private WordToDigraphsConverter wordDataConverter;

	public NeuralNetworkClassifier(WordToDigraphsConverter wordConverter) {

		wordDataConverter = wordConverter;
		csvSaver = new CSVSaver(trainingSetFileName);
		File neuralNetworkFile = new File(neuralNetworkFileName);

		// AddUsersFromDatasetIfNotInDBWorker user = new
		// AddUsersFromDatasetIfNotInDBWorker(
		// numberOfUsers);
		System.out.println("going further");
		// try {
		// user.doInBackground();
		// }
		// catch (SQLException e1) {
		// e1.printStackTrace();
		// }
		if (neuralNetworkFile.exists()) {
			try {
				neuralNetwork = ModelSerializer.restoreMultiLayerNetwork(neuralNetworkFile);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			// printWeights(neuralNetwork.getWeights());
		}
		else {
			createMultiLayerPerceptron();
			try {
				createCSVFileFromExistingDataset();
				learnExistingDataset();
				saveNeuralNetworkState();
			}
			catch (IOException | InterruptedException | ParserConfigurationException | SAXException
					| SQLException e) {
				e.printStackTrace();
			}
			System.out.println("new neural network");
		}

	}

	private void createMultiLayerPerceptron() {
		MultiLayerConfiguration networkConfiguration = createNeuralNetworkConfiguration();
		initNeuralNetwork(networkConfiguration);
	}

	private MultiLayerConfiguration createNeuralNetworkConfiguration() {
		long seed = 6;
		int iterations = 1000;

		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().seed(seed)
				.iterations(iterations).activation(Activation.TANH).weightInit(WeightInit.XAVIER)
				.learningRate(0.1).regularization(true).l2(1e-4).list()
				.layer(0,
						new DenseLayer.Builder().nIn(numberOfInputNeurons).nOut(hiddenLayer1Neurons)
								.build())
				// .layer(1,
				// new
				// DenseLayer.Builder().nIn(hiddenLayer1Neurons).nOut(hiddenLayer2Neurons)
				// .build())
				.layer(1,
						new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
								.activation(Activation.SOFTMAX).nIn(hiddenLayer1Neurons)
								.nOut(numberOfUsers).build())
				.backprop(true).pretrain(false).build();
		return conf;
	}

	private void initNeuralNetwork(MultiLayerConfiguration configuration) {
		neuralNetwork = new MultiLayerNetwork(configuration);
		neuralNetwork.init();
		neuralNetwork.setListeners(new ScoreIterationListener(100));
	}

	private void learnExistingDataset() throws FileNotFoundException, IOException,
			InterruptedException, ParserConfigurationException, SAXException, SQLException {
		SplitTestAndTrain testAndTrain = readExistingDataset();
		DataSet trainingData = testAndTrain.getTrain();
		DataSet testData = testAndTrain.getTest();
		normalizeData(trainingData, testData);
		neuralNetwork.fit(trainingData);
		new File(trainingSetFileName).delete();

	}

	private void createCSVFileFromExistingDataset()
			throws ParserConfigurationException, SAXException, IOException, SQLException {
		File trainingFile = new File(trainingSetFileName);
		if (trainingFile.exists()) {
			trainingFile.delete();
		}
		AddUserDataOnlyWorker addUserDataOnly = new AddUserDataOnlyWorker(wordDataConverter);
		CSVProcessing processing = new CSVProcessing(trainingSetFileName, addUserDataOnly);
		processing.extractStatisticsFromCSVAndSave();
	}

	private SplitTestAndTrain readExistingDataset()
			throws FileNotFoundException, IOException, InterruptedException {
		int numLinesToSkip = 0;
		String delimiter = ", ";
		RecordReader recordReader = new CSVRecordReader(numLinesToSkip, delimiter);
		recordReader.initialize(new FileSplit(new File(trainingSetFileName)));

		int classLabelIndex = numberOfInputNeurons;
		int numClasses = numberOfUsers;
		int samplesToRead = 20000;

		DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader, samplesToRead,
				classLabelIndex, numClasses);
		DataSet allData = iterator.next();
		allData.shuffle();
		double percentOfDataTakenForTrainingSet = 0.1;
		SplitTestAndTrain testAndTrain = allData
				.splitTestAndTrain(percentOfDataTakenForTrainingSet);
		return testAndTrain;
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

	private void printWeights(Double[] weights) {
		for (double d : weights) {
		}
	}

	public void saveDataInTemporaryFileAndLearn(List<NeuralNetworkInput> data)
			throws FileNotFoundException, IOException, InterruptedException,
			ParserConfigurationException, SAXException, SQLException {
		csvSaver.saveTemporary(data);
		learnExistingDataset();
		saveNeuralNetworkState();
	}

	private void saveNeuralNetworkState() throws IOException {
		boolean saveUpdater = true;
		ModelSerializer.writeModel(neuralNetwork, neuralNetworkFileName, saveUpdater);
	}

	public NeuralNetworkOutput classify(NeuralNetworkInput data) {
		NeuralFeature holdTime = data.getHoldTime();
		NeuralFeature interKeyTime = data.getInterKeyTime();
		INDArray features = Nd4j.create(
				new double[] { holdTime.getMean(), holdTime.getMinValue(), holdTime.getMaxValue(),
						holdTime.getVariance(), interKeyTime.getMean(), interKeyTime.getMinValue(),
						interKeyTime.getMaxValue(), interKeyTime.getVariance() });
		INDArray output = neuralNetwork.output(features);
		NeuralNetworkOutput objectOutput = convertOutputFromArray(output);
		return objectOutput;
	}

	private NeuralNetworkOutput convertOutputFromArray(INDArray output) {
		int id = 0;
		double maximum = 0;
		for (int i = 0; i < output.size(1); i++) {
			double element = output.getDouble(i);
			if (element > maximum) {
				maximum = element;
				id = i;
			}
		}
		return new NeuralNetworkOutput(maximum, id);
	}

	public int getNumberOfUsers() {
		return numberOfUsers;
	}

	public void setNumberOfUsers(int numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}

	public void recreateNeuralNetwork() {
		new File(neuralNetworkFileName).delete();
		createMultiLayerPerceptron();
	}

}
