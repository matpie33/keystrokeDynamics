package pl.master.thesis.keyEventHandler;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.master.thesis.classifier.StatisticalClassifier;
import pl.master.thesis.keyTypingObjects.Digraph;
import pl.master.thesis.keyTypingObjects.InterKeyTime;
import pl.master.thesis.keyTypingObjects.KeyHoldingTime;
import pl.master.thesis.keyTypingObjects.KeystrokeDataBeforePreprocessing;

public class KeyEventHandler {

	private int keysTyped;
	private List <Long> interKeysTimes;
	private long lastKeyReleasedTime;
	private long lastKeyPressedTime;
	private long maxBreakTime = 2_000;
	private double meanTypeSpeed;
	private int breaks;
	private int errors;
	private Map <String, Long> currentlyPressedKeys;
	private List <KeyHoldingTime> keyHoldingTime;
	private String lastKeyPressed;
	private List <InterKeyTime> interKeyTimes;
	private KeystrokeDataBeforePreprocessing passwordFeatures;
	private KeystrokeDataBeforePreprocessing repeatPasswordFeatures;
	private KeystrokeDataBeforePreprocessing userNameFeatures;
	private StatisticalClassifier statisticalClassifier;
	private SavingMode savingMode;
	private final String TAB_KEY = "Tab";
	
	public KeyEventHandler(){
		savingMode = SavingMode.USERDATA;
		passwordFeatures = new KeystrokeDataBeforePreprocessing ();
		userNameFeatures = new KeystrokeDataBeforePreprocessing ();
		repeatPasswordFeatures = new KeystrokeDataBeforePreprocessing ();
		statisticalClassifier = new StatisticalClassifier ();
		interKeysTimes = new ArrayList <> ();
		keyHoldingTime = new ArrayList <>();
		interKeyTimes = new ArrayList <> ();
		currentlyPressedKeys=new HashMap <>();
		lastKeyPressed = "";
	}

	public void stopTimerAndCalculateSpeed(){
		
		long sum = 0;
		for (Long l: interKeysTimes){
//			if (l>maxBreakTime){
//				if (keysTyped>0){
//					keysTyped--;
//				}
//				continue;
//			}
			sum+=l;
		}
		for (KeyHoldingTime l: keyHoldingTime){ //TODO think about it, do we not duplicate times in worst case?
			sum+=l.getHoldTime();
		}
		double mean =((double)keysTyped*60D/((double)sum/(1_000_000_000D)));
		if (sum==0){
			return;
		}
		meanTypeSpeed*=breaks++;
		meanTypeSpeed+=mean;
		meanTypeSpeed/=breaks;
	}
	
	public void recordKeyPress(KeyEvent e){		
		String currentKey = getKeyName(e.getKeyCode());
		long interKeyTime = calculateInterKeyTime();
		if (currentlyPressedKeys.containsKey(currentKey)){
			System.out.println("returning coz i have: "+currentKey);
			return;
		}
		
    	saveKeyPress(currentKey, interKeyTime);
		if (!lastKeyPressed.isEmpty()&& !lastKeyPressed.equals(TAB_KEY) && !currentKey.equals(TAB_KEY)){
			saveInterKeyTime(new InterKeyTime (new Digraph(lastKeyPressed, currentKey ), interKeyTime));
		}
		lastKeyPressed = currentKey;
		if (e.getKeyCode()==KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE){
			recordMistypedKey();
		}
		
	}
	
	private void saveInterKeyTime(InterKeyTime interKeyTime){
		switch (savingMode){
		case USERDATA:
			interKeyTimes.add(interKeyTime);
			break;
		case USERNAME:
			userNameFeatures.addInterKeyTime(interKeyTime);
			break;
		case PASSWORD:
			passwordFeatures.addInterKeyTime(interKeyTime);
			break;
		}
	}
	
	private long calculateInterKeyTime(){
		long lastKeyEventTime = Math.max(lastKeyPressedTime, lastKeyReleasedTime);
    	lastKeyPressedTime = System.nanoTime();
    	return lastKeyPressedTime-lastKeyEventTime;
	}
	
	private void saveKeyPress(String currentKey, long interKeyTime){
		currentlyPressedKeys.put(currentKey, lastKeyPressedTime);
		interKeysTimes.add(interKeyTime);
		keysTyped++;
	}
	
	private String getKeyName (int keyCode){
		return KeyEvent.getKeyText(keyCode);
	}
	
	public void recordKeyRelease(KeyEvent e){
		lastKeyReleasedTime = System.nanoTime();
		int keyCode = e.getKeyCode();		
		String keyName = getKeyName(keyCode);
		long pressTime = currentlyPressedKeys.get(keyName)!=null?currentlyPressedKeys.get(keyName):0;
		if (pressTime==0){
			return;
		}
		long holdingTime = lastKeyReleasedTime-pressTime;
		currentlyPressedKeys.remove(keyName);
		Digraph d = new Digraph(lastKeyPressed, keyName);
		if (!keyName.equals(TAB_KEY)){
			saveHoldingTime(d, new KeyHoldingTime(keyName,holdingTime, pressTime));
		}
		
	}
	
	private void saveHoldingTime(Digraph d,KeyHoldingTime holdTime){
		
		switch (savingMode){
		case USERDATA:
			keyHoldingTime.add(holdTime);
			break;
		case USERNAME:
			userNameFeatures.addUsernameHoldingTime(d, holdTime);
			break;
		case PASSWORD:
			passwordFeatures.addUsernameHoldingTime(d, holdTime);
			break;
		case REPEAT_PASSWORD:
			repeatPasswordFeatures.addUsernameHoldingTime(d, holdTime);
		}
	}
	
	private void recordMistypedKey(){
		errors++;
	}
	
	public double getMeanTypeSpeed(){
		return meanTypeSpeed;
	}
	
	public int getErrors(){
		return errors;
	}
	
	public int getNumberOfTypedKeys(){
		return keysTyped;
	}
	
	public void calculateFrequenciesMap(){
		InterKeyTime.getFrequencies(interKeyTimes);
	}
	
	public void setMode(SavingMode mode){
		System.out.println("mode now: "+mode.name());
		savingMode = mode;
	}
	
	public void show(){
		//TODO remove it later
//		System.out.println("pass interkeys: "+passwordFeatures.getInterKeyTimes());
//		System.out.println("pass hold times: "+passwordFeatures.getKeyHoldTimes());
//		System.out.println("username  interkeys: "+userNameFeatures.getInterKeyTimes());
//		System.out.println("username hold times: "+userNameFeatures.getKeyHoldTimes());
		if (passwordFeatures.getInterKeyTimes().isEmpty()){
			return;
		}
		statisticalClassifier.calculateInverseSquareRootOfCovarianceMatrix(
				passwordFeatures.convertToMatrix());
	}
	
}
