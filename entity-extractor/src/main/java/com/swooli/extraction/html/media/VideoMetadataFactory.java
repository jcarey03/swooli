package com.swooli.extraction.html.media;

import com.swooli.extraction.html.entity.EmbedEntity;
import com.swooli.extraction.html.entity.IFrameEntity;
import com.swooli.extraction.html.entity.MetaEntity;
import java.util.List;

public interface VideoMetadataFactory<T extends VideoMetadata> {

    T create(IFrameEntity entity);

    T create(EmbedEntity entity);

    List<T> create(List<MetaEntity> entities);

}
