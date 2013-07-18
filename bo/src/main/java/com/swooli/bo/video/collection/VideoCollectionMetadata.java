package com.swooli.bo.video.collection;

import com.swooli.bo.user.User;
import java.io.Serializable;
import org.springframework.data.annotation.Transient;

public class VideoCollectionMetadata implements Serializable {

    @Transient
    private String id;

    private long version;

    private User createdBy;

    private long creationDate;

    private VideoCollectionDescription description;

    /*
     * Visible Open Matrix
     * T T => community can see collection and vote on videos
     * T F => community can see collection, but only members can vote on videos
     * F T => invalid case because community can't vote what they can't see
     * F F => only members can see collection and vote on videos
     */

    /** true if the collection is visible to the community */
    private boolean visible;

    /** true if the community can contribute videos to the collection */
    private boolean open;

    private VideoCollectionCategory category;

    public VideoCollectionMetadata() {}

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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final User createdBy) {
        this.createdBy = createdBy;
    }

    public VideoCollectionDescription getDescription() {
        return description;
    }

    public void setDescription(final VideoCollectionDescription description) {
        this.description = description;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(final boolean open) {
        this.open = open;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(final long version) {
        this.version = version;
    }
}