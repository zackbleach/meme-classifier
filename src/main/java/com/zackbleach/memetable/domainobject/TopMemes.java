package com.zackbleach.memetable.domainobject;

import java.util.List;

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
