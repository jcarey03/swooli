package com.swooli.extraction.html.media;

import com.swooli.extraction.html.entity.ImageEntity;
import com.swooli.extraction.html.entity.MetaEntity;
import java.util.List;

public interface ImageMetadataFactory<T extends ImageMetadata> {

    List<T> create(List<MetaEntity> entities);

    T create(ImageEntity entity);

}