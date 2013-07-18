package com.swooli.web.bean;

import com.swooli.bo.user.User;
import com.swooli.bo.video.ImageReference;
import com.swooli.bo.video.VideoReference;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * A request for an account.
 *
 * @author jmcarey
 */
public class VideoCollectionRequestBean {

    public static final String DEFAULT_DESCRIPTION = "Describe your video collection...";
    
    public static final String DEFAULT_URL = "http://";
    
    @NotEmpty
    private String title;

    @NotEmpty
    private String description = DEFAULT_DESCRIPTION;

    private String descriptionUrl = DEFAULT_URL;
    
    private String videosUrl = DEFAULT_URL;
    
    private VideoReference videoReference;

    private ImageReference imageReference;

    private boolean visible;
    
    private boolean open;
    
    private List<User> members = new ArrayList<>();
    
    private List<VideoReference> videoReferences = new ArrayList<>();

    public VideoCollectionRequestBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getDescriptionUrl() {
        return descriptionUrl;
    }

    public void setDescriptionUrl(final String descriptionUrl) {
        this.descriptionUrl = descriptionUrl;
    }

    public String getVideosUrl() {
        return videosUrl;
    }

    public void setVideosUrl(String videosUrl) {
        this.videosUrl = videosUrl;
    }

    public VideoReference getVideoReference() {
        return videoReference;
    }

    public void setVideoReference(final VideoReference videoReference) {
        this.videoReference = videoReference;
    }

    public ImageReference getImageReference() {
        return imageReference;
    }

    public void setImageReference(final ImageReference imageReference) {
        this.imageReference = imageReference;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(final boolean open) {
        this.open = open;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(final List<User> members) {
        this.members = members;
    }

    public List<VideoReference> getVideoReferences() {
        return videoReferences;
    }

    public void setVideoReferences(final List<VideoReference> videoReferences) {
        this.videoReferences = videoReferences;
    }

}
