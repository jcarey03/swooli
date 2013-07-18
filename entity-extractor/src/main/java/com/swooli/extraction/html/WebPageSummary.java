package com.swooli.extraction.html;

import com.swooli.extraction.html.media.ImageMetadata;
import com.swooli.extraction.html.media.VideoMetadata;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="webPageSummary")
public class WebPageSummary {

    private String url;
    
    private String host;

    private String title;

    private String description;

    private List<VideoMetadata> videos = new ArrayList<>();

    private List<ImageMetadata> images = new ArrayList<>();

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<VideoMetadata> getVideos() {
        return videos;
    }

    public void setVideos(final List<VideoMetadata> videos) {
        this.videos = videos;
    }

    public List<ImageMetadata> getImages() {
        return images;
    }

    public void setImages(final List<ImageMetadata> images) {
        this.images = images;
    }

}
