package pl.master.thesis.classification;

import java.util.ArrayList;
import java.util.List;

import pl.master.thesis.features.Feature;

public class DataStatistics {
	
	private List <Feature> features;
	private String userId;

	public DataStatistics(String userId){
		this.userId = userId;
		features = new ArrayList <>();
	}

	public void addFeature (Feature f){
		features.add(f);
	}

	public String getUserId() {
		return userId;
	}
	
	public List <Feature> getFeatures(){
		return features;
	}
	
	public void setFeatureWeight(String featureName, double weight){
		for (Feature f: features){
			if (f.getName().equals(featureName)){
				f.setWeight(weight);
			}
		}
	}
	

}
