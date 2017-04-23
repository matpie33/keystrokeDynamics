package pl.master.thesis.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
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
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeepLearning4jUsing {

	private static Logger log = LoggerFactory.getLogger(DeepLearning4jUsing.class);

	public static void use() throws FileNotFoundException, IOException, InterruptedException {
		// First: get the dataset using the record reader. CSVRecordReader
		// handles loading/parsing
		int numLinesToSkip = 0;
		String delimiter = ", ";
		RecordReader recordReader = new CSVRecordReader(numLinesToSkip, delimiter);
		recordReader.initialize(new FileSplit(new File("trainingData.txt")));

		// Second: the RecordReaderDataSetIterator handles conversion to DataSet
		// objects, ready for use in neural network
		int labelIndex = 8; // 5 values in each row of the iris.txt CSV: 4
							// input
							// features followed by an integer label (class)
							// index. Labels are the 5th value (index 4) in
							// each
							// row
		int numClasses = 56; // 3 classes (types of iris flowers) in the iris
								// data set. Classes have integer values 0, 1 or
								// 2
		int batchSize = 20000; // Iris data set: 150 examples total. We are
								// loading all of them into one DataSet (not
								// recommended for large data sets)

		DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader, batchSize,
				labelIndex, numClasses);
		DataSet allData = iterator.next();
		allData.shuffle();
		SplitTestAndTrain testAndTrain = allData.splitTestAndTrain(0.1); // Use
																			// 65%
																			// of
																			// data
																			// for
																			// training

		DataSet trainingData = testAndTrain.getTrain();
		DataSet testData = testAndTrain.getTest();

		// We need to normalize our data. We'll use NormalizeStandardize (which
		// gives us mean 0, unit variance):
		DataNormalization normalizer = new NormalizerStandardize();
		normalizer.fit(trainingData); // Collect the statistics (mean/stdev)
										// from the training data. This does not
										// modify the input data
		normalizer.transform(trainingData); // Apply normalization to the
											// training data
		normalizer.transform(testData); // Apply normalization to the test data.
										// This is using statistics calculated
										// from the *training* set

		final int numInputs = labelIndex;
		int outputNum = numClasses;
		int iterations = 6000;
		long seed = 6;
		int hiddenLayer1Neurons = 50;
		int hiddenLayer2Neurons = 100;

		log.info("Build model....");
		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().seed(seed)
				.iterations(iterations).activation(Activation.TANH).weightInit(WeightInit.XAVIER)
				.learningRate(0.1).list()
				.layer(0, new DenseLayer.Builder().nIn(numInputs).nOut(hiddenLayer1Neurons).build())
				// .layer(1,
				// new
				// DenseLayer.Builder().nIn(hiddenLayer1Neurons).nOut(hiddenLayer2Neurons)
				// .build())
				.layer(1,
						new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
								.activation(Activation.SOFTMAX).nIn(hiddenLayer1Neurons)
								.nOut(outputNum).build())
				.backprop(true).pretrain(false).build();

		// run the model
		MultiLayerNetwork model = new MultiLayerNetwork(conf);
		model.init();
		model.setListeners(new ScoreIterationListener(100));

		model.fit(trainingData);

		// evaluate the model on the test set
		Evaluation eval = new Evaluation(3);
		INDArray output = model.output(testData.getFeatureMatrix());
		eval.eval(testData.getLabels(), output);
		log.info(eval.stats());
	}

}
