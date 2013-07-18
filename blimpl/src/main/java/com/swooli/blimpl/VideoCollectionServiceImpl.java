package com.swooli.blimpl;

import com.swooli.bl.AuthorizationException;
import com.swooli.bl.VideoCollectionService;
import com.swooli.bo.user.UserProfile;
import com.swooli.bo.video.collection.VideoCollection;
import com.swooli.bo.video.collection.VideoCollectionCategory;
import com.swooli.bo.video.collection.VideoCollectionMetadata;
import com.swooli.bo.video.collection.VideoCollectionPop;
import com.swooli.bo.video.collection.VideoCollectionPreview;
import com.swooli.dao.DaoException;
import com.swooli.dao.ObjectNotFoundException;
import com.swooli.dao.OptimisticLockingException;
import com.swooli.dao.UserDao;
import com.swooli.dao.VideoCollectionDao;
import com.swooli.dao.paging.VideoCollectionPreviewPageResult;
import com.swooli.dao.request.VideoCollectionPopRequest;
import com.swooli.dao.request.VideoCollectionUpdateRequest;
import com.swooli.security.Authenticated;
import com.swooli.security.UserSessionContextHolder;
import java.util.Collection;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VideoCollectionServiceImpl implements VideoCollectionService {

    private static final Logger logger = LoggerFactory.getLogger(VideoCollectionServiceImpl.class);

    private VideoCollectionDao videoCollectionDao;

    private UserDao userDao;

    public void setVideoCollectionDao(final VideoCollectionDao videoCollectionDao) {
        this.videoCollectionDao = videoCollectionDao;
    }

    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }


    /// creation methods ///


    @Override
    @Authenticated
    public void createVideoCollection(final VideoCollectionMetadata metadata) throws DaoException {
        videoCollectionDao.createVideoCollection(metadata);
    }


    /// retrieval methods ///


    @Override
    public VideoCollectionPreviewPageResult retrieveVideoCollectionPreviews(final VideoCollectionCategory category, final Long lastPopDate, final Integer batchSize) throws DaoException {
        return videoCollectionDao.retrieveVideoCollectionPreviews(category, lastPopDate, batchSize);
    }

    @Override
    public VideoCollection retrieveVideoCollection(final String videoCollectionId, final Integer popsBatchSize, final Integer videoBatchSize) throws AuthorizationException, ObjectNotFoundException, DaoException {
        checkVideoCollectionVisible(videoCollectionId);
        return videoCollectionDao.retrieveVideoCollection(videoCollectionId, popsBatchSize, videoBatchSize);
    }

    @Override
    public Collection<VideoCollectionPreview> retrieveVideoCollectionPreviews(final Collection<String> ids) throws DaoException {
        final Collection<VideoCollectionPreview> previews = videoCollectionDao.retrieveVideoCollectionPreviews(ids);
        final Iterator<VideoCollectionPreview> itr = previews.iterator();
        final UserProfile userProfile = UserSessionContextHolder.getUserProfile();
        while(itr.hasNext()) {
            final VideoCollectionPreview preview = itr.next();
            if(userProfile == null) {
                if(!preview.getMetadata().isVisible()) {
                    itr.remove();
                }
            } else if(!userProfile.isCreatorOrMemberOrAdminForVideoCollection(preview.getMetadata().getId())) {
                itr.remove();
            }
        }
        return previews;
    }


    /// update methods ///


    @Override
    @Authenticated
    public void updateVideoCollection(final VideoCollectionUpdateRequest request) throws AuthorizationException, OptimisticLockingException, ObjectNotFoundException, DaoException {
        checkVideoCollectionCreator(request.getVideoCollectionId());
        videoCollectionDao.updateVideoCollection(request);
    }

    @Override
    @Authenticated
    public void popVideoCollection(final VideoCollectionPopRequest request) throws AuthorizationException, OptimisticLockingException, ObjectNotFoundException, DaoException {

        final VideoCollectionPop pop = request.getPop();
        final String videoCollectionId = pop.getVideoCollectionId();
        checkVideoCollectionVisible(videoCollectionId);

        boolean videoCollectionPopCreateSuccess = false;
        boolean addPopToUserProfileSuccess = false;
        try {

            // create video collection pop
            videoCollectionDao.createVideoCollectionPop(pop);
            videoCollectionPopCreateSuccess = true;

            // add pop to user profile
            userDao.addPop(request);
            addPopToUserProfileSuccess = true;

            // add pop to video collection
            videoCollectionDao.addPopToVideoCollection(request);

        } catch(final Exception ex) {

            // clear generated IDs
            pop.setId(null);

            // rollbacks
            if(videoCollectionPopCreateSuccess) {
                videoCollectionDao.undoCreateVideoCollectionPop(request);
            }

            if(addPopToUserProfileSuccess) {
                userDao.undoAddPop(request);
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


    /// deletion methods ///


    @Override
    @Authenticated
    public void deleteVideoCollection(final String id) throws AuthorizationException, DaoException {
        checkVideoCollectionCreator(id);
        videoCollectionDao.deleteVideoCollection(id);
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

}