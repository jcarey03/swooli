package com.swooli.bo.video;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="VideoRoot")
public class VideoRoot implements Serializable {

    private String id;

    private VideoRootMetadata metadata;

    private List<Swink> swinks = new ArrayList<>();

    private int swinkCount;

    private List<VideoComment> comments = new ArrayList<>();

    private int commentCount;

    @Transient
    private List<VideoPop> pops = new ArrayList<>();

    private int popCount;

    public VideoRoot() {}

    public VideoRoot(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public VideoRootMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(final VideoRootMetadata metadata) {
        this.metadata = metadata;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(final int commentCount) {
        this.commentCount = commentCount;
    }

    public List<VideoComment> getComments() {
        return comments;
    }

    public void setComments(final List<VideoComment> comments) {
        this.comments = comments;
    }

    public int getSwinkCount() {
        return swinkCount;
    }

    public void setSwinkCount(final int swinkCount) {
        this.swinkCount = swinkCount;
    }

    public List<Swink> getSwinks() {
        return swinks;
    }

    public void setSwinks(final List<Swink> swinks) {
        this.swinks = swinks;
    }

    public List<VideoPop> getPops() {
        return pops;
    }

    public void setPops(final List<VideoPop> recentPops) {
        this.pops = recentPops;
    }

    public int getPopCount() {
        return popCount;
    }

    public void setPopCount(final int popCount) {
        this.popCount = popCount;
    }

}