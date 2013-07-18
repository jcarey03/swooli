package com.swooli.dao;

import com.swooli.bo.video.Swink;
import com.swooli.bo.video.Video;
import com.swooli.bo.video.VideoComment;
import com.swooli.bo.video.VideoMetadata;
import com.swooli.bo.video.VideoPop;
import com.swooli.bo.video.VideoPreview;
import com.swooli.bo.video.VideoRoot;
import com.swooli.bo.video.VideoRootMetadata;
import com.swooli.bo.video.VideoVote;
import com.swooli.dao.paging.VideoPreviewPageResult;
import com.swooli.dao.paging.VideoRatingPageResult;
import com.swooli.dao.request.SwinkCreateRequest;
import com.swooli.dao.request.VideoCommentCreateRequest;
import com.swooli.dao.request.VideoCreateRequest;
import com.swooli.dao.request.VideoPopRequest;
import com.swooli.dao.request.VideoSpotlightRequest;
import com.swooli.dao.request.VideoUpdateRequest;
import com.swooli.dao.request.VideoVoteRequest;
import java.util.List;

public interface VideoDao {


    /// creation methods ///


    void createVideoRoot(VideoRootMetadata metadata) throws DaoException;

    void createVideo(VideoCreateRequest request) throws OptimisticLockingException, ObjectNotFoundException, DaoException;

    void createSwink(SwinkCreateRequest request) throws OptimisticLockingException, ObjectNotFoundException, DaoException;

    void createVideoComment(VideoCommentCreateRequest comment) throws OptimisticLockingException, ObjectNotFoundException, DaoException;

    void createVideoPop(VideoPop pop) throws DaoException;

    void createVideoVote(VideoVote vote) throws DaoException;


    /// retrieval methods ///


    boolean isVideoCreator(String userProfileId, String videoCollectionId, String videoId) throws DaoException;

    boolean isSwinkCreator(String userProfileId, String videoRootId, String swinkId) throws DaoException;

    boolean isVideoCommentCreator(String userProfileId, String videoRootId, String commentId) throws DaoException;

    VideoPreviewPageResult retrieveVideoPreviews(String videoCollectionId, Double lastRating, Long lastDate, Integer batchSize) throws DaoException;

    VideoPreviewPageResult retrieveVideoPreviews(String videoCollectionId, Long lastDate, Integer batchSize) throws DaoException;

    VideoRatingPageResult retrieveVideoRatings(String videoCollectionId, Double lastRating, Long lastDate, Integer batchSize) throws DaoException;

    VideoRatingPageResult retrieveVideoRatings(String videoCollectionId, Long lastDate, Integer batchSize) throws DaoException;

    VideoMetadata retrieveVideoMetadata(String videoCollectionId, String videoId) throws ObjectNotFoundException, DaoException;

    Video retrieveVideo(String videoCollectionId, String videoId, Integer commentsBatchSize, Integer swinksBatchSize, Integer popsBatchSize) throws ObjectNotFoundException, DaoException;

    VideoRoot retrieveVideoRoot(String id, Integer commentsBatchSize, Integer swinksBatchSize, Integer popsBatchSize) throws ObjectNotFoundException, DaoException;

    List<Swink> retrieveSwinks(String videoRootId) throws DaoException;

    List<VideoPop> retrievePops(String videoRootId) throws DaoException;

    List<VideoComment> retrieveVideoComments(String videoRootId) throws DaoException;

    List<VideoPreview> retrieveSpotlightedVideoPreviews(String videoCollectionId) throws DaoException;


    /// update methods ///


    void spotlightVideo(VideoSpotlightRequest request) throws ObjectNotFoundException, OptimisticLockingException, DaoException;

//    void updateVideo(VideoUpdateRequest request) throws ObjectNotFoundException, OptimisticLockingException, DaoException;


    /// update (transactional) methods ///


    void addVoteToVideoRating(VideoVoteRequest request) throws ObjectNotFoundException, OptimisticLockingException, DaoException;

    void undoAddVoteToVideoRating(VideoVoteRequest request);

    void addPopToVideoRoot(VideoPopRequest request) throws ObjectNotFoundException, OptimisticLockingException, DaoException;

    void undoAddPopToVideoRoot(VideoPopRequest request);

    void undoCreateVideoVote(VideoVoteRequest request);

    void undoCreateVideo(VideoCreateRequest request);

    void undoCreateVideoPop(VideoPopRequest request);


    /// delete methods ///


    void deleteVideo(String videoCollectionId, String videoId) throws DaoException;

    void deleteVideoRoot(String id) throws DaoException;

    void deleteSwink(String videoRootId, String swinkId) throws DaoException;

    void deleteVideoComment(String videoRootId, String videoCommentId) throws DaoException;

}
