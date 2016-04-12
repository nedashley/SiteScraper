package com.redmonkeysoftware.sitescraper.web.controllers;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class BaseAbstractController {

    public UserDetails getUserDetails() {
        SecurityContext context = SecurityContextHolder.getContext();
        if ((context != null) && (context.getAuthentication() != null) && (context.getAuthentication().getPrincipal() instanceof UserDetails)) {
            return (UserDetails) context.getAuthentication().getPrincipal();
        }
        return null;
    }

}
