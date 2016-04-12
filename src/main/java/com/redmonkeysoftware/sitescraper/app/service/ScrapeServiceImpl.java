package com.redmonkeysoftware.sitescraper.app.service;

import com.redmonkeysoftware.sitescraper.app.dao.ScrapeDao;
import com.redmonkeysoftware.sitescraper.logic.Link;
import com.redmonkeysoftware.sitescraper.logic.MiniScrape;
import com.redmonkeysoftware.sitescraper.logic.Scrape;
import com.redmonkeysoftware.sitescraper.logic.UrlHelper;
import com.redmonkeysoftware.sitescraper.logic.exceptions.ScrapeException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScrapeServiceImpl implements ScrapeService {

    @Autowired
    private ScrapeDao scrapeDao;
    @Autowired
    private ScrapingService scrapingService;
    @Autowired
    @Qualifier("linkProcessorTaskExecutor")
    private TaskExecutor taskExecutor;

    @Override
    @Transactional(readOnly = true)
    public List<MiniScrape> lookupMiniScrapes() {
        List<MiniScrape> results = scrapeDao.lookupMiniScrapes();
        Collections.sort(results);
        return results;
    }

    @Override
    @Transactional
    public List<Scrape> createScrapes(String name, int maxDepth, String urls) {
        List<Scrape> scrapes = new ArrayList<>();
        if (StringUtils.isBlank(name)) {
            throw new ScrapeException("No name defined", HttpStatus.BAD_REQUEST);
        }

        Scrape scrape = new Scrape();
        scrape.setName(name);
        scrape.setMaxDepth(maxDepth);
        String[] urlsArray = StringUtils.split(urls, " ,\n");
        int counter = 0;
        if ((urlsArray != null) && (urlsArray.length > 0)) {
            for (String url : urlsArray) {
                url = UrlHelper.formatAndValidateUrl(url);
                if (url != null) {
                    if (counter > 1000) {
                        counter = 0;
                        scrapes.add(scrapeDao.persistScrape(scrape));
                        scrape = new Scrape();
                        scrape.setName(name + " - 2");
                        scrape.setMaxDepth(maxDepth);
                    }
                    Link link = new Link();
                    link.setUrl(url);
                    scrape.getLinks().add(link);
                    counter++;
                }
            }
        }
        if (!scrape.getLinks().isEmpty()) {
            scrapes.add(scrapeDao.persistScrape(scrape));
        }
        if (!scrapes.isEmpty()) {
            return scrapes;
        }
        throw new ScrapeException("No valid URLS defined", HttpStatus.BAD_REQUEST);
    }

    @Override
    @Transactional(readOnly = true)
    public Scrape lookupScrape(Long id) {
        Scrape scrape = scrapeDao.lookupScrape(id);
        if (scrape != null) {
            Collections.sort(scrape.getLinks());
        }
        return scrape;
    }

    @Override
    @Transactional
    public void deleteScrape(Long id) {
        scrapeDao.deleteScrape(id);
    }

    @Override
    @Async
    public void queueScrape(Scrape scrape) {
        for (Link link : scrape.getLinks()) {
            taskExecutor.execute(new LinkProcessor(scrapingService, scrape.getId(), scrape.getMaxDepth(), link.getId()));
        }
    }
}
