package com.redmonkeysoftware.sitescraper.app.service;

import com.redmonkeysoftware.sitescraper.logic.MiniScrape;
import com.redmonkeysoftware.sitescraper.logic.Scrape;
import java.util.List;

public interface ScrapeService {

    public List<MiniScrape> lookupMiniScrapes();

    public List<Scrape> createScrapes(String name, int maxDepth, String urls);

    public Scrape lookupScrape(Long id);

    public void deleteScrape(Long id);
    
    public void queueScrape(Scrape scrape);
}
