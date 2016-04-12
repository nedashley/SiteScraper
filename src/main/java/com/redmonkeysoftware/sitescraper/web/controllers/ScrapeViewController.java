package com.redmonkeysoftware.sitescraper.web.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/scrapes")
public class ScrapeViewController extends BaseAbstractController {

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getView() {
        return "scrapes_template";
    }

    @RequestMapping(value = "/overview", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getOverviewView() {
        return "scrapes_overview";
    }

    @RequestMapping(value = "/modals/create_scrape_modal", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getCreateScrapeModalView() {
        return "create_scrape_modal";
    }
    
    @RequestMapping(value = "/modals/emails_modal", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getEmailsModalView() {
        return "emails_modal";
    }
    
    @RequestMapping(value = "/modals/inner_links_modal", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getInnerLinksModalView() {
        return "inner_links_modal";
    }

    @RequestMapping(value = "/scrape", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getScrapeView() {
        return "scrape";
    }
}
