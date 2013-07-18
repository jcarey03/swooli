package com.swooli.blimpl;

import com.swooli.bl.UserService;
import com.swooli.bo.user.UserProfile;
import com.swooli.dao.DaoException;
import com.swooli.dao.ObjectNotFoundException;
import com.swooli.dao.OptimisticLockingException;
import com.swooli.dao.UniquenessViolationException;
import com.swooli.dao.UserDao;
import com.swooli.dao.request.UserProfileUpdateRequest;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }


    /// creation methods ///


    @Override
    public void createUserProfile(final UserProfile profile) throws UniquenessViolationException, DaoException {
        userDao.createUserProfile(profile);
    }


    /// retrieval methods ///

    @Override
    public UserProfile retrieveUserProfile(final String id) throws ObjectNotFoundException, DaoException {
        return userDao.retrieveUserProfile(id);
    }

    @Override
    public UserProfile retrieveUserProfileByFacebookId(final String fbId) throws ObjectNotFoundException, DaoException {
        return userDao.retrieveUserProfileByFacebookId(fbId);
    }

    @Override
    public UserProfile retrieveUserProfileByEmail(final String email) throws ObjectNotFoundException, DaoException {
        return userDao.retrieveUserProfileByEmail(email);
    }


    /// update methods ///

    @Override
    public void loginUser(final String id, final Integer sessionExpiry) throws ObjectNotFoundException, DaoException {
        userDao.loginUser(id, sessionExpiry);
    }


    @Override
    public void updateUserProfile(final UserProfileUpdateRequest request) throws ObjectNotFoundException, OptimisticLockingException, DaoException {
        userDao.updateUserProfile(request);
    }


    /// deletion methods ///


    @Override
    public void deleteUserProfile(String id) throws DaoException {
        userDao.deleteUserProfile(id);
    }

}