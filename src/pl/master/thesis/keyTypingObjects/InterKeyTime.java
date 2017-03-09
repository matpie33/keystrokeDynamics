package pl.master.thesis.keyTypingObjects;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InterKeyTime {
	
	private Digraph digraph;
	private long interKeyTime;
	
	public InterKeyTime (Digraph digraph, long interKeyTime){
		this.digraph = digraph;
		this.interKeyTime = interKeyTime;
	}
	
	public static Map <Digraph, Integer> getFrequencies(List <InterKeyTime> list){
		Map <Digraph, Integer> frequenciesMap = new HashMap <> ();
		for (InterKeyTime keyTime: list){
			Digraph digraph = keyTime.getDigraph();
			frequenciesMap.put(digraph, findFrequencyOfDigraph (list, digraph));
		}
		Map <Digraph, Integer> sorted = sortByValues(frequenciesMap);

		System.out.println("frequencies map: "+sorted);
		return frequenciesMap;
	}
	
	private static int findFrequencyOfDigraph (List <InterKeyTime> list, Digraph d){
		int result = 0;
		for (InterKeyTime ikt: list){
			if (ikt.getDigraph().equals(d)){
				result++;
			}
		}
		return result;
	}
	
	private static Map <Digraph, Integer> sortByValues(Map <Digraph,Integer> map){
		return map.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
				.collect(Collectors.toMap(Map.Entry::getKey,
						Map.Entry::getValue,
						(e1,e2) -> e1,
						LinkedHashMap::new
						));
		}
	
	public String toString(){
//		return digraph+" time: "+interKeyTime;
		return ""+interKeyTime;
	}

	public Digraph getDigraph() {
		return digraph;
	}

	public long getInterKeyTime() {
		return interKeyTime;
	}
	
	public boolean isItTimeBetweenSameKey(){
		return digraph.isSameKey();
	}


}
