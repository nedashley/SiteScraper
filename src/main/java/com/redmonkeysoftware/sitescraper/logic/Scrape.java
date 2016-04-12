package com.redmonkeysoftware.sitescraper.logic;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.ObjectUtils;

public class Scrape implements Serializable, Comparable<Scrape> {

    private static final long serialVersionUID = 6464987327621228876L;
    protected Long id;
    protected String name;
    protected int maxDepth;
    protected LocalDateTime started;
    protected LocalDateTime finished;
    protected List<Link> links = Collections.synchronizedList(new ArrayList<Link>());

    @Override
    public boolean equals(Object o) {
        return (o instanceof Scrape) ? Objects.equals(id, ((Scrape) o).id) : false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public int compareTo(Scrape o) {
        return ObjectUtils.compare(started, o.started);
    }

    public List<Link> getSuccessfulLinks() {
        List<Link> results = new ArrayList<>();
        for (Link link : links) {
            if (link.isSuccessful()) {
                results.add(link);
            }
        }
        return results;
    }

    public List<Link> getFailedLinks() {
        List<Link> results = new ArrayList<>();
        for (Link link : links) {
            if (!link.isSuccessful()) {
                results.add(link);
            }
        }
        return results;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getStarted() {
        return started;
    }

    public void setStarted(LocalDateTime started) {
        this.started = started;
    }

    public LocalDateTime getFinished() {
        return finished;
    }

    public void setFinished(LocalDateTime finished) {
        this.finished = finished;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
