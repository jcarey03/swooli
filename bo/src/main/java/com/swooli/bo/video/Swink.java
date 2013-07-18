package com.swooli.bo.video;

import com.swooli.bo.user.User;
import java.io.Serializable;
import org.springframework.data.annotation.Transient;

/**
 * A link associated to either a swopic or a video response.
 *
 * @author jmcarey
 */
public class Swink implements Serializable {

    private String id;

    @Transient
    private String videoRootId;

    private User createdBy;

    private SwinkDescription description;

    private long creationDate;

    public Swink() {}

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getVideoRootId() {
        return videoRootId;
    }

    public void setVideoRootId(final String videoRootId) {
        this.videoRootId = videoRootId;
    }

    public SwinkDescription getDescription() {
        return description;
    }

    public void setDescription(final SwinkDescription description) {
        this.description = description;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final long creationDate) {
        this.creationDate = creationDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final User createdBy) {
        this.createdBy = createdBy;
    }

}