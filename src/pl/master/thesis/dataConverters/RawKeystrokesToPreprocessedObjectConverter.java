package pl.master.thesis.dataConverters;

import java.util.ArrayList;
import java.util.List;

import pl.master.thesis.keyTypingObjects.Digraph;
import pl.master.thesis.keyTypingObjects.InterKeyTime;
import pl.master.thesis.keyTypingObjects.KeyHoldingTime;
import pl.master.thesis.keyTypingObjects.PreprocessedKeystrokeData;

public class RawKeystrokesToPreprocessedObjectConverter {
	
	public List <PreprocessedKeystrokeData> convertKeyTypingFeatures(List <InterKeyTime> interKeyTimes, 
			List <KeyHoldingTime> keyHoldingTimes){
		System.out.println("before: "+keyHoldingTimes);
		sort(keyHoldingTimes);
		System.out.println("after sorting: "+keyHoldingTimes);
		List <PreprocessedKeystrokeData> list = new ArrayList <>();
		int i = 0;
		for (InterKeyTime interTime: interKeyTimes){
			KeyHoldingTime k1 = keyHoldingTimes.get(i);
			KeyHoldingTime k2 = keyHoldingTimes.get(i+1);
			System.out.println("digrah: "+interTime);
			i++;
			if (new Digraph(k1.getKey(),k2.getKey()).equals(interTime.getDigraph())){
				PreprocessedKeystrokeData p = new PreprocessedKeystrokeData(interTime);
				p.setKey1HoldTime(k1);
				p.setKey2HoldTime(k2);
				list.add(p);
				System.out.println("got match: "+interTime.getDigraph());
			}
			else{
				System.out.println("buuu: "+k1.getKey()+"/"+k2.getKey()+" digraph: "+interTime.getDigraph());
			}
		}
		System.out.println("list list: "+list);
		return list;
	}
	
	private void sort(List <KeyHoldingTime> keysHolds){
		keysHolds.sort( (o1,o2) -> o1.getPressedTime()<o2.getPressedTime() ? -1:1);
		System.out.println("in method: after sort: "+keysHolds);
		
	}

}
