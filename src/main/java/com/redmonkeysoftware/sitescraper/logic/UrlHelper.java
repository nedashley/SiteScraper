package com.redmonkeysoftware.sitescraper.logic;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

public class UrlHelper {

    private static final Logger logger = Logger.getLogger(UrlHelper.class.getName());
    private static UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});

    public static String formatAndValidateUrl(String url) {
        if (StringUtils.isNotBlank(url)) {
            url = StringUtils.lowerCase(url);
            if (!StringUtils.startsWith(url, "http")) {
                if (StringUtils.startsWith(url, ":")) {
                    url = "http" + url;
                } else if (StringUtils.startsWith(url, "//")) {
                    url = "http:" + url;
                } else if (StringUtils.startsWith(url, "/")) {
                    url = "http:/" + url;
                } else {
                    url = "http://" + url;
                }
            }
            url = StringUtils.trim(url);
            if (urlValidator.isValid(url)) {
                try {
                    url = getUrl(url);
                    return url;
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Error with input URL: " + url, e);
                }
            }
        }
        return null;
    }

    public static boolean sameHost(String original, String test) {
        try {
            return StringUtils.equalsIgnoreCase(getDomainName(original), getDomainName(test));
        } catch (Exception e) {
        }
        return false;
    }

    protected static String getDomainName(String input) throws URISyntaxException {
        URI uri = new URI(input);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    protected static String getUrl(String input) throws URISyntaxException {
        URI uri = new URI(input);
        return uri.toString();
    }
}
