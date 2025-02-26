package com.aryan.controller;


import com.aryan.entity.UrlData;
import com.aryan.service.AppService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class AppController {

    @Autowired
    private AppService appService;

    @Value("${server.port}")
    private String port;

    // Add serverPort to the model for all requests 
    @ModelAttribute
    public void addServerPortToModel(Model model) {
        model.addAttribute("serverPort", port);
    }

    @GetMapping
    public String home() {
        return "index";
    }

    @PostMapping("/url-post-process")
    public String processUrl(@RequestParam("url") String url, HttpSession session) {
        try {
            String shortUrl = appService.saveUrl(url);
            session.setAttribute("url", shortUrl);
            session.setAttribute("originalUrl", url);
        } catch (Exception e) {
            session.setAttribute("error", e.getMessage());
        }
        return "index";
    }

    @GetMapping("/error/{message}")
    public String error(@PathVariable String message, HttpSession session) {
        session.setAttribute("error", message);
        return "error";
    }

    @GetMapping("/tiny/{uniqueCode}")
    public RedirectView redirectToOriginalUrl(@PathVariable String uniqueCode) {
        try {
            UrlData data = appService.getOriginalUrl(uniqueCode);
            String originalUrl = data.getOriginalUrl();
            return new RedirectView(originalUrl);
        } catch (Exception e) {
            return new RedirectView("/error/" + e.getMessage());
        }
    }
}
