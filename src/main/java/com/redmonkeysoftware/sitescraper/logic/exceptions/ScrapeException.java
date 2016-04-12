package com.redmonkeysoftware.sitescraper.logic.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Scrape Exception")
public class ScrapeException extends RuntimeException {

    private static final long serialVersionUID = -4872428382950238522L;
    protected final HttpStatus status;

    public ScrapeException(final String message, final HttpStatus status) {
        super(message);
        this.status = status;
    }

    public ScrapeException(final Throwable cause) {
        super(cause);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
