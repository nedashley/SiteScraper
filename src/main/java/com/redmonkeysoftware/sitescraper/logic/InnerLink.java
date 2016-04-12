package com.redmonkeysoftware.sitescraper.logic;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import org.apache.commons.lang3.ObjectUtils;

public class InnerLink implements Serializable, Comparable<InnerLink> {

    private static final long serialVersionUID = 1814467633129615774L;
    protected Long id;
    protected LocalDateTime started;
    protected LocalDateTime finished;
    protected String url;
    protected Integer depth;
    protected Integer response;

    @Override
    public boolean equals(Object o) {
        if (id == null) {
            return (o instanceof InnerLink) ? Objects.equals(url, ((InnerLink) o).url) : false;
        }
        return (o instanceof InnerLink) ? Objects.equals(id, ((InnerLink) o).id) : false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public int compareTo(InnerLink o) {
        int result = ObjectUtils.compare(depth, o.depth);
        return result == 0 ? ObjectUtils.compare(url, o.url) : result;
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

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Integer getResponse() {
        return response;
    }

    public void setResponse(Integer response) {
        this.response = response;
    }
}
