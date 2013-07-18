package com.swooli.web.controller;

import com.swooli.web.bean.VideoCollectionRequestBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/collections")
public class VideoCollectionController {

    private static Logger logger = LoggerFactory.getLogger(VideoCollectionController.class);

    @RequestMapping(method=RequestMethod.GET)
    public String getVideoCollectionRequest(Model model) {
        model.addAttribute("videoCollectionRequestBean", new VideoCollectionRequestBean());
        return "videoCollectionRequest";
    }

    

}