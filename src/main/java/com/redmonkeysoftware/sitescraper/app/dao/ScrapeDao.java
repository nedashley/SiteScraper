package com.redmonkeysoftware.sitescraper.app.dao;

import com.redmonkeysoftware.sitescraper.logic.Link;
import com.redmonkeysoftware.sitescraper.logic.MiniScrape;
import com.redmonkeysoftware.sitescraper.logic.Scrape;
import java.util.List;

public interface ScrapeDao {

    public List<MiniScrape> lookupMiniScrapes();

    public Scrape lookupScrape(Long id);

    public Link lookupLink(Long id);

    public Scrape persistScrape(Scrape scrape);

    public Link persistLink(Long scrapeId, Link link);

    public Long fastPersistLink(Long scrapeId, Link link);

    public void deleteScrape(Long id);

}
