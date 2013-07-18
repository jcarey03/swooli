package com.swooli.dao.paging;

import com.swooli.bo.video.collection.VideoCollectionCategory;
import com.swooli.bo.video.collection.VideoCollectionPreview;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VideoCollectionPreviewPageResult implements Serializable {

    private List<VideoCollectionPreview> previews = new ArrayList<>();

    private VideoCollectionCategory category;

    private Long lastDate;

    private Integer batchSize;

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(final Integer batchSize) {
        this.batchSize = batchSize;
    }

    public List<VideoCollectionPreview> getPreviews() {
        return previews;
    }

    public void setPreviews(final List<VideoCollectionPreview> previews) {
        this.previews = previews;
    }

    public VideoCollectionCategory getCategory() {
        return category;
    }

    public void setCategory(final VideoCollectionCategory category) {
        this.category = category;
    }

    public Long getLastDate() {
        return lastDate;
    }

    public void setLastDate(final Long lastDate) {
        this.lastDate = lastDate;
    }

}