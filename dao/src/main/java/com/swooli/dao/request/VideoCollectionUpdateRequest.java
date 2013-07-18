package com.swooli.dao.request;

import com.swooli.bo.user.User;
import com.swooli.bo.video.collection.VideoCollectionCategory;
import com.swooli.bo.video.collection.VideoCollectionDescription;
import java.io.Serializable;
import java.util.Collection;

public class VideoCollectionUpdateRequest implements Serializable {

    private String videoCollectionId;

    private long videoCollectionVersion;

    private VideoCollectionDescription description;

    private VideoCollectionCategory category;

    private Boolean visible;

    private Boolean open;

    private Collection<User> members;

    public long getVideoCollectionVersion() {
        return videoCollectionVersion;
    }

    public void setVideoCollectionVersion(final long videoCollectionVersion) {
        this.videoCollectionVersion = videoCollectionVersion;
    }

    public VideoCollectionCategory getCategory() {
        return category;
    }

    public void setCategory(final VideoCollectionCategory category) {
        this.category = category;
    }

    public VideoCollectionDescription getDescription() {
        return description;
    }

    public void setDescription(final VideoCollectionDescription description) {
        this.description = description;
    }

    public Collection<User> getMembers() {
        return members;
    }

    public void setMembers(final Collection<User> members) {
        this.members = members;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(final Boolean open) {
        this.open = open;
    }

    public String getVideoCollectionId() {
        return videoCollectionId;
    }

    public void setVideoCollectionId(final String videoCollectionId) {
        this.videoCollectionId = videoCollectionId;
    }

    public void setVisible(final Boolean visible) {
        this.visible = visible;
    }

    public boolean isOpen() {
        return open;
    }

    public Boolean isVisible() {
        return visible;
    }

}