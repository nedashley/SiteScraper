package com.redmonkeysoftware.sitescraper.web.model;

import java.io.Serializable;

public class CreateScrapeModel implements Serializable {

    private static final long serialVersionUID = -6936855121260326513L;
    protected String name;
    protected int maxDepth;
    protected String urls;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }
}
