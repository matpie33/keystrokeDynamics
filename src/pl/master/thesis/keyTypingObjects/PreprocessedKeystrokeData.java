package pl.master.thesis.keyTypingObjects;

public class PreprocessedKeystrokeData {
	
	public InterKeyTime getInterKeyTime() {
		return interKeyTime;
	}

	public KeyHoldingTime getKey1HoldingTime() {
		return key1HoldingTime;
	}

	public KeyHoldingTime getKey2HoldingTime() {
		return key2HoldingTime;
	}

	private InterKeyTime interKeyTime;
	private KeyHoldingTime key1HoldingTime;
	private KeyHoldingTime key2HoldingTime;
	
	public PreprocessedKeystrokeData (InterKeyTime interTime){
		interKeyTime = interTime;
	}
	
	public void setKey1HoldTime(KeyHoldingTime time){
		key1HoldingTime = time; 
	}
	
	public void setKey2HoldTime(KeyHoldingTime time){
		key2HoldingTime = time;
	}

	public Digraph getDigraph(){
		return interKeyTime.getDigraph();
	}
	
	public boolean hasKey1Set(){
		return key1HoldingTime != null;
	}
	
	public boolean hasKey2Set(){
		return key2HoldingTime != null;
	}
	
	@Override
	public String toString(){
		return "inter time: "+ interKeyTime+" hold1: "+key1HoldingTime+" hold2 "+key2HoldingTime;
	}
	
}
