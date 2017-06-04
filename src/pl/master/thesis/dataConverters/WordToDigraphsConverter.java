package pl.master.thesis.dataConverters;

import java.util.ArrayList;
import java.util.List;

import pl.master.thesis.keyTypingObjects.Digraph;
import pl.master.thesis.keyTypingObjects.InterKeyTime;
import pl.master.thesis.keyTypingObjects.KeyHoldingTime;
import pl.master.thesis.keyTypingObjects.DigraphTimingData;
import pl.master.thesis.keyTypingObjects.WordKeystrokeData;

public class WordToDigraphsConverter {

	public List<DigraphTimingData> convertSingleWordToSimplestData(
			WordKeystrokeData wordsData) {
		sort(wordsData.getHoldTimes());
		List<DigraphTimingData> list = new ArrayList<>();
		int i = 0;
		for (InterKeyTime interTime : wordsData.getInterKeyTimes()) {
			KeyHoldingTime k1 = wordsData.getHoldTimes().get(i);
			KeyHoldingTime k2 = wordsData.getHoldTimes().get(i + 1);
			i++;
			if (new Digraph(k1.getKey(), k2.getKey()).equals(interTime.getDigraph())) {
				DigraphTimingData p = new DigraphTimingData(interTime);
				p.setKey1HoldTime(k1);
				p.setKey2HoldTime(k2);
				p.setInterKeyTime(interTime);
				p.setStartedWithTab(wordsData.isStartedWithTab());
				p.setKeysPressedTogether(wasKeysPressedTogether(k1, k2));
				list.add(p);
			}

		}
		return list;
	}

	private void sort(List<KeyHoldingTime> keysHolds) {
		keysHolds.sort((o1, o2) -> o1.getPressedTime() < o2.getPressedTime() ? -1 : 1);
	}

	private boolean wasKeysPressedTogether(KeyHoldingTime k1, KeyHoldingTime k2) {
		return k1.getPressedTime() + k1.getHoldTime() - k2.getPressedTime() > 0; // TODO
																					// check
																					// it
	}

}
