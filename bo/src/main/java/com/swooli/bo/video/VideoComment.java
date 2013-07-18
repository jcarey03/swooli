package com.swooli.bo.video;

import com.swooli.bo.user.User;
import java.io.Serializable;
import org.springframework.data.annotation.Transient;

/**
 * A comment about a video.
 *
 * @author jmcarey
 */
public class VideoComment implements Serializable {

    private String id;

    @Transient
    private String videoRootId;

    private User createdBy;

    private long creationDate;

    private String comment;

    public VideoComment() {
    }

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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final User createdBy) {
        this.createdBy = createdBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final long creationDate) {
        this.creationDate = creationDate;
    }

}
