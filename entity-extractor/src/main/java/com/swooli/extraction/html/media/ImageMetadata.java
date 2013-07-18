package com.swooli.extraction.html.media;

import com.swooli.bo.video.ImageReference;

public class ImageMetadata {

    private ImageReference imageReference;

    private boolean openGraph;

    public ImageReference getImageReference() {
        return imageReference;
    }

    public void setImageReference(final ImageReference imageReference) {
        this.imageReference = imageReference;
    }

    public boolean isOpenGraph() {
        return openGraph;
    }

    public void setOpenGraph(final boolean openGraph) {
        this.openGraph = openGraph;
    }

}