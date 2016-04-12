package com.redmonkeysoftware.sitescraper.app.service;

import com.redmonkeysoftware.sitescraper.logic.Link;

public interface ScrapingService {

    public void persistLink(Long scrapeId, Link link);
}
