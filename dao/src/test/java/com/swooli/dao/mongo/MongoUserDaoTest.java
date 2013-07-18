package com.swooli.dao.mongo;

import com.swooli.bo.user.AccountStatus;
import com.swooli.bo.user.UserProfile;
import com.swooli.bo.user.User;
import com.swooli.bo.video.VideoVote;
import com.swooli.bo.video.collection.VideoCollectionPop;
import com.swooli.dao.ObjectNotFoundException;
import com.swooli.dao.OptimisticLockingException;
import com.swooli.dao.request.UserProfileUpdateRequest;
import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration("/dao-context.xml")
@Ignore
public class MongoUserDaoTest extends TestBase {

    @Autowired
    MongoUserDao userDao;

    @Test
    public void testCreateUserProfile() {

        final UserProfile userProfile = mongoTemplate.findOne(new Query(), UserProfile.class);
        userProfile.setId(null);

        VideoCollectionPop pop = new VideoCollectionPop();
        pop.setCreatedBy(new User());
        pop.setCreationDate(new Date().getTime());
        pop.setVideoCollectionId("1234");
        userProfile.setVideoCollectionPops(Arrays.asList(pop));

        VideoVote vote = new VideoVote();
        vote.setCreationDate(new Date().getTime());
        vote.setUpVote(true);
        vote.setVideoId("1234");
        userProfile.setVideoVotes(Arrays.asList(vote));

        userProfile.setMemberOfVideoCollectionIds(Arrays.asList("1234"));
        userProfile.setCreatedVideoCollectionIds(Arrays.asList("1234"));

        userDao.createUserProfile(userProfile);

        assertNotNull(userProfile.getId());

        final UserProfile dbUserProfile = userDao.retrieveUserProfile(userProfile.getId());
        assertNotNull(dbUserProfile);
        assertEquals(1, dbUserProfile.getVideoVotes().size());
        assertNull(dbUserProfile.getVideoVotes().iterator().next().getCreatedBy());
        assertEquals(1, dbUserProfile.getVideoCollectionPops().size());
        assertNull(dbUserProfile.getVideoCollectionPops().iterator().next().getCreatedBy());
        assertEquals(1, dbUserProfile.getMemberOfVideoCollectionIds().size());
        assertEquals(1, dbUserProfile.getCreatedVideoCollectionIds().size());

    }

    @Test
    public void testUpdateUserProfile() {

        final UserProfile profile = mongoTemplate.findOne(new Query(), UserProfile.class);

        final UserProfileUpdateRequest request = new UserProfileUpdateRequest();
        request.setId(profile.getId());
        request.setPhotoUri(URI.create("http://new/photo"));
        request.setVersion(profile.getVersion());
        request.setAccountStatus(AccountStatus.PENDING);
        request.setLastLoginDate(new Date().getTime());
        request.setPassword("new password");

        userDao.updateUserProfile(request);

        final UserProfile dbProfile = userDao.retrieveUserProfile(profile.getId());
        assertEquals(request.getPhotoUri().toString(), dbProfile.getPhotoUri().toString());
        assertEquals(request.getVersion() + 1, dbProfile.getVersion());
        assertEquals(request.getAccountStatus(), dbProfile.getAccountStatus());
        assertEquals(request.getLastLoginDate(), dbProfile.getLastLoginDate());
        assertEquals(request.getPassword(), dbProfile.getPassword());
    }

    @Test(expected=OptimisticLockingException.class)
    public void testUpdateUserProfileStale() {

        final UserProfile userProfile = mongoTemplate.findOne(new Query(), UserProfile.class);

        final UserProfileUpdateRequest request = new UserProfileUpdateRequest();
        request.setId(userProfile.getId());
        request.setVersion(userProfile.getVersion() - 1);
        userDao.updateUserProfile(request);

    }

    @Test(expected=ObjectNotFoundException.class)
    public void testUpdateUserProfileUnknownUserProfile() {
        userDao.updateUserProfile(new UserProfileUpdateRequest());
    }

    @Test
    public void testDeleteUserProfile() {

        final UserProfile userProfile = mongoTemplate.findOne(new Query(), UserProfile.class);

        userDao.deleteUserProfile(userProfile.getId());

        try {
            userDao.retrieveUserProfile(userProfile.getId());
            fail("User profile was not deleted.");
        } catch(final ObjectNotFoundException ex) {}

    }

}

