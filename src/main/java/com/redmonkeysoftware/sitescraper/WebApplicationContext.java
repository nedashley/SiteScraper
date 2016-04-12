package com.redmonkeysoftware.sitescraper;

import com.redmonkeysoftware.sitescraper.app.SiteScraperAppConfiguration;
import com.redmonkeysoftware.sitescraper.web.SiteScraperSecurityConfiguration;
import com.redmonkeysoftware.sitescraper.web.SiteScraperWebConfiguration;
import javax.servlet.Filter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebApplicationContext extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SiteScraperAppConfiguration.class, SiteScraperSecurityConfiguration.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SiteScraperWebConfiguration.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[]{new HiddenHttpMethodFilter(), new DelegatingFilterProxy("springSecurityFilterChain")};
    }
}
