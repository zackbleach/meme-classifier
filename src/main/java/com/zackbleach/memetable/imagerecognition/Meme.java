package com.zackbleach.memetable.imagerecognition;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public enum Meme {
	
	INSANITY_WOLF("Insanity Wolf", "insanitywolf"),
	PHILOSORAPTOR("Philosoraptor", "philosoraptor"),
	INTERNET_WIFE("Internet Wife", "interwebwife"),
	SCUMBAG_STEVE("Scumbag Steve", "scumbagsteve"),
	FOUL_BACHELOR("Foul Bachelor", "foulbachelor"),
	ADVICE_MALLARD("Actual Advice Mallard", "advicemallard"),
	SUCCESS_KID("Success Kid", "successkid"),
	BAD_ADVICE_MALLARD("Bad Advice Mallard", "badadvicemallard"),
	GOOD_GUY_GREG("Good Guy Greg", "goodguygreg"),
	CATS("Cats", "cats");

	private final String identifier;
	private final String directory;
	
	private static final Map<String, Meme> identifierToMeme;

	  static {
	    Map<String, Meme> build = new HashMap<String, Meme>();
	    for (Meme meme : Meme.values()) {
	      build.put(meme.directory, meme);
	    }
	    identifierToMeme = Collections.unmodifiableMap(build);
	  }
	
	private Meme(String identifier, String directory) {
		this.identifier = identifier;
		this.directory = Indexer.MEME_LOCATION + directory;
	}

	public String identifier() {
		return identifier;
	}

	public String directory() {
		return directory;
	}
	
	/**
	 * Determines the Meme from the path to the file
	 */
	public static Meme typeByPath(String path) {
		String[] parts = path.split("/");
		String newPath = StringUtils.join(Arrays.copyOfRange(parts, 0, parts.length-1), "/");
		
		Meme meme = identifierToMeme.get(newPath);
		if (meme != null) {
			return meme;
		}
		
		throw new IllegalArgumentException("Could not determine Meme type from path: " + path);
	}
	
}
