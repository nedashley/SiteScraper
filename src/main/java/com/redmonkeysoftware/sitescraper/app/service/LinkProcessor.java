package com.redmonkeysoftware.sitescraper.app.service;

import com.redmonkeysoftware.sitescraper.logic.InnerLink;
import com.redmonkeysoftware.sitescraper.logic.Link;
import com.redmonkeysoftware.sitescraper.logic.UrlHelper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkProcessor implements Runnable {

    private static final Logger logger = Logger.getLogger(LinkProcessor.class.getName());
    private final ScrapingService service;
    private final Long scrapeId;
    private final Long linkId;
    private final int maxDepth;
    private CloseableHttpClient client;
    private RequestConfig requestConfig;
    private Pattern emailRegex;
    private Link link;

    public LinkProcessor(final ScrapingService service, final Long scrapeId, final int maxDepth, final Long linkId) {
        this.service = service;
        this.scrapeId = scrapeId;
        this.maxDepth = maxDepth;
        this.linkId = linkId;
    }

    @Override
    public void run() {
        try {
            //initialise components
            requestConfig = RequestConfig.custom().setConnectionRequestTimeout(4000).setConnectTimeout(4000).setSocketTimeout(4000).build();
            client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
            emailRegex = Pattern.compile("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b", Pattern.CASE_INSENSITIVE);
            link = service.lookupLink(linkId);

            if (link != null) {
                link.setStarted(LocalDateTime.now());
                HttpGet request = new HttpGet(link.getUrl());
                try (CloseableHttpResponse response = client.execute(request)) {
                    link.setResolves(true);
                    link.setResponse(response.getStatusLine().getStatusCode());
                    String contentMimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
                    if (StringUtils.equalsIgnoreCase(contentMimeType, ContentType.TEXT_HTML.getMimeType())) {
                        if ((link.getResponse() >= 200) && (link.getResponse() < 300)) {
                            String bodyAsString = EntityUtils.toString(response.getEntity());
                            parseEmails(bodyAsString);
                            if (maxDepth > 1) {
                                for (String url : parseLinks(bodyAsString, link.getUrl())) {
                                    url = UrlHelper.formatAndValidateUrl(url);
                                    if ((url != null) && (UrlHelper.sameHost(link.getUrl(), url))) {
                                        InnerLink innerLink = probeUrl(url, 2);
                                        link.getInnerLinks().add(innerLink);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                logger.log(Level.WARNING, "Could not find link: {0}", linkId);
            }
        } catch (IOException | ParseException e) {
            logger.log(Level.FINE, "Error processing Link", e);
            link.setResolves(false);
            link.setResponse(-1);
        } finally {
            link.setFinished(LocalDateTime.now());
            service.persistLink(scrapeId, link);
            try {
                client.close();
            } catch (Exception e) {
            }
        }
    }

    protected InnerLink probeUrl(String url, int depth) {
        InnerLink il = new InnerLink();
        try {
            il.setUrl(url);
            il.setDepth(depth);
            il.setStarted(LocalDateTime.now());
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = client.execute(request)) {
                il.setResponse(response.getStatusLine().getStatusCode());
                String contentMimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
                if (StringUtils.equalsIgnoreCase(contentMimeType, ContentType.TEXT_HTML.getMimeType())) {
                    if ((il.getResponse() >= 200) && (il.getResponse() < 300)) {
                        String bodyAsString = EntityUtils.toString(response.getEntity());
                        parseEmails(bodyAsString);
                        if ((depth + 1) < maxDepth) {
                            for (String innerUrl : parseLinks(bodyAsString, url)) {
                                innerUrl = UrlHelper.formatAndValidateUrl(innerUrl);
                                if ((innerUrl != null) && (UrlHelper.sameHost(link.getUrl(), url))) {
                                    InnerLink innerLink = probeUrl(innerUrl, depth + 1);
                                    link.getInnerLinks().add(innerLink);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException | ParseException e) {
            logger.log(Level.FINE, "Error dealing with Inner Link", e);
            il.setResponse(-1);
        } finally {
            il.setFinished(LocalDateTime.now());
        }
        return il;
    }

    protected List<String> parseLinks(String body, String baseUrl) {
        List<String> results = new ArrayList<>();
        try {
            Document doc = Jsoup.parse(body, baseUrl);
            Elements links = doc.select("a[href]");
            for (Element innerLink : links) {
                String url = innerLink.attr("abs:href");
                url = UrlHelper.formatAndValidateUrl(url);
                if (url != null) {
                    if (StringUtils.startsWith(url, "/")) {
                        url = link.getUrl() + url;
                    }
                    results.add(url);
                }
            }
        } catch (Exception e) {
            logger.log(Level.FINE, "Error parsing Body Links", e);
        }
        return results;
    }

    protected void parseEmails(String body) {
        try {
            Matcher matcher = emailRegex.matcher(body);
            while (matcher.find()) {
                link.getEmails().add(matcher.group());
            }
        } catch (Exception e) {
            logger.log(Level.FINE, "Error parsing Body Emails", e);
        }
    }

}
