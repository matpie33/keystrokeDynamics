package pl.master.thesis.crossValidation;

import java.util.List;

import org.nd4j.linalg.api.ndarray.INDArray;

public interface AcceptingStrategy {

	public List<Boolean> isUserAccepted(INDArray dataset, INDArray neuralNetworkOutput);

}
