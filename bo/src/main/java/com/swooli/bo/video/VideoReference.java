package com.swooli.bo.video;

import java.io.Serializable;
import java.net.URI;

/**
 * A video identifier for a video, which contains the following links:
 * <ul>
 *      <li>origin URI -- the URI to the originating page that contained the video reference</li>
 *      <li>thumbnail URI -- the URI to the image thumbnail acting as a facade to video</li>
 *      <li>video URI -- the URI to the video</li>
 *      <li>storage repository -- the hosting platform</li>
 * </ul>
 *
 * @author jmcarey
 */
@SuppressWarnings("serial")
public class VideoReference implements Serializable {

    private String videoId;

    private URI originUri;

    private URI thumbnailUri;

    private URI videoUri;

    private StorageRepository storageRepository;

    /**
     * @return the storage repository for the video
     */
    public StorageRepository getStorageRepository() {
        return storageRepository;
    }

    /**
     * Sets the storage repository.
     *
     * @param storageRepository the storage repository
     */
    public void setRepository(final StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    /**
     * @return the third party identifier
     */
    public String getVideoId() {
        return videoId;
    }

    /**
     * Sets the third party identifier.
     *
     * @param id the identifier
     */
    public void setVideoId(final String videoId) {
        this.videoId = videoId;
    }

    /**
     * @return the URI referencing the video thumbnail
     */
    public URI getThumbnailUri() {
        return thumbnailUri;
    }

    /**
     * Sets the thumbnail URI.
     *
     * @param thumbnailUri the URI
     */
    public void setThumbnailUri(final URI thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
    }

    /*
     * @return the URI of the originating site that the video was scraped from
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

    /**
     * @return the URI of the video
     */
    public URI getVideoUri() {
        return videoUri;
    }

    /**
     * Sets the video URI.
     *
     * @param videoUri the URI
     */
    public void setVideoUri(final URI videoUri) {
        this.videoUri = videoUri;
    }

}