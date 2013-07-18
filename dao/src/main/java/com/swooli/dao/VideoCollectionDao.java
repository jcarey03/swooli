package com.swooli.dao;

import com.swooli.bo.video.collection.VideoCollection;
import com.swooli.bo.video.collection.VideoCollectionCategory;
import com.swooli.bo.video.collection.VideoCollectionMetadata;
import com.swooli.bo.video.collection.VideoCollectionPop;
import com.swooli.bo.video.collection.VideoCollectionPreview;
import com.swooli.dao.paging.VideoCollectionPreviewPageResult;
import com.swooli.dao.request.VideoCollectionPopRequest;
import com.swooli.dao.request.VideoCollectionUpdateRequest;
import java.util.Collection;

public interface VideoCollectionDao {


    /// creation methods ///


    void createVideoCollection(VideoCollectionMetadata metadata) throws DaoException;

    void createVideoCollectionPop(VideoCollectionPop pop) throws DaoException;


    /// retrieval methods ///

    boolean isVisible(String videoCollectionId) throws DaoException;

    boolean isOpen(String videoCollectionId) throws DaoException;

    VideoCollectionPreviewPageResult retrieveVideoCollectionPreviews(VideoCollectionCategory category, Long lastPopDate, Integer batchSize) throws DaoException;

    VideoCollection retrieveVideoCollection(String id, Integer popsBatchSize, Integer videoBatchSize) throws ObjectNotFoundException, DaoException;

    Collection<VideoCollectionPreview> retrieveVideoCollectionPreviews(Collection<String> ids) throws DaoException;


    /// update methods ///


    void updateVideoCollection(VideoCollectionUpdateRequest request) throws OptimisticLockingException, ObjectNotFoundException, DaoException;


    /// update (transactional) methods ///


    void undoCreateVideoCollectionPop(VideoCollectionPopRequest request);

    void addPopToVideoCollection(VideoCollectionPopRequest request) throws ObjectNotFoundException, OptimisticLockingException, DaoException;

    void undoAddPopToVideoCollection(VideoCollectionPopRequest request);


    /// deletion methods ///


    void deleteVideoCollection(String id) throws DaoException;

}
