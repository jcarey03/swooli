package com.swooli.bo.video;

import com.swooli.bo.user.User;
import java.io.Serializable;
import java.net.URI;
import org.springframework.data.annotation.Transient;

public class VideoMetadata implements Serializable {

    private String id;

    private long version;

    private String title;

    @Transient
    private String videoCollectionId;

    private URI thumbnailUri;

    private String videoRootId;

    private User addedBy;

    private long addedDate;

    private boolean spotlighted;

    private long spotlightedDate;

    public VideoMetadata() {}

    public VideoMetadata(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public User getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(final User addedBy) {
        this.addedBy = addedBy;
    }

    public long getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(final long addedDate) {
        this.addedDate = addedDate;
    }

    public String getVideoCollectionId() {
        return videoCollectionId;
    }

    public void setVideoCollectionId(final String videoCollectionId) {
        this.videoCollectionId = videoCollectionId;
    }

    public String getVideoRootId() {
        return videoRootId;
    }

    public void setVideoRootId(final String videoRootId) {
        this.videoRootId = videoRootId;
    }

    public boolean isSpotlighted() {
        return spotlighted;
    }

    public void setSpotlighted(final boolean spotlighted) {
        this.spotlighted = spotlighted;
    }

    public long getSpotlightedDate() {
        return spotlightedDate;
    }

    public void setSpotlightedDate(final long spotlightedDate) {
        this.spotlightedDate = spotlightedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(final long version) {
        this.version = version;
    }

    public URI getThumbnailUri() {
        return thumbnailUri;
    }

    public void setThumbnailUri(final URI thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
    }

}