package com.redmonkeysoftware.sitescraper.web.controllers;

import com.redmonkeysoftware.sitescraper.app.service.ScrapeService;
import com.redmonkeysoftware.sitescraper.logic.MiniScrape;
import com.redmonkeysoftware.sitescraper.logic.Scrape;
import com.redmonkeysoftware.sitescraper.web.model.CreateScrapeModel;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/scrapes")
public class ScrapeApiController extends BaseAbstractController {

    @Autowired
    private ScrapeService scrapeService;

    @RequestMapping(value = "/overview", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<MiniScrape> getOverview() {
        return scrapeService.lookupMiniScrapes();
    }

    @RequestMapping(value = "/{scrapeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Scrape getScrape(@PathVariable(value = "scrapeId") Long scrapeId) {
        return scrapeService.lookupScrape(scrapeId);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<MiniScrape> createScrape(@RequestBody CreateScrapeModel model) {
        List<Scrape> scrapes = scrapeService.createScrapes(model.getName(), model.getMaxDepth(), model.getUrls());
        for (Scrape scrape : scrapes) {
            scrapeService.queueScrape(scrape);
        }
        return scrapeService.lookupMiniScrapes();
    }

    @RequestMapping(value = "/{scrapeId}/report", method = RequestMethod.GET)
    public ModelAndView createScrapeReport(@PathVariable(value = "scrapeId") Long scrapeId, ModelMap model) {
        Scrape scrape = scrapeService.lookupScrape(scrapeId);
        model.put("scrape", scrape);
        return new ModelAndView(new ScrapeReportView(), model);
    }

}
