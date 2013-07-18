package com.swooli.dao.mongo;

import com.mongodb.WriteResult;
import com.swooli.bo.video.VideoImpression;
import com.swooli.bo.video.VideoPreview;
import com.swooli.bo.video.VideoRating;
import com.swooli.bo.video.VideoVote;
import com.swooli.bo.video.collection.VideoCollection;
import com.swooli.bo.video.collection.VideoCollectionCategory;
import com.swooli.bo.video.collection.VideoCollectionMetadata;
import com.swooli.bo.video.collection.VideoCollectionPop;
import com.swooli.bo.video.collection.VideoCollectionPreview;
import com.swooli.dao.DaoException;
import com.swooli.dao.ObjectNotFoundException;
import com.swooli.dao.OptimisticLockingException;
import com.swooli.dao.VideoCollectionDao;
import com.swooli.dao.VideoDao;
import com.swooli.dao.paging.VideoCollectionPreviewPageResult;
import com.swooli.dao.paging.VideoPreviewPageResult;
import com.swooli.dao.request.VideoCollectionPopRequest;
import com.swooli.dao.request.VideoCollectionUpdateRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class MongoVideoCollectionDao implements VideoCollectionDao {

    private static final Logger logger = LoggerFactory.getLogger(MongoVideoCollectionDao.class);

    private MongoTemplate mongoTemplate;

    private VideoDao videoDao;

    public void setMongoTemplate(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void setVideoDao(final VideoDao videoDao) {
        this.videoDao = videoDao;
    }

    @Override
    public boolean isVisible(final String videoCollectionId) throws DaoException {
        try {

            final Criteria criteria = Criteria.where("id").is(videoCollectionId);
            final Query query = new Query(criteria);
            query.fields().include("metadata.visible");

            final VideoCollection videoCollection = mongoTemplate.findOne(query, VideoCollection.class);
            if(videoCollection != null) {
                final VideoCollectionMetadata metadata = videoCollection.getMetadata();
                return metadata.isVisible();
            }

            return false;

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public boolean isOpen(final String videoCollectionId) throws DaoException {
        try {

            final Criteria criteria = Criteria.where("id").is(videoCollectionId);
            final Query query = new Query(criteria);
            query.fields().include("metadata.open");

            final VideoCollection videoCollection = mongoTemplate.findOne(query, VideoCollection.class);
            if(videoCollection != null) {
                final VideoCollectionMetadata metadata = videoCollection.getMetadata();
                return metadata.isOpen();
            }

            return false;

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public void createVideoCollection(final VideoCollectionMetadata metadata) throws DaoException {
        try {
            final VideoCollection videoCollection = new VideoCollection();
            videoCollection.setMetadata(metadata);
            mongoTemplate.insert(videoCollection);
            metadata.setId(videoCollection.getId());
        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public void deleteVideoCollection(final String id) throws DaoException {
        try {

            // find collection
            final Criteria videosCriteria = Criteria.where("id").is(id);
            final Query videosQuery = new Query(videosCriteria);
            videosQuery.fields().include("videos.metadata._id");
            final VideoCollection videoCollection = mongoTemplate.findOne(videosQuery, VideoCollection.class);

            // get video IDs
            if(videoCollection != null) {

                final Collection<String> videoIds = new ArrayList<>();
                for(final VideoPreview videoPreview : videoCollection.getVideos()) {
                    videoIds.add(videoPreview.getMetadata().getId());
                }

                // delete video votes
                final Criteria videoVotesCriteria = Criteria.where("videoId").in(videoIds);
                final Query videoVotesQuery = new Query(videoVotesCriteria);
                mongoTemplate.remove(videoVotesQuery, VideoVote.class);

                // delete collection and videos
                mongoTemplate.remove(videoCollection);
            }

            // delete video ratings
            final Criteria videoRatingsCriteria = Criteria.where("videoCollectionId").is(id);
            final Query videoRatingsQuery = new Query(videoRatingsCriteria);
            mongoTemplate.remove(videoRatingsQuery, VideoRating.class);

            // delete pops
            final Criteria popsCriteria = Criteria.where("videoCollectionId").is(id);
            final Query popsQuery = new Query(popsCriteria);
            mongoTemplate.remove(popsQuery, VideoCollectionPop.class);

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public VideoCollectionPreviewPageResult retrieveVideoCollectionPreviews(final VideoCollectionCategory category, final Long lastPopDate, final Integer batchSize) throws DaoException {
        try {

            /// get pops to determine video collection ordering

            long effectiveLastPopDate = (lastPopDate == null) ? Long.MAX_VALUE : lastPopDate;

            int effectiveBatchSize = (batchSize == null) ? Integer.MAX_VALUE : batchSize;

            final Criteria popCriteria = Criteria.where("category").is(category.toString()).and("creationDate").lt(effectiveLastPopDate);

            final Query popQuery = new Query(popCriteria);
            popQuery.sort().on("creationDate", Order.DESCENDING);
            popQuery.limit(effectiveBatchSize);
            popQuery.fields().include("videoCollectionId").include("creationDate");

            final List<VideoCollectionPop> pops = mongoTemplate.find(popQuery, VideoCollectionPop.class);

            // determine the video collection IDs
            final Collection<String> videoCollectionIds = new ArrayList<>();
            for(final VideoCollectionPop pop : pops) {
                videoCollectionIds.add(pop.getVideoCollectionId());
            }

            final Criteria videoCollectionCriteria = Criteria.where("id").in(videoCollectionIds);
            final Query videoCollectionQuery = new Query(videoCollectionCriteria);
            videoCollectionQuery.fields()
                .include("metadata")
                .include("videoCount")
                .include("popCount")
                .include("videos.metadata._id")
                .include("videos.metadata.thumbnailUri");

            final Collection<VideoCollection> videoCollections = mongoTemplate.find(videoCollectionQuery, VideoCollection.class);

            // create video collection previews from video collections
            final Map<String, VideoCollectionPreview> previewMap = new HashMap<>();
            for(final VideoCollection videoCollection : videoCollections) {

                final VideoCollectionPreview preview = new VideoCollectionPreview();
                final VideoCollectionMetadata metadata = videoCollection.getMetadata();
                metadata.setId(videoCollection.getId());
                preview.setMetadata(metadata);

                preview.setPopCount(videoCollection.getPopCount());
                preview.setVideoCount(videoCollection.getVideoCount());

                final List<VideoImpression> videoImpressions = new ArrayList<>(videoCollection.getVideos().size());
                for(final VideoPreview videoPreview : videoCollection.getVideos()) {
                    final VideoImpression videoImpression = new VideoImpression();
                    videoImpression.setId(videoPreview.getMetadata().getId());
                    videoImpression.setThumbnailUri(videoPreview.getMetadata().getThumbnailUri());
                    videoImpressions.add(videoImpression);
                }
                preview.setVideos(videoImpressions);

                previewMap.put(preview.getMetadata().getId(), preview);
            }

            final List<VideoCollectionPreview> previews = new ArrayList<>();
            VideoCollectionPop lastPop = null;
            for(final VideoCollectionPop pop : pops) {
                final VideoCollectionPreview preview = previewMap.get(pop.getVideoCollectionId());
                if(preview != null) {
                    previews.add(preview);
                }
                lastPop = pop;
            }

            final VideoCollectionPreviewPageResult pageResult = new VideoCollectionPreviewPageResult();
            pageResult.setPreviews(previews);
            pageResult.setCategory(category);
            pageResult.setBatchSize(batchSize);

            if(previews.isEmpty()) {
                pageResult.setLastDate(lastPopDate);
            } else {
                pageResult.setLastDate(lastPop.getCreationDate());
            }

            return pageResult;

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public Collection<VideoCollectionPreview> retrieveVideoCollectionPreviews(final Collection<String> ids) throws DaoException {
        try {

            final Criteria videoCollectionCriteria = Criteria.where("id").in(ids);
            final Query query = new Query(videoCollectionCriteria);
            query.fields()
                .include("metadata")
                .include("videoCount")
                .include("popCount")
                .include("videos.metadata._id")
                .include("videos.metadata.thumbnailUri");

            final Collection<VideoCollection> videoCollections = mongoTemplate.find(query, VideoCollection.class);

            // create video collection previews from video collections
            final List<VideoCollectionPreview> previews = new ArrayList<>();
            for(final VideoCollection videoCollection : videoCollections) {

                final VideoCollectionPreview preview = new VideoCollectionPreview();
                final VideoCollectionMetadata metadata = videoCollection.getMetadata();
                metadata.setId(videoCollection.getId());
                preview.setMetadata(metadata);

                preview.setPopCount(videoCollection.getPopCount());
                preview.setVideoCount(videoCollection.getVideoCount());

                final List<VideoImpression> videoImpressions = new ArrayList<>(videoCollection.getVideos().size());
                for(final VideoPreview videoPreview : videoCollection.getVideos()) {
                    final VideoImpression videoImpression = new VideoImpression();
                    videoImpression.setId(videoPreview.getMetadata().getId());
                    videoImpression.setThumbnailUri(videoPreview.getMetadata().getThumbnailUri());
                    videoImpressions.add(videoImpression);
                }
                preview.setVideos(videoImpressions);

                previews.add(preview);

            }

            return previews;

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public void updateVideoCollection(final VideoCollectionUpdateRequest request) throws OptimisticLockingException, ObjectNotFoundException, DaoException {
        boolean popsUpdated = true;
        final VideoCollectionCategory category = request.getCategory();
        final String videoCollectionId = request.getVideoCollectionId();
        try {

            final long videoCollectionVersion = request.getVideoCollectionVersion();

            if(!MongoUtils.exists(videoCollectionId, VideoCollection.class, mongoTemplate)) {
                throw new ObjectNotFoundException(String.format("Video collection '%s' not found.", videoCollectionId));
            }

            // update video
            final Criteria criteria = Criteria.where("id").is(videoCollectionId).and("version").is(videoCollectionVersion);
            final Query query = new Query(criteria);

            final Update update = new Update();
            MongoUtils.updateField("metadata.category", category, update);
            MongoUtils.updateField("metadata.description", request.getDescription(), update);
            MongoUtils.updateField("metadata.open", request.getOpen(), update);
            MongoUtils.updateField("metadata.visible", request.isVisible(), update);
            MongoUtils.updateField("members", request.getMembers(), update);
            update.inc("metadata.version", 1);

            final WriteResult writeResult = mongoTemplate.updateFirst(query, update, VideoCollection.class);
            if(writeResult.getN() != 1) {
                throw new OptimisticLockingException(String.format("Video collection '%s' previously modified.", videoCollectionId));
            }

            // handle category changes

            if(category == null) {
                return;
            }

            logger.info(String.format("Updating category to '%s' in video collection pops for video collection '%s'", category, videoCollectionId));

            popsUpdated = false;

            // update video collection pops
            final Criteria popCriteria = Criteria.where("videoCollectionId").is(videoCollectionId);
            final Query popQuery = new Query(popCriteria);
            final Update popUpdate = new Update();
            popUpdate.set("category", category);
            mongoTemplate.updateMulti(popQuery, popUpdate, VideoCollectionPop.class);

            popsUpdated = true;

            logger.info(String.format("Finished updating category to '%s' in video collection pops for video collection '%s'", category, videoCollectionId));

            /*
             * The user profile stores video collection pops, but updating the category is not
             * done for two reasons.  First, the pops are stored in the profile for fast lookup
             * of which collections the user popped and when.  Secondly, there is potentially more
             * informational value to know what the category was when the user popped it.
             */

        } catch(final DataAccessException dae) {
            if(!popsUpdated) {
                logger.warn(String.format("Failed updating category to '%s' in video collection pops for video collection '%s'", category, videoCollectionId));
            }
            throw new DaoException(dae);
        }
    }

    @Override
    public VideoCollection retrieveVideoCollection(final String id, final Integer popsBatchSize, final Integer videoBatchSize) throws ObjectNotFoundException, DaoException {
        try {

            /// get video collection ///

            final Criteria videoCollectionCriteria = Criteria.where("id").in(id);
            final Query videoCollectionQuery = new Query(videoCollectionCriteria);
            videoCollectionQuery.fields()
                .include("metadata")
                .include("videoCount")
                .include("popCount");

            final VideoCollection videoCollection = mongoTemplate.findOne(videoCollectionQuery, VideoCollection.class);
            if(videoCollection == null) {
                throw new ObjectNotFoundException(String.format("Video collection '%s' not found.", id));
            }

            videoCollection.getMetadata().setId(videoCollection.getId());

            /// get video previews ///

            final VideoPreviewPageResult videoPageResult = videoDao.retrieveVideoPreviews(id, null, null, videoBatchSize);
            videoCollection.setVideos(videoPageResult.getPreviews());

            /// get recent pops ///
            if(videoCollection.getPopCount() > 0) {
                final int effectivePopsBatchSize = (popsBatchSize == null) ? Integer.MAX_VALUE : popsBatchSize;

                final Criteria popsCriteria = Criteria.where("videoCollectionId").is(id);
                final Query popsQuery = new Query(popsCriteria);
                popsQuery.sort().on("creationDate", Order.DESCENDING);
                popsQuery.limit(effectivePopsBatchSize);

                final List<VideoCollectionPop> pops = mongoTemplate.find(popsQuery, VideoCollectionPop.class);
                videoCollection.setPops(pops);
            }

            return videoCollection;

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public void createVideoCollectionPop(final VideoCollectionPop pop) throws DaoException {
        try {
            mongoTemplate.insert(pop);
        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public void undoCreateVideoCollectionPop(final VideoCollectionPopRequest request) {
        final VideoCollectionPop pop = request.getPop();
        try {
            logger.info(String.format("Undoing video collection pop creation for pop '%s'", pop.getId()));
            mongoTemplate.remove(request.getPop());
            logger.info(String.format("Finished undoing video collection pop creation for pop '%s'", pop.getId()));
        } catch(final Exception ex) {
            logger.warn(String.format("Failed undoing video collection pop creation for pop '%s'", pop.getId(), ex));
        }
    }

    @Override
    public void addPopToVideoCollection(final VideoCollectionPopRequest request) throws ObjectNotFoundException, OptimisticLockingException, DaoException {
        try {

            final String videoCollectionId = request.getPop().getVideoCollectionId();
            final long videoCollectionVersion = request.getVideoCollectionVersion();

            if(!MongoUtils.exists(videoCollectionId, VideoCollection.class, mongoTemplate)) {
                throw new ObjectNotFoundException(String.format("Video collection '%s' not found.", videoCollectionId));
            }

            // update pop count in video collection
            final Criteria popCountCriteria = Criteria.where("id").is(videoCollectionId).and("metadata.version").is(videoCollectionVersion);
            final Query popCountQuery = new Query(popCountCriteria);

            final Update popCountUpdate = new Update();
            popCountUpdate.inc("popCount", 1);
            popCountUpdate.inc("metadata.version", videoCollectionVersion + 1);

            final WriteResult popCountWriteResult = mongoTemplate.updateFirst(popCountQuery, popCountUpdate, VideoCollection.class);
            if(popCountWriteResult.getN() != 1) {
                throw new OptimisticLockingException(String.format("Video collection '%s' previously modified.", videoCollectionId));
            }

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public void undoAddPopToVideoCollection(final VideoCollectionPopRequest request) {
        final VideoCollectionPop pop = request.getPop();
        final String videoCollectionId = pop.getVideoCollectionId();
        try {
            logger.info(String.format("Undoing pop count update for video collection '%s'.  Decrementing pop count.", videoCollectionId));

            final Criteria criteria = Criteria.where("id").is(videoCollectionId);
            final Query query = new Query(criteria);
            final Update update = new Update();
            update.inc("popCount", -1);
            update.inc("metadata.version", -1);

            mongoTemplate.updateFirst(query, update, VideoCollection.class);

            logger.info(String.format("Finished undoing pop count update for video collection '%s'", videoCollectionId));
        } catch(final Exception ex) {
            logger.warn(String.format("Failed undoing pop count update for video collection '%s'", videoCollectionId), ex);
        }
    }

}