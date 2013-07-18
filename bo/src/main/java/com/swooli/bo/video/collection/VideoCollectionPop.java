package com.swooli.bo.video.collection;

import com.swooli.bo.user.User;
import java.io.Serializable;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A pop encapsulates the user that "popped" a collection for a given category at a particular time.
 *
 * @author jmcarey
 */
@Document(collection="VideoCollectionPop")
public class VideoCollectionPop implements Serializable {

    private String id;

    private String videoCollectionId;

    private User createdBy;

    private VideoCollectionCategory category;

    private long creationDate;

    public VideoCollectionPop() {}

    public VideoCollectionPop(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public VideoCollectionCategory getCategory() {
        return category;
    }

    public void setCategory(final VideoCollectionCategory category) {
        this.category = category;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final long creationDate) {
        this.creationDate = creationDate;
    }

    public String getVideoCollectionId() {
        return videoCollectionId;
    }

    public void setVideoCollectionId(final String videoCollectionId) {
        this.videoCollectionId = videoCollectionId;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final User createdBy) {
        this.createdBy = createdBy;
    }

}
