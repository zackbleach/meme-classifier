package com.zackbleach.memetable.domainobject;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class TopMemes {

	private List<TopMeme> topMemes;
	
	public List<TopMeme> getTopMemes() {
		return topMemes;
	}

	public void setTopMemes(List<TopMeme> topMemes) {
		this.topMemes = topMemes;
	}
}
