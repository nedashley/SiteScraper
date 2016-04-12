package com.redmonkeysoftware.sitescraper.web.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController extends BaseAbstractController {

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getLoginView() {
        if (this.getUserDetails() != null) {
            return "redirect:/scrapes.htm";
        }
        return "login";
    }
}
