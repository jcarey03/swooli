package com.swooli.bo.video;

import com.swooli.bo.user.User;
import java.io.Serializable;
import org.springframework.data.annotation.Transient;

public class VideoRootMetadata implements Serializable {

    @Transient
    private String id;

    private long version;

    private VideoReference videoReference;

    private User importedBy;

    private long importDate;

    private String title;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public long getImportDate() {
        return importDate;
    }

    public void setImportDate(final long importDate) {
        this.importDate = importDate;
    }

    public User getImportedBy() {
        return importedBy;
    }

    public void setImportedBy(final User importedBy) {
        this.importedBy = importedBy;
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

    public VideoReference getVideoReference() {
        return videoReference;
    }

    public void setVideoReference(final VideoReference videoReference) {
        this.videoReference = videoReference;
    }

}