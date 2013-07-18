package com.swooli.dao.paging;

import com.swooli.bo.video.VideoPreview;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VideoPreviewPageResult implements Serializable {

    private List<VideoPreview> previews = new ArrayList<>();

    private Double lastRating;

    private Long lastDate;

    private Integer batchSize;

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(final Integer batchSize) {
        this.batchSize = batchSize;
    }

    public List<VideoPreview> getPreviews() {
        return previews;
    }

    public void setPreviews(final List<VideoPreview> previews) {
        this.previews = previews;
    }

    public Double getLastRating() {
        return lastRating;
    }

    public void setLastRating(final Double lastRating) {
        this.lastRating = lastRating;
    }

    public Long getLastDate() {
        return lastDate;
    }

    public void setLastDate(final Long lastDate) {
        this.lastDate = lastDate;
    }


}