package com.swooli.serialization.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;

abstract class VideoCollectionPreviewMixIn {
    @JsonIgnore
    abstract long getId();
}