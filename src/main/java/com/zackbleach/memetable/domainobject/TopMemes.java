package com.zackbleach.memetable.domainobject;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.zackbleach.memetable.imagerecognition.Meme;
import com.zackbleach.memetable.imagerecognition.Result;

public class TopMemes {

	List<TopMeme> topMemes;

	public TopMemes() {

	}

	public List<TopMeme> getTopMemes() {
		return topMemes;
	}

	public void setTopMemes(List<TopMeme> customSortingMap) {
		this.topMemes = customSortingMap;
	}
	
}
