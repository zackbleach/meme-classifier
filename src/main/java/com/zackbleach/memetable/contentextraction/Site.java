package com.zackbleach.memetable.contentextraction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

//TODO: add commenting
public enum Site {

	QUICKMEME(ImmutableList.of("quickmeme", "qkme"), ImmutableList.of("img[id$=img]"), ImmutableList.of("meme_name")),
	IMGUR(ImmutableList.of("imgur"), ImmutableList.of("div.image img")),
	LIVEMEME(ImmutableList.of("livememe"), ImmutableList.of("img[id$=memeImage]")),
	MEMECREATOR(ImmutableList.of("memecreator"), ImmutableList.of("div.left img"));

	private final List<String> domains;
	private final List<String> imageExtractionPatterns;
	private final List<String> imageNameExtractionPatterns;

	private static final Map<String, Site> domainToSite;

	  static {
	    Map<String, Site> build = new HashMap<String, Site>();
	    for (Site site : Site.values()) {
	      for (String s : site.getDomains()) {
	    	  build.put(s, site);
	      }
	    }
	    domainToSite = Collections.unmodifiableMap(build);
	  }

	private Site(List<String> domains, List<String> imageExtractionPatterns,
			List<String> imageNameExtractionPatterns) {
		this.domains = domains;
		this.imageExtractionPatterns = imageExtractionPatterns;
		this.imageNameExtractionPatterns = imageNameExtractionPatterns;
	}

	private Site(List<String> domains, List<String> imageExtractionPatterns) {
		this.domains = domains;
		this.imageExtractionPatterns = imageExtractionPatterns;
		this.imageNameExtractionPatterns = new ArrayList<String>();
	}

	/**
	 * Determines the Site from domain
	 */
	public static Site siteFromDomain(String domain) {
		return domainToSite.get(domain);
	}

	public List<String> getDomains() {
		return domains;
	}

	public List<String> getImageExtractionPatterns() {
		return imageExtractionPatterns;
	}

	public List<String> getImageNameExtractionPatterns() {
		return imageNameExtractionPatterns;
	}

	public static Map<String, Site> getIdentifiertomeme() {
		return domainToSite;
	}

}
