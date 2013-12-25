package com.zackbleach.memetable.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.validator.routines.UrlValidator;

public class URLUtils {

    //TODO: surely there are libraries that do a better job than this
    public static void validateUrl(String path) throws URISyntaxException {
        String[] schemes = {"http", "https", "file"};
        UrlValidator urlValidator = new UrlValidator(schemes, UrlValidator.ALLOW_LOCAL_URLS);
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
