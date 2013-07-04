package com.zackbleach.memetable.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.validator.routines.UrlValidator;

public class URLUtils {

	public static void validateUrl(String path) throws URISyntaxException {
		String[] schemes = { "http", "https" };
		UrlValidator urlValidator = new UrlValidator(schemes);
		if (!urlValidator.isValid(path)) {
			throw new URISyntaxException(path, "Invalid URL");
		}
	}

	public static String getDomainName(String url) throws URISyntaxException {
		URI uri = new URI(url);
		String domain = uri.getHost();
		domain = domain.startsWith("www.") ? domain.substring(4) : domain;
		String[] parts = domain.split("\\.");
		return parts[0];
	}
}
