package com.redmonkeysoftware.sitescraper.logic;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.ObjectUtils;

public class Link implements Serializable, Comparable<Link> {

    private static final long serialVersionUID = -6494664806903494410L;
    protected Long id;
    protected LocalDateTime started;
    protected LocalDateTime finished;
    protected String url;
    protected Boolean resolves;
    protected Integer response;
    protected Set<InnerLink> innerLinks = Collections.synchronizedSet(new HashSet<InnerLink>());
    protected Set<String> emails = Collections.synchronizedSet(new HashSet<String>());

    @Override
    public boolean equals(Object o) {
        return (o instanceof Link) ? Objects.equals(id, ((Link) o).id) : false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public int compareTo(Link o) {
        return ObjectUtils.compare(url, o.url);
    }

    public boolean isSuccessful() {
        return ((response != null) && (response >= 200) && (response < 300));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getResolves() {
        return resolves;
    }

    public void setResolves(Boolean resolves) {
        this.resolves = resolves;
    }

    public Integer getResponse() {
        return response;
    }

    public void setResponse(Integer response) {
        this.response = response;
    }

    public Set<InnerLink> getInnerLinks() {
        return innerLinks;
    }

    public void setInnerLinks(Set<InnerLink> innerLinks) {
        this.innerLinks = innerLinks;
    }

    public Set<String> getEmails() {
        return emails;
    }

    public void setEmails(Set<String> emails) {
        this.emails = emails;
    }
}
