package com.swooli.extraction.html;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.swooli.extraction.html.media.ImageMetadata;
import com.swooli.extraction.html.media.VideoMetadata;
import org.slf4j.LoggerFactory;

public class Main {

    public static void main(String args[]) throws Exception {

        Logger root = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);

        String[] urls = {
            "http://www.youtube.com/watch?v=okEmwaLUemU",
//            "http://www.youtube.com/watch?v=q8KbqR1jqFM",
//            "http://www.youtube.com/watch?v=Gng3sPiJdzA",
//            "http://www.virool.com/about",
//            "http://stevegarfield.blogs.com/",
//            "http://www.pinterest.com",
//            "http://www.landofnod.com/cribs/kids-gear/1",
//            "http://vimeo.com/21362582",
//            "http://www.dailymotion.com/video/xp1juk_atom-and-his-package-undercover-funny_music?search_algo=2#.UO72JuVqyhc"
        };

        final WebPageSummaryFactory factory = new WebPageSummaryFactory();

        // warm-up
//        final WebPageSummary summary1 = factory.create("http://www.youtube.com/watch?v=Gng3sPiJdzA");

        for(int i = 0; i < 1; i++) {
            for(final String url : urls) {
                long start = System.currentTimeMillis();
                final WebPageSummary summary = factory.create(url);
                long end = System.currentTimeMillis();
                if(summary != null) {
                    for(final ImageMetadata metadata : summary.getImages()) {
                        System.out.println("Image URI: " + metadata.getImageReference().getImageUri());
                    }
                    for(final VideoMetadata metadata : summary.getVideos()) {
                        System.out.println("Video URI: " + metadata.getVideoReference().getVideoUri());
                        System.out.println("Video Thumbnail: " + metadata.getVideoReference().getThumbnailUri());
                    }
                    System.out.println("Title: " + summary.getTitle());
                    System.out.println("Description: " + summary.getDescription());
                    System.out.println("Total Time: " + (end - start) / 1000.0 + " seconds for " + url);
                }
            }
        }
    }
}