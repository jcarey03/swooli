package com.swooli.dao;

import com.swooli.bo.user.UserProfile;
import com.swooli.dao.request.UserProfileUpdateRequest;
import com.swooli.dao.request.VideoCollectionPopRequest;
import com.swooli.dao.request.VideoVoteRequest;

public interface UserDao {


    /// creation methods ///


    void createUserProfile(UserProfile profile) throws UniquenessViolationException, DaoException;


    /// retrieval methods ///


    boolean isAvailable(String emailAddress);

    UserProfile retrieveUserProfile(String id) throws ObjectNotFoundException, DaoException;

    UserProfile retrieveUserProfileByFacebookId(String facebookId) throws ObjectNotFoundException, DaoException;

    UserProfile retrieveUserProfileByEmail(String email) throws ObjectNotFoundException, DaoException;


    /// update methods ///


    void loginUser(String id, Integer sessionExpiry) throws ObjectNotFoundException, DaoException;

    void updateUserProfile(UserProfileUpdateRequest request) throws ObjectNotFoundException, OptimisticLockingException, DaoException;


    /// update (transactional) methods ///


    void addVote(VideoVoteRequest request) throws ObjectNotFoundException, OptimisticLockingException, DaoException;

    void undoAddVote(VideoVoteRequest request);

    void addPop(VideoCollectionPopRequest request) throws ObjectNotFoundException, OptimisticLockingException, DaoException;

    void undoAddPop(VideoCollectionPopRequest request);


    /// deletion methods ///


    void deleteUserProfile(String id) throws DaoException;

}