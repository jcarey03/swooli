package com.swooli.bo.video.collection;

import com.swooli.bo.video.ImageReference;
import com.swooli.bo.video.VideoReference;
import java.io.Serializable;

/**
 * A description about a video collection, which is composed of a textual description and optionally a video or an image.
 *
 * @author jmcarey
 */
public class VideoCollectionDescription implements Serializable {

    private String title;

    private String description;

    private VideoReference videoReference;

    private ImageReference imageReference;

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

    public VideoReference getVideoReference() {
        return videoReference;
    }

    public void setVideoReference(final VideoReference videoReference) {
        this.videoReference = videoReference;
    }

    public ImageReference getImageReference() {
        return imageReference;
    }

    public void setImageReference(final ImageReference imageReference) {
        this.imageReference = imageReference;
    }

    public boolean hasVideo() {
        return videoReference != null;
    }

    public boolean hasImage() {
        return imageReference != null;
    }

}