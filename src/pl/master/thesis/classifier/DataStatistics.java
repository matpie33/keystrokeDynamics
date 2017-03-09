package pl.master.thesis.classifier;

public class DataStatistics {
	
	private double meanInterKeyTime;
	private double meanHoldTime;
	private double tabsPercent;
	
	public DataStatistics(double meanInterKeyTime, double meanHoldTime, double tabsPercent){
		this.meanHoldTime = meanHoldTime;
		this.meanInterKeyTime = meanInterKeyTime;
		this.tabsPercent = tabsPercent;
	}

	public double getMeanInterKeyTime() {
		return meanInterKeyTime;
	}

	public double getMeanHoldTime() {
		return meanHoldTime;
	}

	public double getTabsAmount() {
		return tabsPercent;
	}
	
	

}
