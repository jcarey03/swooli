package com.swooli.bo.user;

import com.swooli.bo.video.VideoVote;
import com.swooli.bo.video.collection.VideoCollectionPop;
import java.beans.Transient;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * An application user's profile.
 *
 * @author jmcarey
 */
@Document(collection="UserProfile")
public class UserProfile implements Serializable {

    private String id;

    private long version;

    private String fbId;

    private URI photoUri;

    private String firstName;

    private String lastName;

    private String password;

    private String email;

    private Gender gender;

    private AccountStatus accountStatus;

    private long creationDate;

    private long lastLoginDate;

    private boolean admin;

    private int sessionExpiry;

    private Collection<SecurityQuestion> securityQuestions = new ArrayList<>();

    private Collection<VideoCollectionPop> videoCollectionPops = new ArrayList<>();

    private Collection<VideoVote> videoVotes = new ArrayList<>();

    private Collection<String> createdVideoCollectionIds = new ArrayList<>();

    private Collection<String> memberOfVideoCollectionIds = new ArrayList<>();

    public UserProfile() {
    }

    public UserProfile(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(final long version) {
        this.version = version;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(final String fbId) {
        this.fbId = fbId;
    }

    public URI getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(final URI photoUri) {
        this.photoUri = photoUri;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final long creationDate) {
        this.creationDate = creationDate;
    }

    public Collection<SecurityQuestion> getSecurityQuestions() {
        return securityQuestions;
    }

    public void setSecurityQuestions(final Collection<SecurityQuestion> securityQuestions) {
        this.securityQuestions = securityQuestions;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(final AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public long getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(final long lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Collection<VideoCollectionPop> getVideoCollectionPops() {
        return videoCollectionPops;
    }

    public void setVideoCollectionPops(final Collection<VideoCollectionPop> pops) {
        this.videoCollectionPops = pops;
    }

    public Collection<VideoVote> getVideoVotes() {
        return videoVotes;
    }

    public void setVideoVotes(final Collection<VideoVote> videoVotes) {
        this.videoVotes = videoVotes;
    }

    public Collection<String> getCreatedVideoCollectionIds() {
        return createdVideoCollectionIds;
    }

    public void setCreatedVideoCollectionIds(final Collection<String> createdVideoCollectionIds) {
        this.createdVideoCollectionIds = createdVideoCollectionIds;
    }

    public Collection<String> getMemberOfVideoCollectionIds() {
        return memberOfVideoCollectionIds;
    }

    public void setMemberOfVideoCollectionIds(final Collection<String> memberOfVideoCollectionIds) {
        this.memberOfVideoCollectionIds = memberOfVideoCollectionIds;
    }

    @Transient
    public String getName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(final Gender gender) {
        this.gender = gender;
    }

    public boolean isCreatorOrMemberOrAdminForVideoCollection(final String videoCollectionId) {
        return isAdmin() ||
               createdVideoCollectionIds.contains(videoCollectionId) ||
               memberOfVideoCollectionIds.contains(videoCollectionId);
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(final boolean admin) {
        this.admin = admin;
    }

    public int getSessionExpiry() {
        return sessionExpiry;
    }

    public void setSessionExpiry(final int sessionExpiry) {
        this.sessionExpiry = sessionExpiry;
    }

}
