package com.swooli.bo.video.collection;

import com.swooli.bo.user.User;
import com.swooli.bo.video.VideoPreview;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="VideoCollection")
public class VideoCollection implements Serializable {

    private String id;

    private VideoCollectionMetadata metadata = new VideoCollectionMetadata();

    private Collection<User> members = new ArrayList<>();

    private List<VideoPreview> videos = new ArrayList<>();

    private int videoCount;

    @Transient
    private List<VideoCollectionPop> pops = new ArrayList<>();

    private int popCount;

    public VideoCollection() {}

    public VideoCollection(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public VideoCollectionMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(final VideoCollectionMetadata metadata) {
        this.metadata = metadata;
    }

    public Collection<User> getMembers() {
        return members;
    }

    public void setMembers(final Collection<User> members) {
        this.members = members;
    }

    public List<VideoPreview> getVideos() {
        return videos;
    }

    public void setVideos(final List<VideoPreview> videos) {
        this.videos = videos;
    }

    public int getPopCount() {
        return popCount;
    }

    public void setPopCount(final int popCount) {
        this.popCount = popCount;
    }

    public int getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(final int videoCount) {
        this.videoCount = videoCount;
    }

    public List<VideoCollectionPop> getPops() {
        return pops;
    }

    public void setPops(final List<VideoCollectionPop> recentPops) {
        this.pops = recentPops;
    }

}