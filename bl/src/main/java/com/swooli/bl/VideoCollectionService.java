package com.swooli.bl;

import com.swooli.bo.video.collection.VideoCollection;
import com.swooli.bo.video.collection.VideoCollectionCategory;
import com.swooli.bo.video.collection.VideoCollectionMetadata;
import com.swooli.bo.video.collection.VideoCollectionPreview;
import com.swooli.dao.DaoException;
import com.swooli.dao.ObjectNotFoundException;
import com.swooli.dao.OptimisticLockingException;
import com.swooli.dao.paging.VideoCollectionPreviewPageResult;
import com.swooli.dao.request.VideoCollectionPopRequest;
import com.swooli.dao.request.VideoCollectionUpdateRequest;
import java.util.Collection;

public interface VideoCollectionService {


    /// creation methods ///


    void createVideoCollection(VideoCollectionMetadata metadata) throws DaoException;


    /// retrieval methods ///

    VideoCollectionPreviewPageResult retrieveVideoCollectionPreviews(VideoCollectionCategory category, Long lastPopDate, Integer batchSize) throws DaoException;

    VideoCollection retrieveVideoCollection(String videoCollectionId, Integer popsBatchSize, Integer videoBatchSize) throws AuthorizationException, ObjectNotFoundException, DaoException;

    Collection<VideoCollectionPreview> retrieveVideoCollectionPreviews(Collection<String> videoCollectionIds) throws DaoException;


    /// update methods ///


    void updateVideoCollection(VideoCollectionUpdateRequest request) throws AuthorizationException, OptimisticLockingException, ObjectNotFoundException, DaoException;

    void popVideoCollection(VideoCollectionPopRequest request) throws AuthorizationException, OptimisticLockingException, ObjectNotFoundException, DaoException;


    /// deletion methods ///


    void deleteVideoCollection(String videoCollectionId) throws AuthorizationException, DaoException;

}
