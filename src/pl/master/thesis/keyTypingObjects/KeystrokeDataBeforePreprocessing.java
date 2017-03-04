package pl.master.thesis.keyTypingObjects;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;

import pl.master.thesis.dataConverters.KeystrokeDataToArrayConverter;
import pl.master.thesis.dataConverters.RawKeystrokesToPreprocessedObjectConverter;

public class KeystrokeDataBeforePreprocessing {
	
	private List <PreprocessedKeystrokeData> digraphFeatures;
	private List <InterKeyTime> interKeyTimes;
	private List <KeyHoldingTime> holdTimes;
	private RawKeystrokesToPreprocessedObjectConverter converter;
	private KeystrokeDataToArrayConverter dataToArrayConverter;
	
	public KeystrokeDataBeforePreprocessing(){
		interKeyTimes = new ArrayList <>();
		holdTimes = new ArrayList <>();
		converter = new RawKeystrokesToPreprocessedObjectConverter();
		dataToArrayConverter = new KeystrokeDataToArrayConverter();
		digraphFeatures = new ArrayList <> ();
	}
	
	public List<InterKeyTime> getInterKeyTimes() {
		return interKeyTimes;
	}
	public List<KeyHoldingTime> getUsernameHoldTimes() {
		return holdTimes;
	}
	
	public void addInterKeyTime(InterKeyTime inter){
		interKeyTimes.add(inter);
	}

	public void addUsernameHoldingTime(Digraph d, KeyHoldingTime hold){
		for (PreprocessedKeystrokeData time: digraphFeatures){
			if (time.getDigraph().equals(d) ){
				if (!time.hasKey1Set()){
					time.setKey1HoldTime(hold);
				}
				else if (!time.hasKey2Set()){
					time.setKey2HoldTime(hold);
				}
			}
		}
		holdTimes.add(hold);
	}
	
	public RealMatrix convertToMatrix(){
		RealMatrix matrix = dataToArrayConverter.convertPreprocessedDataToMatrix(
				converter.convertKeyTypingFeatures(interKeyTimes, holdTimes));
		return matrix;
	}
	
	
	
}
