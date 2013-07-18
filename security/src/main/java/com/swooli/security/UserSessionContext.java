package com.swooli.security;

import com.swooli.bo.user.UserProfile;
import java.io.Serializable;

/**
 * A context containing information about a user's session.
 * 
 * @author jmcarey
 */
public class UserSessionContext implements Serializable {

    private UserProfile userProfile;

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

}