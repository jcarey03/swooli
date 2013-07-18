package com.swooli.dao.request;

import com.swooli.bo.user.AccountStatus;
import java.io.Serializable;
import java.net.URI;

public class UserProfileUpdateRequest implements Serializable {

    private String id;

    private long version;

    private URI photoUri;

    private String password;

    private AccountStatus accountStatus;

    private long lastLoginDate;

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(final AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public long getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(final long lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(final long version) {
        this.version = version;
    }

    public URI getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(URI photoUri) {
        this.photoUri = photoUri;
    }


}