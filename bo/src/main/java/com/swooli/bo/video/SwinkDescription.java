package com.swooli.bo.video;

import java.io.Serializable;
import java.net.URI;

/**
 * A swink description, which is additional information related to a video.
 *
 * @author jmcarey
 */
public class SwinkDescription implements Serializable {

    private String title;

    private String description;

    private URI imageUri;

    private URI originUri;

    public SwinkDescription() {}

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

    public URI getImageUri() {
        return imageUri;
    }

    public void setImageUri(final URI imageUri) {
        this.imageUri = imageUri;
    }

    public boolean hasImage() {
        return imageUri != null;
    }

    public URI getOriginUri() {
        return originUri;
    }

    public void setOriginUri(final URI originUri) {
        this.originUri = originUri;
    }

}