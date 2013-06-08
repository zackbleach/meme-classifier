package com.zackbleach.memetable.imagerecognition;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public enum Meme {
	
	AND_ITS_GONE("South Park: aaaaaand", "aaand"),
	ADVICE_DOG("Advice Dog", "advicedog"),
	ADVICE_MALLARD("Actual Advice Mallard", "advicemallard"),
	AM_I_THE_ONLY_ONE_AROUND_HERE("Am I The Only One Around Here", "amitheonlyonearoundhere"),
	BAD_ADVICE_MALLARD("Bad Advice Mallard", "badadvicemallard"),
	BRACE_YOURSELVES("Brace Yourselves", "braceyourselves"),
	CATS("Cats", "cats"),
	CAPTAIN_HINDSIGHT("Captain Hindsight", "captainhindsight"),
	COLLEGE_HIPPY("College Hippy", "collegehippy"),
	BAD_LUCK_BRIAN("Bad Luck Brian", "badluckbrian"),
	BOLD_MOVE_COTTON("Bold Move Cotton", "boldmovecotton"),
	BUSINESS_CAT("Business Cat", "businesscat"),
	CANT_WAIT_TO_GET_HOME("I Can't Wait To Get Home", "cantwaittogethome"),
	CLARITY_CLARENCE("Sudden Clarity Clarence", "clarityclarence"),
	CONFESSION_BEAR("Confession Bear", "confessionbear"),
	COURAGE_WOLF("Courage Wolf", "couragewolf"),
	EMBARASSED_KID("Embarassed Kid", "embarassedkid"),
	FOUL_BACHELOR("Foul Bachelor", "foulbachelor"),
	FIRST_WORLD_PROBLEMS("First World Problems", "firstworldproblems"),
	FUTURAMA_FRY("Futurama Fry", "futuramafry"),
	GOOD_GIRL_GINA("Good Girl Gina", "goodgirlgina"),
	GOOD_GUY_GREG("Good Guy Greg", "goodguygreg"),
	GOOD_GUY_OBAMA("Good Guy Obama", "goodguyobama"),
	INTERNET_WIFE("Internet Wife", "interwebwife"),
	INSANITY_WOLF("Insanity Wolf", "insanitywolf"),
	I_SHOULD_BUY_A_BOAT("I Should Buy a Boat Cat", "ishouldbuyaboat"),
	I_WOULD_BE_SO_HAPPY("South Park: I woul dbe so happy", "iwouldbesohappy"),
	JOKER("Joker", "joker"),
	OVERLY_ATTACHED_GIRLFRIEND("Overly Attached Girlfriend", "overlyattachedgirlfriend"),
	PEPPERIDGE_FARM("Pepperidge Farm Remembers", "pepperidgefarm"),
	PICCARD("Piccard", "piccard"),
	PHILOSORAPTOR("Philosoraptor", "philosoraptor"),
	SCUMBAG_BRAIN("Scumbag Brain", "scumbagbrain"),
	SCUMBAG_STACEY("Scumbag Stacey", "scumbagstacey"),
	SCUMBAG_STEVE("Scumbag Steve", "scumbagsteve"),
	SCUMBAG_TEACHER("Scumbag Teacher", "scumbagteacher"),
	SOCIALLY_AWKWARD_PENGUIN("Socially Awkward Penguin", "sociallyawkwardpenguin"),
	SOCIALL_AWKWARD_AWESOME_PENGUIN("Socially Awkward Awesome Penguin", "sociallyawkwardawesomepenguin"),
	SPONGEBOB("Sponge Bob", "spongebob"),
	SUCCESS_KID("Success Kid", "successkid"),
	THATD_BE_GREAT("That'd be Great", "thatdbegreat"),
	TECH_IMPARIED_DUCK("Tech Impaired Duck", "techimpairedduck"),
	TEN_GUY("Ten Guy", "10guy"),
	THE_MOST_INTERESTING_MAN_IN_THE_WORLD("The Most Interesting Man In The World", "themostinterestingmanintheworld"),
	TODAY_WAS_A_GOOD_DAY("Today Was A Good Day", "todaywasagoodday"),
	WHAT_IF_I_TOLD_YOU("What If I Told You", "whatifitoldyou");
	
	private final String identifier;
	private final String absoluteDirectory;
	private final String directory;
	
	public String getDirectory() {
		return directory;
	}

	private static final Map<String, Meme> identifierToMeme;

	  static {
	    Map<String, Meme> build = new HashMap<String, Meme>();
	    for (Meme meme : Meme.values()) {
	      build.put(meme.absoluteDirectory, meme);
	    }
	    identifierToMeme = Collections.unmodifiableMap(build);
	  }
	
	private Meme(String identifier, String directory) {
		this.identifier = identifier;
		this.absoluteDirectory = Indexer.MEME_LOCATION + directory;
		this.directory = directory;
	}

	public String identifier() {
		return identifier;
	}

	public String directory() {
		return absoluteDirectory;
	}
	
	/**
	 * Determines the Meme from the path to the file
	 */
	public static Meme typeByPath(String path) {
		
		File file = new File(path);
		String parentDir = file.getParent();
		
		Meme meme = identifierToMeme.get(parentDir);
		if (meme == null) {
			throw new IllegalArgumentException("Could not determine Meme type from path: " + path);
		}
		return meme;
	}
	
}
