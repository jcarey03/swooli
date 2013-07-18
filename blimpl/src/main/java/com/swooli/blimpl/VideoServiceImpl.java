package com.swooli.blimpl;

import com.swooli.bl.AuthorizationException;
import com.swooli.bl.VideoService;
import com.swooli.bo.user.UserProfile;
import com.swooli.bo.video.Swink;
import com.swooli.bo.video.Video;
import com.swooli.bo.video.VideoComment;
import com.swooli.bo.video.VideoMetadata;
import com.swooli.bo.video.VideoPop;
import com.swooli.bo.video.VideoPreview;
import com.swooli.bo.video.VideoRootMetadata;
import com.swooli.bo.video.VideoVote;
import com.swooli.dao.DaoException;
import com.swooli.dao.ObjectNotFoundException;
import com.swooli.dao.OptimisticLockingException;
import com.swooli.dao.UserDao;
import com.swooli.dao.VideoCollectionDao;
import com.swooli.dao.VideoDao;
import com.swooli.dao.paging.VideoPreviewPageResult;
import com.swooli.dao.request.SwinkCreateRequest;
import com.swooli.dao.request.VideoCommentCreateRequest;
import com.swooli.dao.request.VideoCreateRequest;
import com.swooli.dao.request.VideoPopRequest;
import com.swooli.dao.request.VideoSpotlightRequest;
import com.swooli.dao.request.VideoVoteRequest;
import com.swooli.security.UserSessionContextHolder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VideoServiceImpl implements VideoService {

    private static final Logger logger = LoggerFactory.getLogger(VideoServiceImpl.class);

    private VideoCollectionDao videoCollectionDao;

    private VideoDao videoDao;

    private UserDao userDao;

    public void setVideoCollectionDao(final VideoCollectionDao videoCollectionDao) {
        this.videoCollectionDao = videoCollectionDao;
    }

    public void setVideoDao(final VideoDao videoDao) {
        this.videoDao = videoDao;
    }

    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }


    /// creation methods ///


    @Override
    public VideoMetadata importVideo(final String videoCollectionId, final VideoRootMetadata videoRootMetadata) throws DaoException {

        videoDao.createVideoRoot(videoRootMetadata);

        final VideoMetadata videoMetadata = new VideoMetadata();
        videoMetadata.setAddedBy(videoRootMetadata.getImportedBy());
        videoMetadata.setAddedDate(videoRootMetadata.getImportDate());
        videoMetadata.setThumbnailUri(videoRootMetadata.getVideoReference().getThumbnailUri());
        videoMetadata.setTitle(videoRootMetadata.getTitle());
        videoMetadata.setVideoCollectionId(videoCollectionId);
        videoMetadata.setVideoRootId(videoRootMetadata.getId());

        return videoMetadata;
    }

    @Override
    public void createSwink(final SwinkCreateRequest request) throws AuthorizationException, OptimisticLockingException, ObjectNotFoundException, DaoException {
        checkVideoCollectionVisible(request.getVideoCollectionId());
        videoDao.createSwink(request);
    }

    @Override
    public void createVideoComment(VideoCommentCreateRequest request) throws OptimisticLockingException, ObjectNotFoundException, DaoException {
        checkVideoCollectionVisible(request.getVideoCollectionId());
        videoDao.createVideoComment(request);
    }


    /// retrieval methods ///


    @Override
    public VideoPreviewPageResult retrieveVideoPreviews(final String videoCollectionId, final Double lastRating, final Long lastDate, final Integer batchSize) throws AuthorizationException, DaoException {
        checkVideoCollectionVisible(videoCollectionId);
        return videoDao.retrieveVideoPreviews(videoCollectionId, lastRating, lastDate, batchSize);
    }

    @Override
    public VideoPreviewPageResult retrieveVideoPreviews(final String videoCollectionId, final Long lastDate, final Integer batchSize) throws AuthorizationException, DaoException {
        checkVideoCollectionVisible(videoCollectionId);
        return videoDao.retrieveVideoPreviews(videoCollectionId, lastDate, batchSize);
    }

    @Override
    public Video retrieveVideo(final String videoCollectionId, final String videoId, final Integer commentsBatchSize, final Integer swinksBatchSize, final Integer popsBatchSize) throws AuthorizationException, ObjectNotFoundException, DaoException {
        checkVideoCollectionVisible(videoCollectionId);
        return videoDao.retrieveVideo(videoCollectionId, videoId, commentsBatchSize, swinksBatchSize, popsBatchSize);
    }

    @Override
    public List<Swink> retrieveSwinks(final String videoCollectionId, final String videoRootId) throws DaoException {
        checkVideoCollectionVisible(videoCollectionId);
        return videoDao.retrieveSwinks(videoRootId);
    }

    @Override
    public List<VideoComment> retrieveVideoComments(final String videoCollectionId, final String videoRootId) throws DaoException {
        checkVideoCollectionVisible(videoCollectionId);
        return videoDao.retrieveVideoComments(videoRootId);
    }

    @Override
    public List<VideoPreview> retrieveSpotlightedVideoPreviews(final String videoCollectionId) throws DaoException {
        checkVideoCollectionVisible(videoCollectionId);
        return videoDao.retrieveSpotlightedVideoPreviews(videoCollectionId);
    }


    /// update methods ///


    @Override
    public void spotlightVideo(final VideoSpotlightRequest request) throws AuthorizationException, ObjectNotFoundException, OptimisticLockingException, DaoException {
        checkVideoCollectionCreator(request.getVideoCollectionId());
        videoDao.spotlightVideo(request);
    }

    @Override
    public void voteVideo(final VideoVoteRequest request) throws ObjectNotFoundException, OptimisticLockingException, AuthorizationException, DaoException {
        checkVideoCollectionOpen(request.getVideoCollectionId());

        final VideoVote vote = request.getVote();
        boolean videoVoteCreateSuccess = false;
        boolean addVoteToUserProfileSuccess = false;
        try {

            // create video vote
            videoDao.createVideoVote(vote);
            videoVoteCreateSuccess = true;

            // add video vote to user profile
            userDao.addVote(request);
            addVoteToUserProfileSuccess = true;

            // update video rating
            videoDao.addVoteToVideoRating(request);

        } catch(final Exception ex) {

            // clear generated IDs
            vote.setId(null);

            // rollbacks
            if(videoVoteCreateSuccess) {
                videoDao.undoCreateVideoVote(request);
            }

            if(addVoteToUserProfileSuccess) {
                userDao.undoAddVote(request);
            }

            if(ex instanceof ObjectNotFoundException) {
                throw (ObjectNotFoundException) ex;
            } else if(ex instanceof OptimisticLockingException) {
                throw (OptimisticLockingException) ex;
            } else if(ex instanceof AuthorizationException) {
                throw (AuthorizationException) ex;
            } else if(ex instanceof DaoException) {
                throw (DaoException) ex;
            } else {
                throw new DaoException(ex);
            }
        }
    }

    @Override
    public void popVideo(final VideoPopRequest request) throws ObjectNotFoundException, OptimisticLockingException, AuthorizationException, DaoException {

        final VideoPop pop = request.getPop();

        final String fromVideoCollectionId = pop.getFromVideoCollectionId();
        checkVideoCollectionVisible(fromVideoCollectionId);

        final String toVideoCollectionId = pop.getToVideoCollectionId();
        checkVideoCollectionVisible(toVideoCollectionId);

        boolean videoCreateSuccess = false;
        boolean videoPopCreateSuccess = false;
        VideoCreateRequest videoCreateRequest = null;
        try {

            final String fromVideoId = pop.getFromVideoId();
            final long toVideoCollectionVersion = request.getToVideoCollectionVersion();

            final VideoMetadata fromVideoMetadata = videoDao.retrieveVideoMetadata(fromVideoCollectionId, fromVideoId);
            final VideoMetadata toVideoMetadata = fromVideoMetadata;
            toVideoMetadata.setId(null);

            videoCreateRequest = new VideoCreateRequest();
            videoCreateRequest.setVideoCollectionVersion(toVideoCollectionVersion);
            videoCreateRequest.setVideoMetadata(toVideoMetadata);
            videoDao.createVideo(videoCreateRequest);
            videoCreateSuccess = true;

            // set ID of newly created video
            pop.setToVideoId(toVideoMetadata.getId());

            // create video pop
            videoDao.createVideoPop(pop);
            videoPopCreateSuccess = true;

            // add video pop to video root
            videoDao.addPopToVideoRoot(request);

        } catch(final Exception ex) {

            // clear generated IDs
            pop.setId(null);
            pop.setToVideoId(null);

            // rollbacks
            if(videoCreateSuccess) {
                videoDao.undoCreateVideo(videoCreateRequest);
            }

            if(videoPopCreateSuccess) {
                videoDao.undoCreateVideoPop(request);
            }

            if(ex instanceof ObjectNotFoundException) {
                throw (ObjectNotFoundException) ex;
            } else if(ex instanceof OptimisticLockingException) {
                throw (OptimisticLockingException) ex;
            } else if(ex instanceof AuthorizationException) {
                throw (AuthorizationException) ex;
            } else if(ex instanceof DaoException) {
                throw (DaoException) ex;
            } else {
                throw new DaoException(ex);
            }
        }
    }


    /// delete methods ///


    @Override
    public void deleteVideo(final String videoCollectionId, final String videoId) throws AuthorizationException, DaoException {
        checkVideoCreator(videoCollectionId, videoId);
        videoDao.deleteVideo(videoCollectionId, videoId);
    }

    @Override
    public void deleteSwink(final String videoRootId, final String swinkId) throws AuthorizationException, DaoException {
        checkSwinkCreator(videoRootId, swinkId);
        videoDao.deleteSwink(videoRootId, swinkId);
    }

    @Override
    public void deleteVideoComment(final String videoRootId, final String videoCommentId) throws AuthorizationException, DaoException {
        checkVideoCommentCreator(videoRootId, videoCommentId);
        videoDao.deleteSwink(videoRootId, videoCommentId);
    }

    private void checkVideoCollectionVisible(final String videoCollectionId) throws AuthorizationException {
        final UserProfile userProfile = UserSessionContextHolder.getUserProfile();
        if(userProfile == null) {
            if(!videoCollectionDao.isVisible(videoCollectionId)) {
                throw new AuthorizationException(String.format("User is not authorized to perform action"));
            }
        } else {
            if(!userProfile.isCreatorOrMemberOrAdminForVideoCollection(videoCollectionId) && !videoCollectionDao.isVisible(videoCollectionId)) {
                throw new AuthorizationException(String.format("User '%s' is not authorized to perform action", userProfile.getId()));
            }
        }
    }

    private void checkVideoCollectionOpen(final String videoCollectionId) throws AuthorizationException {
        final UserProfile userProfile = UserSessionContextHolder.getUserProfile();
        if(userProfile == null) {
            if(!videoCollectionDao.isOpen(videoCollectionId)) {
                throw new AuthorizationException(String.format("User is not authorized to perform action"));
            }
        } else {
            if(!userProfile.isCreatorOrMemberOrAdminForVideoCollection(videoCollectionId) && !videoCollectionDao.isVisible(videoCollectionId)) {
                throw new AuthorizationException(String.format("User '%s' is not authorized to perform action", userProfile.getId()));
            }
        }
    }

    private void checkVideoCollectionCreator(final String videoCollectionId) throws AuthorizationException {
        final UserProfile userProfile = UserSessionContextHolder.getUserProfile();
        if(userProfile == null) {
            throw new AuthorizationException(String.format("User is not authorized to perform action"));
        } else {
            if(!userProfile.getCreatedVideoCollectionIds().contains(videoCollectionId)) {
                throw new AuthorizationException(String.format("User '%s' is not authorized to perform action", userProfile.getId()));
            }
        }
    }

    private void checkVideoCreator(final String videoCollectionId, final String videoId) throws AuthorizationException {
        final UserProfile userProfile = UserSessionContextHolder.getUserProfile();
        if(userProfile == null) {
            throw new AuthorizationException(String.format("User is not authorized to perform action"));
        } else {
            if(videoDao.isVideoCreator(userProfile.getId(), videoCollectionId, videoId)) {
                throw new AuthorizationException(String.format("User '%s' is not authorized to perform action", userProfile.getId()));
            }
        }
    }

    private void checkSwinkCreator(final String videoRootId, final String swinkId) throws AuthorizationException {
        final UserProfile userProfile = UserSessionContextHolder.getUserProfile();
        if(userProfile == null) {
            throw new AuthorizationException(String.format("User is not authorized to perform action"));
        } else {
            if(videoDao.isSwinkCreator(userProfile.getId(), videoRootId, swinkId)) {
                throw new AuthorizationException(String.format("User '%s' is not authorized to perform action", userProfile.getId()));
            }
        }
    }

    private void checkVideoCommentCreator(final String videoRootId, final String videoCommentId) throws AuthorizationException {
        final UserProfile userProfile = UserSessionContextHolder.getUserProfile();
        if(userProfile == null) {
            throw new AuthorizationException(String.format("User is not authorized to perform action"));
        } else {
            if(videoDao.isVideoCommentCreator(userProfile.getId(), videoRootId, videoCommentId)) {
                throw new AuthorizationException(String.format("User '%s' is not authorized to perform action", userProfile.getId()));
            }
        }
    }

}