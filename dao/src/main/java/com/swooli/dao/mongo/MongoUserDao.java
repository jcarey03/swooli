package com.swooli.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;
import com.swooli.bo.user.User;
import com.swooli.bo.user.UserProfile;
import com.swooli.bo.video.VideoVote;
import com.swooli.bo.video.collection.VideoCollectionPop;
import com.swooli.dao.DaoException;
import com.swooli.dao.ObjectNotFoundException;
import com.swooli.dao.OptimisticLockingException;
import com.swooli.dao.UniquenessViolationException;
import com.swooli.dao.UserDao;
import com.swooli.dao.request.UserProfileUpdateRequest;
import com.swooli.dao.request.VideoCollectionPopRequest;
import com.swooli.dao.request.VideoVoteRequest;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class MongoUserDao implements UserDao {

    private static Logger logger = LoggerFactory.getLogger(MongoUserDao.class);

    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void createUserProfile(final UserProfile profile) throws UniquenessViolationException, DaoException {
        try {
            mongoTemplate.insert(profile);
        } catch(final DataIntegrityViolationException dive) {
            throw new UniquenessViolationException(dive);
        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public boolean isAvailable(final String emailAddress) {
        try {
            final Criteria criteria = Criteria.where("email").is(emailAddress);
            final Query query = new Query(criteria);
            query.fields().include("email");
            return (mongoTemplate.findOne(query, UserProfile.class) == null);
        } catch(final DataAccessException dae) {
            return false;
        }
    }

    @Override
    public UserProfile retrieveUserProfile(final String id) throws ObjectNotFoundException, DaoException {
        try {
            final UserProfile profile = mongoTemplate.findById(id, UserProfile.class);
            if(profile == null) {
                throw new ObjectNotFoundException(String.format("User profile '%s' not found.", id));
            } else {
                return profile;
            }
        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public UserProfile retrieveUserProfileByFacebookId(final String fbId) throws ObjectNotFoundException, DaoException {
        try {

            final Criteria criteria = Criteria.where("fbId").is(fbId);
            final Query query = new Query(criteria);

            final UserProfile profile = mongoTemplate.findOne(query, UserProfile.class);
            if(profile == null) {
                throw new ObjectNotFoundException(String.format("User profile for facebook ID '%s' not found.", fbId));
            } else {
                return profile;
            }
        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public UserProfile retrieveUserProfileByEmail(final String email) throws ObjectNotFoundException, DaoException {
        try {

            final Criteria criteria = Criteria.where("email").is(email);
            final Query query = new Query(criteria);

            final UserProfile profile = mongoTemplate.findOne(query, UserProfile.class);
            if(profile == null) {
                throw new ObjectNotFoundException(String.format("User profile for email '%s' not found.", email));
            } else {
                return profile;
            }
        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public void updateUserProfile(final UserProfileUpdateRequest request) throws ObjectNotFoundException, OptimisticLockingException, DaoException {

        if(!MongoUtils.exists(request.getId(), UserProfile.class, mongoTemplate)) {
            throw new ObjectNotFoundException(String.format("User profile '%s' not found.", request.getVersion()));
        }

        try {

            final Criteria criteria = Criteria.where("id").is(request.getId()).and("version").is(request.getVersion());
            final Query query = new Query(criteria);

            final Update update = new Update();
            MongoUtils.updateField("photoUri", request.getPhotoUri(), update);
            MongoUtils.updateField("password", request.getPassword(), update);
            MongoUtils.updateField("accountStatus", request.getAccountStatus(), update);
            MongoUtils.updateField("lastLoginDate", request.getLastLoginDate(), update);
            update.inc("version", 1);

            final WriteResult writeResult = mongoTemplate.updateFirst(query, update, UserProfile.class);
            if(writeResult.getN() != 1) {
                throw new OptimisticLockingException(String.format("User profile '%s' previously modified.", request.getId()));
            }

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }

    }

    @Override
    public void loginUser(final String id, final Integer sessionExpiry) throws ObjectNotFoundException, DaoException {

        try {

            final Criteria criteria = Criteria.where("id").is(id);
            final Query query = new Query(criteria);

            final Update update = new Update();
            MongoUtils.updateField("sessionExpiry", sessionExpiry, update);
            update.set("lastLoginDate", new Date().getTime());

            final WriteResult writeResult = mongoTemplate.updateFirst(query, update, UserProfile.class);
            if(writeResult.getN() != 1) {
                throw new ObjectNotFoundException(String.format("User profile '%s' not found.", id));
            }

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }

    }

    @Override
    public void deleteUserProfile(final String id) throws DaoException {
        try {

            logger.info(String.format("Deleted user profile '%s'", id));

            /*
             * delete video collections
             * delete video collection pops
             * delete video pops
             * delete videos
             * delete comment
             * delete votes
             * delete video root
             */

            mongoTemplate.remove(new UserProfile(id));
        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public void addVote(final VideoVoteRequest request) throws ObjectNotFoundException, OptimisticLockingException, DaoException {
        try {

            final VideoVote vote = request.getVote();
            final User userReference = vote.getCreatedBy();
            final String userProfileId = userReference.getUserProfileId();
            final long version = request.getUserProfileVersion();

            if(!MongoUtils.exists(userProfileId, UserProfile.class, mongoTemplate)) {
                throw new ObjectNotFoundException(String.format("User profile '%s' not found.", userProfileId));
            }

            // do not store created by since it is the same user
            final VideoVote voteNoUser = new VideoVote(vote.getId());
            voteNoUser.setCreationDate(vote.getCreationDate());
            voteNoUser.setId(vote.getId());
            voteNoUser.setUpVote(vote.isUpVote());
            voteNoUser.setVideoId(vote.getVideoId());

            final Criteria criteria = Criteria.where("id").is(userProfileId).and("version").is(version);
            final Query query = new Query(criteria);

            final Update update = new Update();
            update.inc("version", 1);
            update.push("videoVotes", voteNoUser);

            final WriteResult writeResult = mongoTemplate.updateFirst(query, update, UserProfile.class);
            if(writeResult.getN() != 1) {
                throw new OptimisticLockingException(String.format("User profile '%s' previously modified.", userProfileId));
            }

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public void undoAddVote(VideoVoteRequest request) {
       final VideoVote vote = request.getVote();
       final String voteId = vote.getId();
       final String userProfileId = vote.getCreatedBy().getUserProfileId();
        try {

            logger.info(String.format("Undoing video vote creation for vote '%s' and user profile '%s'", voteId, userProfileId));


            final Criteria criteria = Criteria.where("id").is(userProfileId).and("videoVotes._id").is(voteId);
            final Query query = new Query(criteria);

            /*
             * Developer Note: The MongoTemplate does not currently support pulling an
             * array element based on a property of the element using dot notation.  Therefore,
             * the raw DBObject is used.
             */
            final Update update = new Update();
            update.pull("videoVotes", new BasicDBObject("_id", voteId));

            mongoTemplate.updateFirst(query, update, UserProfile.class);

            logger.info(String.format("Finished undoing video vote creation for vote '%s' and user profile '%s'", voteId, userProfileId));

        } catch(final Exception ex) {
            logger.info(String.format("Failed undoing video vote creation for vote '%s' and user profile '%s'", voteId, userProfileId), ex);
        }
    }

    @Override
    public void addPop(final VideoCollectionPopRequest request) throws ObjectNotFoundException, OptimisticLockingException, DaoException {
        try {

            final VideoCollectionPop pop = request.getPop();
            final User userReference = pop.getCreatedBy();
            final String userProfileId = userReference.getUserProfileId();
            final long version = request.getUserProfileVersion();

            if(!MongoUtils.exists(userProfileId, UserProfile.class, mongoTemplate)) {
                throw new ObjectNotFoundException(String.format("User profile '%s' not found.", userProfileId));
            }

            // do not store created by since it is the same user
            final VideoCollectionPop popNoUser = new VideoCollectionPop(pop.getId());
            popNoUser.setCategory(pop.getCategory());
            popNoUser.setCreationDate(pop.getCreationDate());
            popNoUser.setId(pop.getId());
            popNoUser.setVideoCollectionId(pop.getVideoCollectionId());

            final Criteria criteria = Criteria.where("id").is(userProfileId).and("version").is(version);
            final Query query = new Query(criteria);

            final Update update = new Update();
            update.inc("version", 1);
            update.push("videoCollectionPops", popNoUser);

            final WriteResult writeResult = mongoTemplate.updateFirst(query, update, UserProfile.class);
            if(writeResult.getN() != 1) {
                throw new OptimisticLockingException(String.format("User profile '%s' previously modified.", userProfileId));
            }

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public void undoAddPop(final VideoCollectionPopRequest request) {
        final VideoCollectionPop pop = request.getPop();
        final String popId = pop.getId();
        final String userProfileId = pop.getCreatedBy().getUserProfileId();
        try {

            logger.info(String.format("Undoing video collection pop creation for pop '%s' and user profile '%s'", popId, userProfileId));


            final Criteria criteria = Criteria.where("id").is(userProfileId).and("videoCollectionPops._id").is(popId);
            final Query query = new Query(criteria);

            /*
             * Developer Note: The MongoTemplate does not currently support pulling an
             * array element based on a property of the element using dot notation.  Therefore,
             * the raw DBObject is used.
             */
            final Update update = new Update();
            update.pull("videoCollectionPops", new BasicDBObject("_id", popId));

            mongoTemplate.updateFirst(query, update, UserProfile.class);

            logger.info(String.format("Finished undoing video collection pop creation for pop '%s' and user profile '%s'", popId, userProfileId));

        } catch(final Exception ex) {
            logger.info(String.format("Failed undoing video collection pop creation for pop '%s' and user profile '%s'", popId, userProfileId), ex);
        }
    }

}