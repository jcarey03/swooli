package com.swooli.bo.video;

import com.swooli.bo.user.User;
import java.io.Serializable;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A pop encapsulates the user that "popped" a video from a collection to their collection at a particular time.
 *
 * @author jmcarey
 */
@Document(collection="VideoPop")
public class VideoPop implements Serializable {

    private String id;

    private String videoRootId;

    private String fromVideoCollectionId;

    private String toVideoCollectionId;

    private String fromVideoId;

    private String toVideoId;

    private User createdBy;

    private long creationDate;

    public VideoPop() {}

    public VideoPop(final String id) {
        this.id = id;
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

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final long creationDate) {
        this.creationDate = creationDate;
    }

    public String getFromVideoId() {
        return fromVideoId;
    }

    public void setFromVideoId(final String fromVideoId) {
        this.fromVideoId = fromVideoId;
    }

    public String getToVideoId() {
        return toVideoId;
    }

    public void setToVideoId(final String toVideoId) {
        this.toVideoId = toVideoId;
    }

    public String getFromVideoCollectionId() {
        return fromVideoCollectionId;
    }

    public void setFromVideoCollectionId(final String fromVideoCollectionId) {
        this.fromVideoCollectionId = fromVideoCollectionId;
    }

    public String getToVideoCollectionId() {
        return toVideoCollectionId;
    }

    public void setToVideoCollectionId(final String toVideoCollectionId) {
        this.toVideoCollectionId = toVideoCollectionId;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final User createdBy) {
        this.createdBy = createdBy;
    }

}
