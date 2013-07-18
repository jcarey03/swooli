package com.swooli.bo.user;

import java.io.Serializable;
import java.net.URI;

/**
 * A reference to a user.
 *
 * @author jmcarey
 */
public class User implements Serializable {

    private String userProfileId;

    private String name;

    private URI photoUri;

    public User() {}

    public User(final String userProfileId) {
        this.userProfileId = userProfileId;
    }

    public String getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(final String userProfileId) {
        this.userProfileId = userProfileId;
    }

    public URI getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(final URI photoUri) {
        this.photoUri = photoUri;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

}