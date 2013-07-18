package com.swooli.bo.video;


import java.io.Serializable;
import java.net.URI;

/**
 * A image identifier for an image, which contains the following links:
 * <ul>
 *      <li>origin URI -- the URI to the originating page that contained the image</li>
 *      <li>image URI -- the URI to the image</li>
 * </ul>
 *
 * @author jmcarey
 */
public class ImageReference implements Serializable {

    private URI originUri;

    private URI imageUri;

    /**
     * @return the URI referencing the image
     */
    public URI getImageUri() {
        return imageUri;
    }

    /**
     * Sets the image URI.
     *
     * @param imageUri the URI
     */
    public void setImageUri(final URI imageUri) {
        this.imageUri = imageUri;
    }

    /*
     * @return the URI of the originating site that the image was scraped from
     */
    public URI getOriginUri() {
        return originUri;
    }

    /**
     * Sets the origin URI.
     *
     * @param originUri the URI
     */
    public void setOriginUri(final URI originUri) {
        this.originUri = originUri;
    }
}