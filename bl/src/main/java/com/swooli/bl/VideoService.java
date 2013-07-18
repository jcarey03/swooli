package com.swooli.bl;

import com.swooli.bo.video.Swink;
import com.swooli.bo.video.Video;
import com.swooli.bo.video.VideoComment;
import com.swooli.bo.video.VideoMetadata;
import com.swooli.bo.video.VideoPreview;
import com.swooli.bo.video.VideoRootMetadata;
import com.swooli.dao.DaoException;
import com.swooli.dao.ObjectNotFoundException;
import com.swooli.dao.OptimisticLockingException;
import com.swooli.dao.paging.VideoPreviewPageResult;
import com.swooli.dao.request.SwinkCreateRequest;
import com.swooli.dao.request.VideoCommentCreateRequest;
import com.swooli.dao.request.VideoPopRequest;
import com.swooli.dao.request.VideoSpotlightRequest;
import com.swooli.dao.request.VideoVoteRequest;
import java.util.List;

public interface VideoService {

    VideoMetadata importVideo(String videoCollectionId, VideoRootMetadata videoRootMetadata) throws DaoException;

    void createSwink(SwinkCreateRequest request) throws OptimisticLockingException, ObjectNotFoundException, DaoException;

    void createVideoComment(VideoCommentCreateRequest request) throws OptimisticLockingException, ObjectNotFoundException, DaoException;


    /// retrieval methods ///


    VideoPreviewPageResult retrieveVideoPreviews(String videoCollectionId, Double lastRating, Long lastDate, Integer batchSize) throws AuthorizationException, DaoException;

    VideoPreviewPageResult retrieveVideoPreviews(String videoCollectionId, Long lastDate, Integer batchSize) throws AuthorizationException, DaoException;

    Video retrieveVideo(String videoCollectionId, String videoId, Integer commentsBatchSize, Integer swinksBatchSize, Integer popsBatchSize) throws AuthorizationException, ObjectNotFoundException, DaoException;

    List<Swink> retrieveSwinks(String videoCollectionId, String videoRootId) throws DaoException;

    List<VideoComment> retrieveVideoComments(String videoCollectionId, String videoRootId) throws DaoException;

    List<VideoPreview> retrieveSpotlightedVideoPreviews(String videoCollectionId) throws AuthorizationException, DaoException;


    /// update methods ///


    void spotlightVideo(VideoSpotlightRequest request) throws AuthorizationException, ObjectNotFoundException, OptimisticLockingException, DaoException;

    void voteVideo(VideoVoteRequest request) throws AuthorizationException, ObjectNotFoundException, OptimisticLockingException, AuthorizationException, DaoException;

    void popVideo(VideoPopRequest request) throws AuthorizationException, ObjectNotFoundException, OptimisticLockingException, AuthorizationException, DaoException;


    /// delete methods ///


    void deleteVideo(String videoCollectionId, String videoId) throws AuthorizationException, DaoException;

    void deleteSwink(String videoRootId, String swinkId) throws AuthorizationException, DaoException;

    void deleteVideoComment(String videoRootId, String videoCommentId) throws AuthorizationException, DaoException;
}
