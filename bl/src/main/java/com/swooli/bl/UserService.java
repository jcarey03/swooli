package com.swooli.bl;

import com.swooli.bo.user.UserProfile;
import com.swooli.dao.DaoException;
import com.swooli.dao.ObjectNotFoundException;
import com.swooli.dao.OptimisticLockingException;
import com.swooli.dao.UniquenessViolationException;
import com.swooli.dao.request.UserProfileUpdateRequest;

public interface UserService {

    /// creation methods ///


    void createUserProfile(UserProfile profile) throws UniquenessViolationException, DaoException;


    /// retrieval methods ///


    UserProfile retrieveUserProfile(String id) throws ObjectNotFoundException, DaoException;

    UserProfile retrieveUserProfileByFacebookId(String fbId) throws ObjectNotFoundException, DaoException;

    UserProfile retrieveUserProfileByEmail(String email) throws ObjectNotFoundException, DaoException;


    /// update methods ///


    void loginUser(String id, Integer sessionExpiry) throws ObjectNotFoundException, DaoException;

    void updateUserProfile(UserProfileUpdateRequest request) throws ObjectNotFoundException, OptimisticLockingException, DaoException;


    /// deletion methods ///


    void deleteUserProfile(String id) throws DaoException;

}
