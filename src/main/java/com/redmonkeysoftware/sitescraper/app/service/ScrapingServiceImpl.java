package com.redmonkeysoftware.sitescraper.app.service;

import com.redmonkeysoftware.sitescraper.app.dao.ScrapeDao;
import com.redmonkeysoftware.sitescraper.logic.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScrapingServiceImpl implements ScrapingService {

    @Autowired
    private ScrapeDao scrapeDao;

    @Override
    @Transactional(readOnly = true)
    public Link lookupLink(Long linkId) {
        return scrapeDao.lookupLink(linkId);
    }
    
    @Override
    @Transactional
    public void persistLink(Long scrapeId, Link link) {
        scrapeDao.fastPersistLink(scrapeId, link);
    }
}
