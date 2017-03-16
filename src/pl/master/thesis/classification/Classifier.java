package pl.master.thesis.classification;

import org.apache.commons.math3.stat.inference.TTest;

public class Classifier {
	private KeystrokeDataArray dataSet1;
	private KeystrokeDataArray dataSet2;
	
	public Classifier(){
		dataSet1 = new KeystrokeDataArray();
		dataSet2 = new KeystrokeDataArray();
	}
	
	public void saveInputData(KeystrokeDataArray dataSet1, KeystrokeDataArray dataSet2){
		this.dataSet1 = dataSet1;
		this.dataSet2 = dataSet2;
	}
	
	public void compare(){
		removeOutliers();
		doComparation();
		
	}
	
	private void removeOutliers(){
		dataSet1.removeOutliers();
		dataSet2.removeOutliers();
	}
	
	private void doComparation(){
		boolean holdTimeMeansAreSame = compareMeans(dataSet1.getHoldTimes(), dataSet2.getHoldTimes());
		boolean interKeyTimeMeansAreSame = compareMeans(dataSet1.getInterKeyTimes(), 
				dataSet2.getInterKeyTimes());
	}
	
	private boolean compareMeans(double [] sample1, double [] sample2){
		TTest test = new TTest();
		double d = test.tTest(sample1, sample2);
		System.out.println("prawdopodobienstwo jest: "+d);

		return test.homoscedasticTTest(sample1, sample2, 0.05);
	}

}
