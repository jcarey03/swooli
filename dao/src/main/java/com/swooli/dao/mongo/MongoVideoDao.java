package com.swooli.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;
import com.swooli.bo.ranking.VideoRatingAlgorithm;
import com.swooli.bo.video.Swink;
import com.swooli.bo.video.Video;
import com.swooli.bo.video.VideoComment;
import com.swooli.bo.video.VideoMetadata;
import com.swooli.bo.video.VideoPop;
import com.swooli.bo.video.VideoPreview;
import com.swooli.bo.video.VideoRating;
import com.swooli.bo.video.VideoRoot;
import com.swooli.bo.video.VideoRootMetadata;
import com.swooli.bo.video.VideoVote;
import com.swooli.bo.video.collection.VideoCollection;
import com.swooli.dao.DaoException;
import com.swooli.dao.ObjectNotFoundException;
import com.swooli.dao.OptimisticLockingException;
import com.swooli.dao.VideoDao;
import com.swooli.dao.paging.VideoPreviewPageResult;
import com.swooli.dao.paging.VideoRatingPageResult;
import com.swooli.dao.request.SwinkCreateRequest;
import com.swooli.dao.request.VideoCommentCreateRequest;
import com.swooli.dao.request.VideoCreateRequest;
import com.swooli.dao.request.VideoPopRequest;
import com.swooli.dao.request.VideoSpotlightRequest;
import com.swooli.dao.request.VideoVoteRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class MongoVideoDao implements VideoDao {

    private static Logger logger = LoggerFactory.getLogger(MongoVideoDao.class);

    private MongoTemplate mongoTemplate;

    private VideoRatingAlgorithm videoRatingAlgorithm;

    public void setMongoTemplate(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void setVideoRatingAlgorithm(final VideoRatingAlgorithm videoRatingAlgorithm) {
        this.videoRatingAlgorithm = videoRatingAlgorithm;
    }

    @Override
    public void createVideoRoot(final VideoRootMetadata metadata) throws DaoException {
        try {
            final VideoRoot videoRoot = new VideoRoot();
            videoRoot.setMetadata(metadata);
            mongoTemplate.insert(videoRoot);
            metadata.setId(videoRoot.getId());
        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public void createVideo(final VideoCreateRequest request) throws OptimisticLockingException, ObjectNotFoundException, DaoException {

        try {

            final VideoMetadata videoMetadata = request.getVideoMetadata();

            if(!MongoUtils.exists(videoMetadata.getVideoCollectionId(), VideoCollection.class, mongoTemplate)) {
                throw new ObjectNotFoundException(String.format("Video collection '%s' not found.", videoMetadata.getVideoCollectionId()));
            } else if(!MongoUtils.exists(videoMetadata.getVideoRootId(), VideoRoot.class, mongoTemplate)) {
                throw new ObjectNotFoundException(String.format("Video root '%s' not found.", videoMetadata.getVideoRootId()));
            }

            // precompute video ID
            videoMetadata.setId(new ObjectId().toString());

            // create video
            final Criteria videoCriteria = Criteria.where("id").is(videoMetadata.getVideoCollectionId()).and("metadata.version").is(request.getVideoCollectionVersion());
            final Query videoQuery = new Query(videoCriteria);

            final Update videoUpdate = new Update();
            videoUpdate.push("videos", videoMetadata);
            videoUpdate.inc("videoCount", 1);
            videoUpdate.inc("metadata.version", 1);

            final WriteResult writeResult = mongoTemplate.updateFirst(videoQuery, videoUpdate, VideoCollection.class);
            if(writeResult.getN() != 1) {
                throw new OptimisticLockingException(String.format("Video collection '%s' previously modified.", videoMetadata.getVideoCollectionId()));
            }

            /// create new video rating ///

            final VideoRating videoRating = new VideoRating();
            videoRating.setVideoCollectionId(videoMetadata.getVideoCollectionId());
            videoRating.setVideoId(videoMetadata.getId());
            videoRating.setCreationDate(videoMetadata.getAddedDate());
            mongoTemplate.insert(videoRating);

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }

    }

    @Override
    public void createSwink(final SwinkCreateRequest request) throws OptimisticLockingException, ObjectNotFoundException, DaoException {
        try {

            final Swink swink = request.getSwink();

            if(!MongoUtils.exists(swink.getVideoRootId(), VideoRoot.class, mongoTemplate)) {
                throw new ObjectNotFoundException(String.format("Video root '%s' not found.", swink.getVideoRootId()));
            }

            // precompute swink ID
            swink.setId(new ObjectId().toString());

            // create swink
            final Criteria criteria = Criteria.where("id").is(swink.getVideoRootId()).and("metadata.version").is(request.getVideoRootVersion());
            final Query query = new Query(criteria);

            final Update update = new Update();
            update.push("swinks", swink);
            update.inc("swinkCount", 1);
            update.inc("metadata.version", 1);

            final WriteResult writeResult = mongoTemplate.updateFirst(query, update, VideoRoot.class);
            if(writeResult.getN() != 1) {
                throw new OptimisticLockingException(String.format("Video root '%s' previously modified.", swink.getVideoRootId()));
            }

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public void createVideoComment(final VideoCommentCreateRequest request) throws OptimisticLockingException, ObjectNotFoundException, DaoException {
        try {

            final VideoComment comment = request.getVideoComment();

            if(!MongoUtils.exists(comment.getVideoRootId(), VideoRoot.class, mongoTemplate)) {
                throw new ObjectNotFoundException(String.format("Video root '%s' not found.", comment.getVideoRootId()));
            }

            // precompute comment ID
            comment.setId(new ObjectId().toString());

            // create comment
            final Criteria criteria = Criteria.where("id").is(comment.getVideoRootId()).and("metadata.version").is(request.getVideoRootVersion());
            final Query query = new Query(criteria);

            final Update update = new Update();
            update.push("comments", comment);
            update.inc("commentCount", 1);
            update.inc("metadata.version", 1);

            final WriteResult writeResult = mongoTemplate.updateFirst(query, update, VideoRoot.class);
            if(writeResult.getN() != 1) {
                throw new OptimisticLockingException(String.format("Video root '%s' previously modified.", comment.getVideoRootId()));
            }

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }


    @Override
    public void createVideoVote(final VideoVote vote) throws DaoException {
        try {
            mongoTemplate.insert(vote);
        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public void createVideoPop(final VideoPop pop) throws DaoException {
        try {
            mongoTemplate.insert(pop);
        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public boolean isVideoCreator(final String userProfileId, final String videoCollectionId, final String videoId) throws DaoException {
        try {

            final Criteria criteria = Criteria.where("id").is(videoCollectionId).and("videos.metadata._id").is(videoId);
            final Query query = new Query(criteria);
            query.fields().include("videos.metadata.addedBy.userProfileId");

            final VideoCollection videoCollection = mongoTemplate.findOne(query, VideoCollection.class);
            if(videoCollection != null) {
                for(final VideoPreview videoPreview : videoCollection.getVideos()) {
                    if(videoPreview.getMetadata().getAddedBy().getUserProfileId().equals(userProfileId)) {
                        return true;
                    }
                }
            }

            return false;

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public boolean isVideoCommentCreator(final String userProfileId, final String videoRootId, final String commentId) throws DaoException {
        try {

            final Criteria criteria = Criteria.where("id").is(videoRootId).and("comments._id").is(commentId);
            final Query query = new Query(criteria);
            query.fields().include("comments.createdBy.userProfileId");

            final VideoRoot videoRoot = mongoTemplate.findOne(query, VideoRoot.class);
            if(videoRoot != null) {
                for(final VideoComment comment : videoRoot.getComments()) {
                    if(comment.getCreatedBy().getUserProfileId().equals(userProfileId)) {
                        return true;
                    }
                }
            }

            return false;

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public boolean isSwinkCreator(final String userProfileId, final String videoRootId, final String swinkId) throws DaoException {
        try {

            final Criteria criteria = Criteria.where("id").is(videoRootId).and("swinks._id").is(swinkId);
            final Query query = new Query(criteria);
            query.fields().include("swinks.createdBy.userProfileId");

            final VideoRoot videoRoot = mongoTemplate.findOne(query, VideoRoot.class);
            if(videoRoot != null) {
                for(final Swink swink : videoRoot.getSwinks()) {
                    if(swink.getCreatedBy().getUserProfileId().equals(userProfileId)) {
                        return true;
                    }
                }
            }

            return false;

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public VideoRoot retrieveVideoRoot(final String id, Integer commentsBatchSize, Integer swinksBatchSize, Integer popsBatchSize) throws ObjectNotFoundException, DaoException {
        try {

            final int effectiveCommentsBatchSize = (commentsBatchSize == null) ? Integer.MAX_VALUE : commentsBatchSize;
            final int effectiveSwinksBatchSize = (swinksBatchSize == null) ? Integer.MAX_VALUE : swinksBatchSize;
            final int effectivePopsBatchSize = (popsBatchSize == null) ? Integer.MAX_VALUE : popsBatchSize;

            final Criteria criteria = Criteria.where("id").is(id);
            final Query query = new Query(criteria);
            query
                .fields()
                    .slice("comments", -effectiveCommentsBatchSize)  // most recent
                    .slice("swinks", -effectiveSwinksBatchSize);

            final VideoRoot videoRoot = mongoTemplate.findOne(query, VideoRoot.class);
            if(videoRoot == null) {
                throw new ObjectNotFoundException(String.format("Video root '%s' not found.", id));
            } else {

                // set video root id

                // metadata
                videoRoot.getMetadata().setId(videoRoot.getId());

                // comments
                for(final VideoComment videoComment : videoRoot.getComments()) {
                    videoComment.setVideoRootId(id);
                }

                // swinks
                for(final Swink swink : videoRoot.getSwinks()) {
                    swink.setVideoRootId(id);
                }

                // retrieve video pops

                if(videoRoot.getPopCount() > 0) {

                    final Criteria popCriteria = Criteria.where("videoRootId").is(id);
                    final Query popQuery = new Query(popCriteria);
                    popQuery.sort().on("creationDate", Order.DESCENDING);
                    popQuery.limit(effectivePopsBatchSize);

                    videoRoot.setPops(mongoTemplate.find(popQuery, VideoPop.class));
                }

                return videoRoot;
            }

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public List<Swink> retrieveSwinks(final String videoRootId) throws DaoException {
        try {

            final Criteria criteria = Criteria.where("id").is(videoRootId);
            final Query query = new Query(criteria);
            query.fields().include("swinks");
            query.sort().on("swinks.creationDate", Order.ASCENDING);

            final VideoRoot videoRoot = mongoTemplate.findOne(query, VideoRoot.class);
            if(videoRoot == null) {
                return new ArrayList<>();
            } else {

                // set video root id
                for(final Swink swink : videoRoot.getSwinks()) {
                    swink.setVideoRootId(videoRoot.getId());
                }

                return videoRoot.getSwinks();
            }

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public List<VideoComment> retrieveVideoComments(final String videoRootId) throws DaoException {
        try {

            final Criteria criteria = Criteria.where("id").is(videoRootId);
            final Query query = new Query(criteria);
            query.fields().include("comments");
            query.sort().on("comments.creationDate", Order.ASCENDING);

            final VideoRoot videoRoot = mongoTemplate.findOne(query, VideoRoot.class);
            if(videoRoot == null) {
                return new ArrayList<>();
            } else {

                // set video root id
                for(final VideoComment videoComment : videoRoot.getComments()) {
                    videoComment.setVideoRootId(videoRoot.getId());
                }

                return videoRoot.getComments();
            }

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public List<VideoPop> retrievePops(final String videoRootId) throws DaoException {
        try {
            final Criteria criteria = Criteria.where("videoRootId").is(videoRootId);
            final Query query = new Query(criteria);
            query.sort().on("creationDate", Order.ASCENDING);
            return mongoTemplate.find(query, VideoPop.class);
        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public Video retrieveVideo(final String videoCollectionId, final String videoId, final Integer commentsBatchSize,
            final Integer swinksBatchSize, final Integer popsBatchSize) throws ObjectNotFoundException, DaoException {
        try {

            final Video video = new Video();

            // video metadata
            final VideoMetadata videoMetadata = retrieveVideoMetadata(videoCollectionId, videoId);
            video.setMetadata(videoMetadata);

            // video rating
            final VideoRating videoRating = retrieveVideoRatingByVideoId(videoId);
            video.setRating(videoRating);

            // video root
            final VideoRoot videoRoot = retrieveVideoRoot(videoMetadata.getVideoRootId(), commentsBatchSize, swinksBatchSize, popsBatchSize);
            video.setRoot(videoRoot);

            return video;

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public VideoMetadata retrieveVideoMetadata(final String videoCollectionId, final String videoId) throws ObjectNotFoundException, DaoException {
        try {

            final Criteria criteria = Criteria.where("id").is(videoCollectionId).and("videos").elemMatch(Criteria.where("metadata._id").is(videoId));
            final Query query = new Query(criteria);

            final VideoCollection videoCollection = mongoTemplate.findOne(query, VideoCollection.class);
            if(videoCollection == null || videoCollection.getVideos() == null) {
                throw new ObjectNotFoundException(String.format("Video metadata '%s' not found.", videoId));
            } else {

                final VideoMetadata metadata = videoCollection.getVideos().get(0).getMetadata();

                // set video collection id
                metadata.setVideoCollectionId(videoCollectionId);

                return metadata;
            }

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    public Collection<VideoMetadata> retrieveVideoMetadatas(final String videoCollectionId, final Collection<String> ids) throws DaoException {
        try {

            final Collection<VideoMetadata> videoMetadatas = new ArrayList<>();

            final Criteria criteria = Criteria.where("id").is(videoCollectionId).and("videos.metadata._id").in(ids);
            final Query query = new Query(criteria);
            query.fields().include("videos.metadata");

            final Collection<VideoCollection> videoCollections = mongoTemplate.find(query, VideoCollection.class);
            for(final VideoCollection videoCollection : videoCollections) {
                for(final VideoPreview videoPreview : videoCollection.getVideos()) {

                    final VideoMetadata videoMetadata = videoPreview.getMetadata();

                    // set video collection id
                    videoMetadata.setVideoCollectionId(videoCollectionId);

                    videoMetadatas.add(videoMetadata);
                }
            }

            return videoMetadatas;

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    public Collection<VideoRating> retrieveVideoRatingsByVideoIds(final Collection<String> videoIds) throws DaoException {
        try {
            final Criteria criteria = Criteria.where("videoId").in(videoIds);
            final Query query = new Query(criteria);
            return mongoTemplate.find(query, VideoRating.class);
        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    public VideoRating retrieveVideoRatingByVideoId(final String videoId) throws ObjectNotFoundException, DaoException {
        try {
            final Criteria criteria = Criteria.where("videoId").is(videoId);
            final Query query = new Query(criteria);
            final VideoRating videoRating =  mongoTemplate.findOne(query, VideoRating.class);
            if(videoRating == null) {
                throw new ObjectNotFoundException(String.format("Video rating for video '%s' not found.", videoId));
            } else {
                return videoRating;
            }
        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    public List<VideoMetadata> retrieveSpotlightedVideoMetadatas(final String videoCollectionId) throws DaoException {
        try {

            final Criteria criteria = Criteria.where("id").is(videoCollectionId);

            final Query query = new Query(criteria);
            query.fields().include("videos.metadata");
            query.sort().on("spotlightedDate", Order.DESCENDING);

            final List<VideoMetadata> videoMetadatas = new ArrayList<>();

            final VideoCollection videoCollection =  mongoTemplate.findOne(query, VideoCollection.class);
            if(videoCollection == null) {
                return videoMetadatas;
            }

            for(final VideoPreview videoPreview : videoCollection.getVideos()) {
                final VideoMetadata videoMetadata = videoPreview.getMetadata();
                if(videoMetadata.isSpotlighted()) {
                    videoMetadata.setVideoCollectionId(videoCollectionId);
                    videoMetadatas.add(videoMetadata);
                }
            }

            return videoMetadatas;

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public VideoRatingPageResult retrieveVideoRatings(final String videoCollectionId, final Double lastRating,
            final Long lastDate, final Integer batchSize) throws DaoException {
        try {

            final double effectiveLastRating = (lastRating == null) ? Double.MAX_VALUE : lastRating;

            final long effectiveLastDate = (lastDate == null) ? Long.MAX_VALUE : lastDate;

            final int effectiveBatchSize = (batchSize == null) ? Integer.MAX_VALUE : batchSize;

            final Criteria criteria = Criteria.where("videoCollectionId").is(videoCollectionId).and("rating").lte(effectiveLastRating).orOperator(
                Criteria.where("rating").lt(effectiveLastRating),
                Criteria.where("creationDate").lt(effectiveLastDate));

            final Query query = new Query(criteria);
            query.limit(effectiveBatchSize)
                .sort()
                    .on("rating", Order.DESCENDING)
                    .on("creationDate", Order.DESCENDING);

            final List<VideoRating> videoRatings = mongoTemplate.find(query, VideoRating.class);

            final VideoRatingPageResult pageResult = new VideoRatingPageResult();
            pageResult.setRatings(videoRatings);
            pageResult.setBatchSize(batchSize);

            if(videoRatings.isEmpty()) {
                pageResult.setLastRating(lastRating);
                pageResult.setLastDate(lastDate);
                return pageResult;
            } else {
                final VideoRating lastVideoRating = videoRatings.get(videoRatings.size() - 1);
                pageResult.setLastRating(lastVideoRating.getRating());
                pageResult.setLastDate(lastVideoRating.getCreationDate());
            }

            return pageResult;

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public VideoRatingPageResult retrieveVideoRatings(final String videoCollectionId, final Long lastDate, final Integer batchSize) throws DaoException {
        try {

            final long effectiveLastDate = (lastDate == null) ? Long.MAX_VALUE : lastDate;

            final int effectiveBatchSize = (batchSize == null) ? Integer.MAX_VALUE : batchSize;

            final Criteria criteria = Criteria.where("videoCollectionId").is(videoCollectionId).and("creationDate").lt(effectiveLastDate);

            final Query query = new Query(criteria);
            query.limit(effectiveBatchSize).sort().on("creationDate", Order.DESCENDING);

            final List<VideoRating> videoRatings =  mongoTemplate.find(query, VideoRating.class);

            final VideoRatingPageResult pageResult = new VideoRatingPageResult();
            pageResult.setRatings(videoRatings);
            pageResult.setBatchSize(batchSize);

            if(videoRatings.isEmpty()) {
                pageResult.setLastDate(lastDate);
                return pageResult;
            } else {
                final VideoRating lastVideoRating = videoRatings.get(videoRatings.size() - 1);
                pageResult.setLastDate(lastVideoRating.getCreationDate());
            }

            return pageResult;

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public List<VideoPreview> retrieveSpotlightedVideoPreviews(final String videoCollectionId) throws DaoException {
        try {

            final Map<String, VideoPreview> videoIdToVideoPreview = new LinkedHashMap<>();

            // retrieve ordered video metadatas
            final List<VideoMetadata> videoMetadatas = retrieveSpotlightedVideoMetadatas(videoCollectionId);

            final Map<String, List<String>> videoRootIdToVideoIds = new HashMap<>();
            for(final VideoMetadata videoMetadata : videoMetadatas) {

                final VideoPreview videoPreview = new VideoPreview();

                // add video metadata to video preview
                videoPreview.setMetadata(videoMetadata);

                // associate video ID to video preview for fast lookup
                videoIdToVideoPreview.put(videoMetadata.getId(), videoPreview);

                // associate video root ID to video IDs to later add video root to video preview
                List<String> videoRootVideoIds = videoRootIdToVideoIds.get(videoMetadata.getVideoRootId());
                if(videoRootVideoIds == null) {
                    videoRootVideoIds = new ArrayList<>();
                    videoRootIdToVideoIds.put(videoMetadata.getVideoRootId(), videoRootVideoIds);
                }
                videoRootVideoIds.add(videoMetadata.getId());

            }

            // retrieve video ratings
            final Collection<VideoRating> videoRatings = retrieveVideoRatingsByVideoIds(videoIdToVideoPreview.keySet());
            for(final VideoRating videoRating : videoRatings) {
                videoIdToVideoPreview.get(videoRating.getVideoId()).setRating(videoRating);
            }

            // create query to retrieve video statistics
            final Criteria feedbackCountsCriteria = Criteria.where("id").in(videoRootIdToVideoIds.keySet());
            final Query feedbackCountsQuery = new Query(feedbackCountsCriteria);
            feedbackCountsQuery.fields().include("commentCount").include("swinkCount").include("popCount");

            // retrieve video roots
            final Collection<VideoRoot> videoRoots = mongoTemplate.find(feedbackCountsQuery, VideoRoot.class);

            // set video statistics in video previews
            for(final VideoRoot videoRoot : videoRoots) {
                for(final String videoId : videoRootIdToVideoIds.get(videoRoot.getId())) {
                    final VideoPreview videoPreview = videoIdToVideoPreview.get(videoId);
                    videoPreview.setCommentCount(videoRoot.getCommentCount());
                    videoPreview.setSwinkCount(videoRoot.getSwinkCount());
                    videoPreview.setPopCount(videoRoot.getPopCount());
                }
            }

            return new ArrayList<>(videoIdToVideoPreview.values());

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public VideoPreviewPageResult retrieveVideoPreviews(final String videoCollectionId, final Double lastRating,
            final Long lastDate, final Integer batchSize) throws DaoException {
        try {

            final Map<String, VideoPreview> videoIdToVideoPreview = new LinkedHashMap<>(batchSize);

            // retrieve ordered video ratings
            final VideoRatingPageResult videoRatingPageResult = retrieveVideoRatings(videoCollectionId, lastRating, lastDate, batchSize);
            for(final VideoRating videoRating : videoRatingPageResult.getRatings()) {

                final VideoPreview videoPreview = new VideoPreview();

                // add video rating to video preview
                videoPreview.setRating(videoRating);

                // associate video ID to video preview for fast lookup
                videoIdToVideoPreview.put(videoRating.getVideoId(), videoPreview);
            }

            // retrieve video metadatas
            final Collection<VideoMetadata> videoMetadatas = retrieveVideoMetadatas(videoCollectionId, videoIdToVideoPreview.keySet());

            final Map<String, List<String>> videoRootIdToVideoIds = new HashMap<>();
            for(final VideoMetadata videoMetadata : videoMetadatas) {

                final VideoPreview videoPreview = videoIdToVideoPreview.get(videoMetadata.getId());
                if(videoPreview == null) {
                    continue;
                }

                // associate video root ID to video IDs to later add video root to video preview
                List<String> videoRootVideoIds = videoRootIdToVideoIds.get(videoMetadata.getVideoRootId());
                if(videoRootVideoIds == null) {
                    videoRootVideoIds = new ArrayList<>();
                    videoRootIdToVideoIds.put(videoMetadata.getVideoRootId(), videoRootVideoIds);
                }
                videoRootVideoIds.add(videoMetadata.getId());

                // add video metadata to video preview
                videoPreview.setMetadata(videoMetadata);
            }

            // create query to retrieve video statistics
            final Criteria feedbackCountsCriteria = Criteria.where("id").in(videoRootIdToVideoIds.keySet());
            final Query feedbackCountsQuery = new Query(feedbackCountsCriteria);
            feedbackCountsQuery.fields().include("commentCount").include("swinkCount").include("popCount");

            // retrieve video roots
            final Collection<VideoRoot> videoRoots = mongoTemplate.find(feedbackCountsQuery, VideoRoot.class);

            // set video statistics in video previews
            for(final VideoRoot videoRoot : videoRoots) {
                for(final String videoId : videoRootIdToVideoIds.get(videoRoot.getId())) {
                    final VideoPreview videoPreview = videoIdToVideoPreview.get(videoId);
                    videoPreview.setCommentCount(videoRoot.getCommentCount());
                    videoPreview.setSwinkCount(videoRoot.getSwinkCount());
                    videoPreview.setPopCount(videoRoot.getPopCount());
                }
            }

            final List<VideoPreview> videoPreviews = new ArrayList<>(videoIdToVideoPreview.values());

            final VideoPreviewPageResult pageResult = new VideoPreviewPageResult();
            pageResult.setPreviews(videoPreviews);
            pageResult.setBatchSize(batchSize);

            if(videoPreviews.isEmpty()) {
                pageResult.setLastRating(lastRating);
                pageResult.setLastDate(lastDate);
                return pageResult;
            } else {
                final VideoRating lastVideoRating = videoPreviews.get(videoPreviews.size() - 1).getRating();
                pageResult.setLastRating(lastVideoRating.getRating());
                pageResult.setLastDate(lastVideoRating.getCreationDate());
            }

            return pageResult;

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public VideoPreviewPageResult retrieveVideoPreviews(final String videoCollectionId, final Long lastDate, final Integer batchSize) throws DaoException {
        try {

            final Map<String, VideoPreview> videoIdToVideoPreview = new LinkedHashMap<>(batchSize);

            // retrieve ordered video ratings
            final VideoRatingPageResult videoRatingPageResult = retrieveVideoRatings(videoCollectionId, lastDate, batchSize);
            for(final VideoRating videoRating : videoRatingPageResult.getRatings()) {

                final VideoPreview videoPreview = new VideoPreview();

                // add video rating to video preview
                videoPreview.setRating(videoRating);

                // associate video ID to video preview for fast lookup
                videoIdToVideoPreview.put(videoRating.getVideoId(), videoPreview);
            }

            // retrieve video metadatas
            final Collection<VideoMetadata> videoMetadatas = retrieveVideoMetadatas(videoCollectionId, videoIdToVideoPreview.keySet());

            final Map<String, List<String>> videoRootIdToVideoIds = new HashMap<>();
            for(final VideoMetadata videoMetadata : videoMetadatas) {

                final VideoPreview videoPreview = videoIdToVideoPreview.get(videoMetadata.getId());
                if(videoPreview == null) {
                    continue;
                }

                // associate video root ID to video IDs to later add video root to video preview
                List<String> videoRootVideoIds = videoRootIdToVideoIds.get(videoMetadata.getVideoRootId());
                if(videoRootVideoIds == null) {
                    videoRootVideoIds = new ArrayList<>();
                    videoRootIdToVideoIds.put(videoMetadata.getVideoRootId(), videoRootVideoIds);
                }
                videoRootVideoIds.add(videoMetadata.getId());

                // add video metadata to video preview
                videoPreview.setMetadata(videoMetadata);
            }

            // create query to retrieve video statistics
            final Criteria feedbackCountsCriteria = Criteria.where("id").in(videoRootIdToVideoIds.keySet());
            final Query feedbackCountsQuery = new Query(feedbackCountsCriteria);
            feedbackCountsQuery.fields().include("commentCount").include("swinkCount").include("popCount");

            // retrieve video roots
            final Collection<VideoRoot> videoRoots = mongoTemplate.find(feedbackCountsQuery, VideoRoot.class);

            // set video statistics in video previews
            for(final VideoRoot videoRoot : videoRoots) {
                for(final String videoId : videoRootIdToVideoIds.get(videoRoot.getId())) {
                    final VideoPreview videoPreview = videoIdToVideoPreview.get(videoId);
                    videoPreview.setCommentCount(videoRoot.getCommentCount());
                    videoPreview.setSwinkCount(videoRoot.getSwinkCount());
                    videoPreview.setPopCount(videoRoot.getPopCount());
                }
            }

            final List<VideoPreview> videoPreviews = new ArrayList<>(videoIdToVideoPreview.values());

            final VideoPreviewPageResult pageResult = new VideoPreviewPageResult();
            pageResult.setPreviews(videoPreviews);
            pageResult.setBatchSize(batchSize);

            if(videoPreviews.isEmpty()) {
                pageResult.setLastDate(lastDate);
                return pageResult;
            } else {
                final VideoRating lastVideoRating = videoPreviews.get(videoPreviews.size() - 1).getRating();
                pageResult.setLastDate(lastVideoRating.getCreationDate());
            }

            return pageResult;

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public void deleteVideoComment(final String videoRootId, final String videoCommentId) throws DaoException {
        try {

            final Criteria videoCommentCriteria = Criteria.where("id").is(videoRootId);
            final Query videoCommentQuery = new Query(videoCommentCriteria);

            /*
             * Developer Note: The MongoTemplate does not currently support pulling an
             * array element based on a property of the element using dot notation.  Therefore,
             * the raw DBObject is used.
             */
            final Update update = new Update();
            update.pull("comments", new BasicDBObject("_id", videoCommentId))
                  .inc("commentCount", -1);

            mongoTemplate.updateFirst(videoCommentQuery, update, VideoRoot.class);

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public void deleteSwink(final String videoRootId, final String swinkId) throws DaoException {
        try {

            final Criteria videoSwinkCriteria = Criteria.where("id").is(videoRootId);
            final Query swinkQuery = new Query(videoSwinkCriteria);

            /*
             * Developer Note: The MongoTemplate does not currently support pulling an
             * array element based on a property of the element using dot notation.  Therefore,
             * the raw DBObject is used.
             */
            final Update update = new Update();
            update.pull("swinks", new BasicDBObject("_id", swinkId))
                  .inc("swinkCount", -1);

            mongoTemplate.updateFirst(swinkQuery, update, VideoRoot.class);

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }



    @Override
    public void deleteVideo(final String videoCollectionId, final String videoId) throws DaoException {
        try {

            final Criteria videoCriteria = Criteria.where("id").is(videoCollectionId);
            final Query videoQuery = new Query(videoCriteria);

            /*
             * Developer Note: The MongoTemplate does not currently support pulling an
             * array element based on a property of the element using dot notation.  Therefore,
             * the raw DBObject is used.
             */
            final Update videoUpdate = new Update();
            videoUpdate.pull("videos", new BasicDBObject("metadata._id", videoId))
                  .inc("videoCount", -1);

            mongoTemplate.updateFirst(videoQuery, videoUpdate, VideoCollection.class);

            // delete video votes

            final Criteria videoVotesCriteria = Criteria.where("videoId").is(videoId);
            final Query videoVotesQuery = new Query(videoVotesCriteria);
            mongoTemplate.remove(videoVotesQuery, VideoVote.class);

            // delete video rating

            final Criteria ratingCriteria = Criteria.where("videoId").is(videoId);
            final Query ratingQuery = new Query(ratingCriteria);
            mongoTemplate.remove(ratingQuery, VideoRating.class);

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public void deleteVideoRoot(final String id) throws DaoException {
        try {

            // get video ids to delete
            final Criteria videoCriteria = Criteria.where("videoRootId").is(id);
            final Query videoQuery = new Query(videoCriteria);
            videoQuery.fields().include("videos.metadata._id");
            final List<VideoCollection> videoCollections = mongoTemplate.find(videoQuery, VideoCollection.class);

            // delete videos
            for(final VideoCollection videoCollection : videoCollections) {
                for(final VideoPreview videoPreview : videoCollection.getVideos()) {
                    try {
                        deleteVideo(videoCollection.getId(), videoPreview.getMetadata().getId());
                    } catch(final DaoException de) {
                        logger.warn(String.format("Failed to delete video '%s' while deleting video root '%s'", videoPreview.getMetadata().getId(), id), de);
                    }
                }
            }

            // delete video root
            final Criteria videoRootCriteria = Criteria.where("id").is(id);
            final Query videoRootQuery = new Query(videoRootCriteria);
            mongoTemplate.remove(videoRootQuery, VideoRoot.class);

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

    @Override
    public void spotlightVideo(final VideoSpotlightRequest request) throws ObjectNotFoundException, OptimisticLockingException, DaoException {
        try {

            final String videoCollectionId = request.getVideoCollectionId();
            final String videoId = request.getVideoId();
            final long videoVersion = request.getVideoVersion();

            if(!MongoUtils.exists(videoCollectionId, VideoCollection.class, mongoTemplate)) {
                throw new ObjectNotFoundException(String.format("Video collection '%s' not found.", videoCollectionId));
            }

            final Criteria videoCriteria = Criteria.where("id").is(videoCollectionId).and("videos.metadata._id").is(videoId);
            final Query videoExists = new Query(videoCriteria);
            videoExists.fields().include("_id");

            if(mongoTemplate.findOne(videoExists, VideoCollection.class) == null) {
                throw new ObjectNotFoundException(String.format("Video '%s' not found.", videoId));
            }

            // update video
            final Criteria criteria = videoCriteria.and("videos.metadata.version").is(videoVersion);
            final Query videoUpdateQuery = new Query(criteria);

            final Update update = new Update();
            update.set("videos.$.metadata.spotlighted", request.isSpotlighted());
            update.inc("videos.$.metadata.version", 1);

            final WriteResult writeResult = mongoTemplate.updateFirst(videoUpdateQuery, update, VideoCollection.class);
            if(writeResult.getN() != 1) {
                throw new OptimisticLockingException(String.format("Video '%s' previously modified.", videoCollectionId));
            }

        } catch(final DataAccessException dae) {
            throw new DaoException(dae);
        }
    }

//    @Override
//    public void updateVideo(final VideoUpdateRequest request) throws ObjectNotFoundException, OptimisticLockingException, DaoException {
//        try {
//
//            final String videoCollectionId = request.getVideoCollectionId();
//            final String videoId = request.getVideoId();
//            final long videoVersion = request.getVideoVersion();
//
//            if(!MongoUtils.exists(videoCollectionId, VideoCollection.class, mongoTemplate)) {
//                throw new ObjectNotFoundException(String.format("Video collection '%s' not found.", videoCollectionId));
//            }
//
//            final Criteria videoCriteria = Criteria.where("id").is(videoCollectionId).and("videos.metadata._id").is(videoId);
//            final Query videoExists = new Query(videoCriteria);
//            videoExists.fields().include("_id");
//
//            if(mongoTemplate.findOne(videoExists, VideoCollection.class) == null) {
//                throw new ObjectNotFoundException(String.format("Video '%s' not found.", videoId));
//            }
//
//            // update video
//            final Criteria criteria = videoCriteria.and("videos.metadata.version").is(videoVersion);
//            final Query videoUpdateQuery = new Query(criteria);
//
//            final Update update = new Update();
//            MongoUtils.updateField("videos.$.metadata.title", request.getTitle(), update);
//            MongoUtils.updateField("videos.$.metadata.spotlighted", request.isSpotlighted(), update);
//            update.inc("videos.$.metadata.version", 1);
//
//            final WriteResult writeResult = mongoTemplate.updateFirst(videoUpdateQuery, update, VideoCollection.class);
//            if(writeResult.getN() != 1) {
//                throw new OptimisticLockingException(String.format("Video '%s' previously modified.", videoCollectionId));
//            }
//
//        } catch(final DataAccessException dae) {
//            throw new DaoException(dae);
//        }
//    }

    @Override
    public void addVoteToVideoRating(final VideoVoteRequest request) throws ObjectNotFoundException, OptimisticLockingException, DaoException {
        try {

            final long videoRatingVersion = request.getVideoRatingVersion();
            final VideoVote vote = request.getVote();
            final Boolean previousUpVote = vote.isUpVote();

            final Criteria videoRatingCriteria = Criteria.where("videoId").is(vote.getVideoId());
            final Query videoRatingQuery = new Query(videoRatingCriteria);
            final VideoRating videoRating = mongoTemplate.findOne(videoRatingQuery, VideoRating.class);
            if(videoRating == null) {
                throw new ObjectNotFoundException(String.format("Video rating for video '%s' not found.", vote.getVideoId()));
            }

            int currentUpVoteCount = videoRating.getUpVoteCount();
            int currentDownVoteCount = videoRating.getDownVoteCount();
            if(previousUpVote != null) {
                if(previousUpVote) {
                    currentUpVoteCount--;
                } else {
                    currentDownVoteCount--;
                }
            }

            if(vote.isUpVote()) {
                currentUpVoteCount++;
            } else {
                currentDownVoteCount++;
            }

            final double rating = videoRatingAlgorithm.computeRating(currentUpVoteCount, currentDownVoteCount);

            /// update rating ///

            final Criteria videoRatingVersionCriteria = videoRatingCriteria.and("version").is(videoRatingVersion);
            final Query videoRatingVersionQuery = new Query(videoRatingVersionCriteria);

            final Update videoRatingUpdate = new Update();
            videoRatingUpdate.set("upVoteCount", currentUpVoteCount);
            videoRatingUpdate.set("downVoteCount", currentDownVoteCount);
            videoRatingUpdate.set("rating", rating);
            videoRatingUpdate.inc("version", videoRatingVersion + 1);

            final WriteResult writeResult = mongoTemplate.updateFirst(videoRatingVersionQuery, videoRatingUpdate, VideoRating.class);
            if(writeResult.getN() != 1) {
                throw new OptimisticLockingException(String.format("Video rating for video '%s' previously modified.", vote.getVideoId()));
            }

        } catch(final DataAccessException de) {
            throw new DaoException(de);
        }
    }

    @Override
    public void undoAddVoteToVideoRating(VideoVoteRequest request) {
        final VideoVote vote = request.getVote();
        final String videoId = vote.getVideoId();
        try {
            logger.info(String.format("Undoing vote update to video rating for video '%s' and vote '%'s.", videoId, vote.getId()));

            final Boolean previousUpVote = vote.isUpVote();

            final Criteria videoRatingCriteria = Criteria.where("videoId").is(vote.getVideoId());
            final Query videoRatingQuery = new Query(videoRatingCriteria);
            final VideoRating videoRating = mongoTemplate.findOne(videoRatingQuery, VideoRating.class);
            if(videoRating == null) {
                return;
            }

            int currentUpVoteCount = videoRating.getUpVoteCount();
            int currentDownVoteCount = videoRating.getDownVoteCount();
            if(previousUpVote != null) {
                if(previousUpVote) {
                    currentUpVoteCount++;
                } else {
                    currentDownVoteCount++;
                }
            }

            if(vote.isUpVote()) {
                currentUpVoteCount--;
            } else {
                currentDownVoteCount--;
            }

            final double rating = videoRatingAlgorithm.computeRating(currentUpVoteCount, currentDownVoteCount);

            /// update rating ///

            final Criteria videoIdVersionCriteria = Criteria.where("videoId").is(vote.getVideoId());
            final Query videoRatingVersionQuery = new Query(videoIdVersionCriteria);

            final Update videoRatingUpdate = new Update();
            videoRatingUpdate.set("upVoteCount", currentUpVoteCount);
            videoRatingUpdate.set("downVoteCount", currentDownVoteCount);
            videoRatingUpdate.set("rating", rating);

            mongoTemplate.updateFirst(videoRatingVersionQuery, videoRatingUpdate, VideoRating.class);

            logger.info(String.format("Finished undoing vote update for vote '%s'", vote.getId()));
        } catch(final Exception ex) {
            logger.warn(String.format("Failed undoing vote update for vote '%s'", vote.getId()), ex);
        }
    }

    @Override
    public void addPopToVideoRoot(final VideoPopRequest request) throws ObjectNotFoundException, OptimisticLockingException, DaoException {
        try {

            final long videoRootVersion = request.getVideoRootVersion();
            final VideoPop pop = request.getPop();
            final String videoRootId = pop.getVideoRootId();

            final Criteria videoRootCriteria = Criteria.where("id").is(videoRootId);
            final Query videoRootQuery = new Query(videoRootCriteria);
            final VideoRoot videoRoot = mongoTemplate.findOne(videoRootQuery, VideoRoot.class);
            if(videoRoot == null) {
                throw new ObjectNotFoundException(String.format("Video root '%s' not found.", videoRootId));
            }

            // update pop count ///

            final Criteria videoRootVersionCriteria = Criteria.where("id").is(videoRootId).and("metadata.version").is(videoRootVersion);
            final Query videoRootVersionQuery = new Query(videoRootVersionCriteria);

            final Update videoRootUpdate = new Update();
            videoRootUpdate.set("popCount", -1);
            videoRootUpdate.inc("version", videoRootVersion + 1);

            final WriteResult writeResult = mongoTemplate.updateFirst(videoRootVersionQuery, videoRootUpdate, VideoRoot.class);
            if(writeResult.getN() != 1) {
                throw new OptimisticLockingException(String.format("Video root '%s' previously modified.", videoRootId));
            }

        } catch(final DataAccessException de) {
            throw new DaoException(de);
        }
    }

    @Override
    public void undoAddPopToVideoRoot(VideoPopRequest request) {
        final VideoPop pop = request.getPop();
        final String videoRootId = pop.getVideoRootId();
        try {

            logger.info(String.format("Undoing video pop creation for pop '%s' and video root '%s'", pop.getId(), pop.getVideoRootId()));


            final Criteria criteria = Criteria.where("id").is(videoRootId);
            final Query videoRootVersionQuery = new Query(criteria);

            final Update videoRootUpdate = new Update();
            videoRootUpdate.set("popCount", 1);

            mongoTemplate.updateFirst(videoRootVersionQuery, videoRootUpdate, VideoRoot.class);

            logger.info(String.format("Finished undoing video pop creation for pop '%s'", pop.getId()));

        } catch(final Exception ex) {
            logger.warn(String.format("Failed undoing video pop creation for pop '%s'", pop.getId(), ex));
        }
    }

    @Override
    public void undoCreateVideoVote(VideoVoteRequest request) {
        final VideoVote vote = request.getVote();
        try {
            logger.info(String.format("Undoing video vote creation for vote '%s'", vote.getId()));
            mongoTemplate.remove(request.getVote());
            logger.info(String.format("Finished undoing video vote creation for vote '%s'", vote.getId()));
        } catch(final Exception ex) {
            logger.warn(String.format("Failed undoing video vote creation for vote '%s'", vote.getId(), ex));
        }
    }

    @Override
    public void undoCreateVideo(final VideoCreateRequest request) {
        final String videoCollectionId = request.getVideoMetadata().getVideoCollectionId();
        final String videoId = request.getVideoMetadata().getId();
        try {

            logger.info(String.format("Undoing video creation for video collection '%s' and video '%s'", videoCollectionId, videoId));

            final Criteria videoCriteria = Criteria.where("id").is(videoCollectionId).and("videos.metadata._id").is(videoId);
            final Query videoQuery = new Query(videoCriteria);

            /*
             * Developer Note: The MongoTemplate does not currently support pulling an
             * array element based on a property of the element using dot notation.  Therefore,
             * the raw DBObject is used.
             */
            final Update videoUpdate = new Update();
            videoUpdate.pull("videos", new BasicDBObject("metadata._id", videoId))
                  .inc("videoCount", -1);

            mongoTemplate.updateFirst(videoQuery, videoUpdate, VideoCollection.class);

            // delete video rating

            logger.info(String.format("Deleting video rating for video '%s'", videoId));

            final Criteria ratingCriteria = Criteria.where("videoId").is(videoId);
            final Query ratingQuery = new Query(ratingCriteria);
            mongoTemplate.remove(ratingQuery, VideoRating.class);

            logger.info(String.format("Deleted video rating for video '%s'", videoId));

            logger.info(String.format("Finished undoing video creation for video '%s'", videoId));

        } catch(final Exception ex) {
            logger.warn(String.format("Failed undoing video creation for video '%s'", videoId, ex));
        }
    }

    @Override
    public void undoCreateVideoPop(final VideoPopRequest request) {
        final VideoPop pop = request.getPop();
        try {
            logger.info(String.format("Undoing video pop creation for pop '%s'", pop.getId()));
            mongoTemplate.remove(request.getPop());
            logger.info(String.format("Finished undoing video pop creation for pop '%s'", pop.getId()));
        } catch(final Exception ex) {
            logger.warn(String.format("Failed undoing video pop creation for pop '%s'", pop.getId(), ex));
        }
    }

}