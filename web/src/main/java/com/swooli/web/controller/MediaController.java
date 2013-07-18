package com.swooli.web.controller;

import com.swooli.extraction.html.WebPageSummary;
import com.swooli.extraction.html.WebPageSummaryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/media")
public class MediaController {

    private static Logger logger = LoggerFactory.getLogger(MediaController.class);

    @RequestMapping(method=RequestMethod.GET)
    public String getMedia(@RequestParam(value="url") String url, Model model) {
        
        final WebPageSummaryFactory factory = new WebPageSummaryFactory();
        final WebPageSummary webPageSummary = factory.create(url);
        
        model.addAttribute("webPageSummary", webPageSummary);
        return "mediaRequest";
    }

    

}