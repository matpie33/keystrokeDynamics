package pl.master.thesis.timing;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Timing {

	private Timer timer;
	private int time;
	private byte TIME_DELAY=10;
	private int keysTyped;
	private int typingTime;
	private boolean isRunning;
	private List <Long> intervals;
	private long lastKeyReleasedTime;
	private long lastKeyPressedTime;
	private long maxBreakTime = 2_000;
	private double meanTypeSpeed;
	private int breaks;
	private int errors;
	private int sumTypedWords;
	
	
	public Timing(){
		isRunning = false;
		intervals = new ArrayList <Long> ();
	}
	
	public void cleanStopWatch(){
		time=0;    	
		if (!isRunning){
			startTimer();
		}
		
    }
	
	private void startTimer() {
		keysTyped=0;
		timer=new Timer();  
		isRunning=true;
		typingTime=0;
		intervals.clear();
	    TimerTask task=new TimerTask(){
	        @Override
	        public void run(){       
	            time+=TIME_DELAY; 
	            typingTime +=TIME_DELAY;
	            System.out.println(time);
	            if (time>maxBreakTime){
	            	stopTimerAndCalculateSpeed();
	            }
	        }
	    };
	    timer.scheduleAtFixedRate(task, 0, TIME_DELAY);
	}

	public void stopTimerAndCalculateSpeed(){
		
		if (!isRunning){
			return;
		}
		long breakk = System.nanoTime()-lastKeyReleasedTime;
//		typingTime-=breakk;
		sumTypedWords+=keysTyped;
		isRunning = false;
		timer.cancel();
		
		long sum = 0;
		for (Long l: intervals){
			sum+=l;
		}
		double mean =((double)keysTyped/((double)typingTime/1000D/60D));
		meanTypeSpeed*=breaks++;
		meanTypeSpeed+=mean;
		meanTypeSpeed/=breaks;
		System.out.println("typed: "+keysTyped+" in time: "+typingTime);
		System.out.println("srednio: "+ mean);
	}
	
	public void recordKeyPress(KeyEvent e){
		System.out.println(e.getKeyCode());
		if (e.getKeyCode() == KeyEvent.VK_SHIFT || e.getKeyCode()== KeyEvent.VK_ALT || e.getKeyCode() == KeyEvent.VK_CONTROL){
			System.out.println("SHIFT lub ALT");
			return;
		}
		if (e.getKeyCode()==KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE){
			System.out.println("Backspace");
			recordMistypedKey();
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_TAB){
			System.out.println("Tab");
			return;
		}
    	long lastKeyTime = Math.max(lastKeyPressedTime, lastKeyReleasedTime);
    	lastKeyPressedTime = System.nanoTime();
    	cleanStopWatch();
    	if (lastKeyTime>0){
    		keysTyped++;
    		intervals.add(lastKeyPressedTime-lastKeyTime);
    		System.out.println(intervals.get(intervals.size()-1));
    	}
	}
	
	public void recordKeyRelease(KeyEvent e){
		lastKeyReleasedTime = System.nanoTime();
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
		return sumTypedWords;
	}
	
	
}
