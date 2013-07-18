package com.swooli.dao.mongo;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration("/dao-context.xml")
@Ignore
public class MongoVideoDaoTest extends TestBase {

    @Autowired
    MongoVideoDao videoDao;

    @Autowired
    MongoVideoCollectionDao videoCollectionDao;

//    @Test
//    public void testCreateVideoRoot() {
//
//        final VideoRoot root = videoDao.retrieveVideoRoot("50a66ad6e4b0decb049e324e", null, null);
//        root.getMetadata().setId(null);
//        videoDao.createVideoRoot(root.getMetadata());
//        assertNotNull(root.getMetadata().getId());
//    }
//
//    @Test
//    public void testCreateVideo() {
//
//        final VideoCollection videoCollection = mongoTemplate.findById("50a5a5b3e4b0b112181908e0", VideoCollection.class);
//        final VideoPreview preview = videoCollection.getVideos().get(0);
//        final VideoRoot root = mongoTemplate.findById(preview.getMetadata().getVideoRootId(), VideoRoot.class);
//
//        final VideoMetadata metadata = preview.getMetadata();
//        metadata.setId(null);
//        metadata.setVideoCollectionId(videoCollection.getId());
//
//        final VideoCreateRequest request = new VideoCreateRequest();
//        request.setVideoCollectionVersion(videoCollection.getMetadata().getVersion());
//        request.setVideoMetadata(metadata);
//
//        videoDao.createVideo(request);
//
//        assertNotNull(metadata.getId());
//
//        final VideoCollection dbVideoCollection = mongoTemplate.findById("50a5a5b3e4b0b112181908e0", VideoCollection.class);
//        assertEquals(videoCollection.getVideoCount() + 1, dbVideoCollection.getVideoCount());
//    }
//
//    @Test(expected=OptimisticLockingException.class)
//    public void testCreateVideoStaleVersion() {
//
//        final VideoCollection videoCollection = mongoTemplate.findById("50a5a5b3e4b0b112181908e0", VideoCollection.class);
//        final VideoPreview preview = videoCollection.getVideos().get(0);
//
//        final VideoMetadata metadata = preview.getMetadata();
//        metadata.setId(null);
//        metadata.setVideoCollectionId(videoCollection.getId());
//
//        final VideoCreateRequest request = new VideoCreateRequest();
//        request.setVideoCollectionVersion(videoCollection.getMetadata().getVersion() - 1);
//        request.setVideoMetadata(metadata);
//
//        videoDao.createVideo(request);
//
//    }
//
//    @Test(expected=ObjectNotFoundException.class)
//    public void testCreateVideoUnknownCollection() {
//
//        final VideoCollection videoCollection = mongoTemplate.findById("50a5a5b3e4b0b112181908e0", VideoCollection.class);
//        final VideoPreview preview = videoCollection.getVideos().get(0);
//
//        final VideoMetadata metadata = preview.getMetadata();
//        metadata.setId(null);
//        metadata.setVideoCollectionId("foobar");
//
//        final VideoCreateRequest request = new VideoCreateRequest();
//        request.setVideoCollectionVersion(videoCollection.getMetadata().getVersion());
//        request.setVideoMetadata(metadata);
//
//        videoDao.createVideo(request);
//
//    }
//
//    @Test(expected=ObjectNotFoundException.class)
//    public void testCreateVideoUnknownRoot() {
//
//        final VideoCollection videoCollection = mongoTemplate.findById("50a5a5b3e4b0b112181908e0", VideoCollection.class);
//        final VideoPreview preview = videoCollection.getVideos().get(0);
//
//        final VideoMetadata metadata = preview.getMetadata();
//        metadata.setId(null);
//        metadata.setVideoCollectionId(videoCollection.getId());
//        metadata.setVideoRootId("foobar");
//
//        final VideoCreateRequest request = new VideoCreateRequest();
//        request.setVideoCollectionVersion(videoCollection.getMetadata().getVersion());
//        request.setVideoMetadata(metadata);
//
//        videoDao.createVideo(request);
//
//    }
//
//    @Test
//    public void testCreateVideoComment() {
//
//        // create new video collection
//        final VideoCollection videoCollection = mongoTemplate.findById("50a5a5b3e4b0b112181908e0", VideoCollection.class);
//        videoCollection.getMetadata().setId(null);
//        videoCollectionDao.createVideoCollection(videoCollection.getMetadata());
//
//        final VideoPreview preview = videoCollection.getVideos().get(0);
//        final VideoRoot root = mongoTemplate.findById(preview.getMetadata().getVideoRootId(), VideoRoot.class);
//
//        // create comment
//        final VideoComment videoComment = new VideoComment();
//        videoComment.setVideoRootId(root.getId());
//
//        // create comment request
//        final VideoCommentCreateRequest request = new VideoCommentCreateRequest();
//        request.setVideoComment(videoComment);
//        request.setVideoRootVersion(root.getMetadata().getVersion());
//
//        videoDao.createVideoComment(request);
//
//        final VideoRoot updatedRoot = mongoTemplate.findById(preview.getMetadata().getVideoRootId(), VideoRoot.class);
//
//        assertNotNull(videoComment.getId());
//        assertEquals(root.getCommentCount() + 1, updatedRoot.getCommentCount());
//    }
//
//    @Test(expected=OptimisticLockingException.class)
//    public void testCreateVideoCommentStale() {
//
//        // create new video collection
//        final VideoCollection videoCollection = mongoTemplate.findById("50a5a5b3e4b0b112181908e0", VideoCollection.class);
//        videoCollection.getMetadata().setId(null);
//        videoCollectionDao.createVideoCollection(videoCollection.getMetadata());
//
//        final VideoPreview preview = videoCollection.getVideos().get(0);
//        final VideoRoot root = mongoTemplate.findById(preview.getMetadata().getVideoRootId(), VideoRoot.class);
//
//        // create comment
//        final VideoComment videoComment = new VideoComment();
//        videoComment.setVideoRootId(root.getId());
//
//        // create comment request
//        final VideoCommentCreateRequest request = new VideoCommentCreateRequest();
//        request.setVideoComment(videoComment);
//        request.setVideoRootVersion(root.getMetadata().getVersion() - 1);
//
//        videoDao.createVideoComment(request);
//    }
//
//    @Test(expected=ObjectNotFoundException.class)
//    public void testCreateVideoCommentUnknownRoot() {
//
//        // create new video collection
//        final VideoCollection videoCollection = mongoTemplate.findById("50a5a5b3e4b0b112181908e0", VideoCollection.class);
//        videoCollection.getMetadata().setId(null);
//        videoCollectionDao.createVideoCollection(videoCollection.getMetadata());
//
//        final VideoPreview preview = videoCollection.getVideos().get(0);
//        final VideoRoot root = mongoTemplate.findById(preview.getMetadata().getVideoRootId(), VideoRoot.class);
//
//        // create comment
//        final VideoComment videoComment = new VideoComment();
//        videoComment.setVideoRootId("foobar");
//
//        // create comment request
//        final VideoCommentCreateRequest request = new VideoCommentCreateRequest();
//        request.setVideoComment(videoComment);
//        request.setVideoRootVersion(root.getMetadata().getVersion());
//
//        videoDao.createVideoComment(request);
//    }
//
//    @Test
//    public void testDeleteVideoRoot() {
//
//        final VideoRoot root = videoDao.retrieveVideoRoot("50a66ad6e4b0decb049e324e", null, null);
//        root.getMetadata().setId(null);
//        videoDao.createVideoRoot(root.getMetadata());
//
//        videoDao.deleteVideoRoot(root.getMetadata().getId());
//
//        try {
//            videoDao.retrieveVideoRoot(root.getMetadata().getId(), null, null);
//            fail("Failed to delete video root.");
//        } catch(final ObjectNotFoundException onfe) {}
//    }
//
////    @Test
////    public void testDeleteVideo() {
////
////        // create new video collection
////        final VideoCollection videoCollection = mongoTemplate.findById("50a5a5b3e4b0b112181908e0", VideoCollection.class);
////        videoCollection.getMetadata().setId(null);
////        videoCollectionDao.createVideoCollection(videoCollection.getMetadata());
////        final String rootId = videoCollection.getVideos().get(0).getMetadata().getVideoRootId();
////        final VideoRoot root = mongoTemplate.findById(rootId, VideoRoot.class);
////
////        videoDao.deleteVideo("50a5a5b3e4b0b112181908cb");
////
////        final VideoRoot dbRoot = mongoTemplate.findById(rootId, VideoRoot.class);
////
////        final VideoCollection updatedVideoCollection = mongoTemplate.findById(videoCollection.getId(), VideoCollection.class);
////        assertEquals(videoCollection.getVideoCount() - 1, updatedVideoCollection.getVideoCount()); //  add one, delete one, so the same
////        assertEquals(videoCollection.getVideos().size() - 1, updatedVideoCollection.getVideos().size());
////
////        assertEquals(root.getNumChildren() - 1, dbRoot.getNumChildren());
////
////        // check that rating was deleted
////        assertNull(mongoTemplate.findById("50a6acc3e4b0b4e1d969f3de", VideoRating.class));
////
////    }
//
//    @Test
//    public void testDeleteVideoComment() {
//
//        final Criteria criteria = Criteria.where("comments").exists(true);
//        final Query query = new Query(criteria);
//        final VideoRoot root = mongoTemplate.findOne(query, VideoRoot.class);
//
//        videoDao.deleteVideoComment(root.getComments().get(0).getId());
//
//        final VideoRoot updatedRoot = mongoTemplate.findById(root.getId(), VideoRoot.class);
//        assertEquals(root.getComments().size() - 1, updatedRoot.getComments().size());
//        assertEquals(root.getCommentCount() - 1, updatedRoot.getCommentCount());
//
//    }
//
//    @Test
//    public void testDeleteVideoCommentNonExistent() {
//        videoDao.deleteVideoComment(new ObjectId().toString());
//    }
//
//    @Test
//    public void testDeleteSwink() {
//
//        final Criteria criteria = Criteria.where("swinks").exists(true);
//        final Query query = new Query(criteria);
//        final VideoRoot root = mongoTemplate.findOne(query, VideoRoot.class);
//
//        videoDao.deleteSwink(root.getSwinks().get(0).getId());
//
//        final VideoRoot updatedRoot = mongoTemplate.findById(root.getId(), VideoRoot.class);
//        assertEquals(root.getSwinks().size() - 1, updatedRoot.getSwinks().size());
//        assertEquals(root.getSwinkCount() - 1, updatedRoot.getSwinkCount());
//
//    }
//
//    @Test
//    public void testDeleteSwinkNonExistent() {
//        videoDao.deleteVideoComment(new ObjectId().toString());
//    }
//
//    @Test
//    public void testRetrieveVideoRoot() {
//        final VideoRoot videoRoot = videoDao.retrieveVideoRoot("50a66ad6e4b0decb049e324e", 2, 1);
//
//        assertEquals(2, videoRoot.getComments().size());
//        assertEquals(1352768693226L, videoRoot.getComments().get(0).getCreationDate());
//        assertEquals(1384340693226L, videoRoot.getComments().get(1).getCreationDate());
//
//        assertEquals(1, videoRoot.getSwinks().size());
//        assertEquals(1384686293226L, videoRoot.getSwinks().get(0).getCreationDate());
//
//    }
//
//    @Test
//    public void testRetrieveSpotlightedVideoPreviews() {
//        final List<VideoPreview> previews = videoDao.retrieveSpotlightedVideoPreviews("50a5a5b3e4b0b112181908e0");
//        assertEquals(2, previews.size());
//        assertEquals("50a5a5b3e4b0b112181908cb", previews.get(0).getMetadata().getId());
//        assertEquals("50a5a5b3e4b0b112181908cf", previews.get(1).getMetadata().getId());
//    }
//
//    @Test
//    public void testRetrieveVideoPreviewsByAddedDate() {
//        final VideoPreviewPageResult pageResult = videoDao.retrieveVideoPreviews("50a5a5b3e4b0b112181908e0", null, 4);
//        final List<VideoPreview> previews = pageResult.getPreviews();
//        assertEquals(4, previews.size());
//        assertEquals("50a5a5b3e4b0b112181908cf", previews.get(0).getMetadata().getId());
//        assertEquals("50a5a5b3e4b0b112181908cb", previews.get(1).getMetadata().getId());
//        assertEquals("50a5a5b3e4b0b112181908d0", previews.get(2).getMetadata().getId());
//        assertEquals("50a5a5b3e4b0b112181908d1", previews.get(3).getMetadata().getId());
//    }
//
//    @Test
//    public void testRetrieveVideoPreviewsByRating() {
//        final VideoPreviewPageResult pageResult = videoDao.retrieveVideoPreviews("50a5a5b3e4b0b112181908e0", null, null, 4);
//        final List<VideoPreview> previews = pageResult.getPreviews();
//        assertEquals(4, previews.size());
//        assertEquals("50a5a5b3e4b0b112181908d1", previews.get(0).getMetadata().getId());
//        assertEquals("50a5a5b3e4b0b112181908cf", previews.get(1).getMetadata().getId());
//        assertEquals("50a5a5b3e4b0b112181908d0", previews.get(2).getMetadata().getId());
//        assertEquals("50a5a5b3e4b0b112181908cb", previews.get(3).getMetadata().getId());
//    }
//
//    @Test
//    public void testRetrieveVideoPreviewsSameRatingSubset() {
//        final VideoPreviewPageResult pageResult = videoDao.retrieveVideoPreviews("50a5a5b3e4b0b112181908e0", 0.8, 1353273283462L, 4);
//        final List<VideoPreview> previews = pageResult.getPreviews();
//        assertEquals(2, previews.size());
//        assertEquals("50a5a5b3e4b0b112181908d0", previews.get(0).getMetadata().getId());
//        assertEquals("50a5a5b3e4b0b112181908cb", previews.get(1).getMetadata().getId());
//    }
//
//    @Test
//    public void testRetrieveVideoPreviewsByRatingPagination() {
//
//        VideoPreviewPageResult pageResult = videoDao.retrieveVideoPreviews("50a5a5b3e4b0b112181908e0", null, null, 1);
//        List<VideoPreview> previews = pageResult.getPreviews();
//        assertEquals("50a5a5b3e4b0b112181908d1", previews.get(0).getMetadata().getId());
//
//        pageResult = videoDao.retrieveVideoPreviews("50a5a5b3e4b0b112181908e0", pageResult.getLastRating(), pageResult.getLastDate(), pageResult.getBatchSize());
//        previews = pageResult.getPreviews();
//        assertEquals("50a5a5b3e4b0b112181908cf", previews.get(0).getMetadata().getId());
//
//        pageResult = videoDao.retrieveVideoPreviews("50a5a5b3e4b0b112181908e0", pageResult.getLastRating(), pageResult.getLastDate(), pageResult.getBatchSize());
//        previews = pageResult.getPreviews();
//        assertEquals("50a5a5b3e4b0b112181908d0", previews.get(0).getMetadata().getId());
//
//        pageResult = videoDao.retrieveVideoPreviews("50a5a5b3e4b0b112181908e0", pageResult.getLastRating(), pageResult.getLastDate(), pageResult.getBatchSize());
//        previews = pageResult.getPreviews();
//        assertEquals("50a5a5b3e4b0b112181908cb", previews.get(0).getMetadata().getId());
//
//        pageResult = videoDao.retrieveVideoPreviews("50a5a5b3e4b0b112181908e0", pageResult.getLastRating(), pageResult.getLastDate(), pageResult.getBatchSize());
//        previews = pageResult.getPreviews();
//        assertTrue(previews.isEmpty());
//    }
//
//    @Test
//    public void testRetrieveSwinks() {
//
//        final Criteria criteria = Criteria.where("swinks").exists(true);
//        final Query query = new Query(criteria);
//        final VideoRoot root = mongoTemplate.findOne(query, VideoRoot.class);
//
//        final List<Swink> swinks = videoDao.retrieveSwinks(root.getId());
//        assertEquals(root.getSwinks().size(), swinks.size());
//        for(final Swink swink : swinks) {
//            assertNotNull(swink.getVideoRootId());
//        }
//
//    }
//
//    @Test
//    public void testRetrieveVideoComments() {
//
//        final Criteria criteria = Criteria.where("comments").exists(true);
//        final Query query = new Query(criteria);
//        final VideoRoot root = mongoTemplate.findOne(query, VideoRoot.class);
//
//        final List<VideoComment> comments = videoDao.retrieveVideoComments(root.getId());
//        assertEquals(root.getComments().size(), comments.size());
//        for(final VideoComment comment : comments) {
//            assertNotNull(comment.getVideoRootId());
//        }
//
//    }
//
//    @Test
//    public void testSpotlightVideo() {
//
//        final Criteria criteria = Criteria.where("videos").exists(true);
//        final Query query = new Query(criteria);
//        final VideoCollection videoCollection = mongoTemplate.findOne(query, VideoCollection.class);
//
//        final VideoPreview videoPreview = videoCollection.getVideos().get(0);
//        final VideoMetadata videoMetadata = videoPreview.getMetadata();
//
//        final VideoSpotlightRequest request = new VideoSpotlightRequest();
//        request.setVideoCollectionId(videoCollection.getId());
//        request.setVideoId(videoMetadata.getId());
//        request.setVideoVersion(videoMetadata.getVersion());
//        request.setSpotlighted(!videoMetadata.isSpotlighted());
//
//        videoDao.spotlightVideo(request);
//
//        final VideoMetadata dbVideoMetadata = videoDao.retrieveVideoMetadata(videoCollection.getId(), videoMetadata.getId());
//        assertEquals(request.isSpotlighted(), dbVideoMetadata.isSpotlighted());
//        assertEquals(request.getVideoVersion() + 1, dbVideoMetadata.getVersion());
//    }
//
//    @Test(expected=OptimisticLockingException.class)
//    public void testSpotlightVideoStaleVideo() {
//
//        final Criteria criteria = Criteria.where("videos").exists(true);
//        final Query query = new Query(criteria);
//        final VideoCollection videoCollection = mongoTemplate.findOne(query, VideoCollection.class);
//
//        final VideoPreview videoPreview = videoCollection.getVideos().get(0);
//        final VideoMetadata videoMetadata = videoPreview.getMetadata();
//
//        final VideoSpotlightRequest request = new VideoSpotlightRequest();
//        request.setVideoCollectionId(videoCollection.getId());
//        request.setVideoId(videoMetadata.getId());
//        request.setVideoVersion(videoMetadata.getVersion() - 1);
//        request.setSpotlighted(!videoMetadata.isSpotlighted());
//
//        videoDao.spotlightVideo(request);
//    }
//
//    @Test(expected=ObjectNotFoundException.class)
//    public void testSpotlightVideoUnknownVideo() {
//
//        final Criteria criteria = Criteria.where("videos").exists(true);
//        final Query query = new Query(criteria);
//        final VideoCollection videoCollection = mongoTemplate.findOne(query, VideoCollection.class);
//
//        final VideoPreview videoPreview = videoCollection.getVideos().get(0);
//        final VideoMetadata videoMetadata = videoPreview.getMetadata();
//
//        final VideoSpotlightRequest request = new VideoSpotlightRequest();
//        request.setVideoCollectionId(videoCollection.getId());
//        request.setVideoId("foobar");
//        request.setVideoVersion(videoMetadata.getVersion());
//        request.setSpotlighted(!videoMetadata.isSpotlighted());
//
//        videoDao.spotlightVideo(request);
//    }
//
//    @Test
//    public void testUpdateVideo() {
//
//        final Criteria criteria = Criteria.where("videos").exists(true);
//        final Query query = new Query(criteria);
//        final VideoCollection videoCollection = mongoTemplate.findOne(query, VideoCollection.class);
//
//        final VideoPreview videoPreview = videoCollection.getVideos().get(0);
//        final VideoMetadata videoMetadata = videoPreview.getMetadata();
//
//        final VideoUpdateRequest request = new VideoUpdateRequest();
//        request.setVideoCollectionId(videoCollection.getId());
//        request.setVideoId(videoMetadata.getId());
//        request.setVideoVersion(videoMetadata.getVersion());
//        request.setSpotlighted(!videoMetadata.isSpotlighted());
//        request.setTitle("new title");
//
//        videoDao.updateVideo(request);
//
//        final VideoMetadata dbVideoMetadata = videoDao.retrieveVideoMetadata(videoCollection.getId(), videoMetadata.getId());
//        assertEquals(request.isSpotlighted(), dbVideoMetadata.isSpotlighted());
//        assertEquals(request.getTitle(), dbVideoMetadata.getTitle());
//        assertEquals(request.getVideoVersion() + 1, dbVideoMetadata.getVersion());
//    }
//
//    @Test(expected=OptimisticLockingException.class)
//    public void testUpdateVideoStaleVideo() {
//
//        final Criteria criteria = Criteria.where("videos").exists(true);
//        final Query query = new Query(criteria);
//        final VideoCollection videoCollection = mongoTemplate.findOne(query, VideoCollection.class);
//
//        final VideoPreview videoPreview = videoCollection.getVideos().get(0);
//        final VideoMetadata videoMetadata = videoPreview.getMetadata();
//
//        final VideoUpdateRequest request = new VideoUpdateRequest();
//        request.setVideoCollectionId(videoCollection.getId());
//        request.setVideoId(videoMetadata.getId());
//        request.setVideoVersion(videoMetadata.getVersion() - 1);
//        request.setSpotlighted(!videoMetadata.isSpotlighted());
//        request.setTitle("new title");
//
//        videoDao.updateVideo(request);
//
//    }
//
//    @Test(expected=ObjectNotFoundException.class)
//    public void testUpdateVideoUnknownVideo() {
//
//        final Criteria criteria = Criteria.where("videos").exists(true);
//        final Query query = new Query(criteria);
//        final VideoCollection videoCollection = mongoTemplate.findOne(query, VideoCollection.class);
//
//        final VideoPreview videoPreview = videoCollection.getVideos().get(0);
//        final VideoMetadata videoMetadata = videoPreview.getMetadata();
//
//        final VideoUpdateRequest request = new VideoUpdateRequest();
//        request.setVideoCollectionId(videoCollection.getId());
//        request.setVideoId("foobar");
//        request.setVideoVersion(videoMetadata.getVersion());
//        request.setSpotlighted(!videoMetadata.isSpotlighted());
//        request.setTitle("new title");
//
//        videoDao.updateVideo(request);
//
//    }
//
//    @Test
//    public void testVoteVideoNewVote() {
//
//        final VideoRating videoRating = mongoTemplate.findOne(new Query(), VideoRating.class);
//
//        final UserProfile userProfile = mongoTemplate.findById("50a5a5b3e4b0b112181908c3", UserProfile.class);
//
//        final VideoVote vote = new VideoVote();
//        vote.setCreatedBy(new UserReference(userProfile.getId()));
//        vote.setCreationDate(new Date().getTime());
//        vote.setUpVote(true);
//        vote.setVideoId(videoRating.getVideoId());
//
//        final VideoVoteRequest request = new VideoVoteRequest();
//        request.setUserProfileVersion(userProfile.getVersion());
//        request.setVideoRatingVersion(videoRating.getVersion());
//        request.setVote(vote);
//
//        videoDao.voteVideo(request);
//
//        final VideoRating dbVideoRating = mongoTemplate.findOne(new Query(), VideoRating.class);
//
//        final UserProfile dbUserProfile = mongoTemplate.findById("50a5a5b3e4b0b112181908c3", UserProfile.class);
//
//        assertNotNull(request.getVote().getId());
//
//        assertEquals(videoRating.getUpVoteCount() + 1, dbVideoRating.getUpVoteCount());
//        assertEquals(videoRating.getDownVoteCount(), dbVideoRating.getDownVoteCount());
//        assertTrue(videoRating.getRating() < dbVideoRating.getRating());
//        assertEquals(videoRating.getVersion() + 1, dbVideoRating.getVersion());
//
//        assertEquals(userProfile.getVersion() + 1, dbUserProfile.getVersion());
//        assertEquals(userProfile.getVideoVotes().size() + 1, dbUserProfile.getVideoVotes().size());
//    }
//
//    @Test
//    public void testVoteVideoChangeVote() {
//
//        final VideoRating videoRating = mongoTemplate.findById("50a6acc3e4b0b4e1d969f3de", VideoRating.class);
//
//        final UserProfile userProfile = mongoTemplate.findById("50ad952ae4b004309b26aebc", UserProfile.class);
//
//        final VideoVote vote = new VideoVote();
//        vote.setCreatedBy(new UserReference(userProfile.getId()));
//        vote.setCreationDate(new Date().getTime());
//        vote.setUpVote(false);
//        vote.setVideoId(videoRating.getVideoId());
//
//        final VideoVoteRequest request = new VideoVoteRequest();
//        request.setUserProfileVersion(userProfile.getVersion());
//        request.setVideoRatingVersion(videoRating.getVersion());
//        request.setVote(vote);
//        request.setPreviousUpVote(true);
//
//        videoDao.voteVideo(request);
//
//        final VideoRating dbVideoRating = mongoTemplate.findById("50a6acc3e4b0b4e1d969f3de", VideoRating.class);
//
//        final UserProfile dbUserProfile = mongoTemplate.findById("50ad952ae4b004309b26aebc", UserProfile.class);
//
//        assertNotNull(request.getVote().getId());
//
//        assertEquals(videoRating.getUpVoteCount() - 1, dbVideoRating.getUpVoteCount());
//        assertEquals(videoRating.getDownVoteCount() + 1, dbVideoRating.getDownVoteCount());
//        assertTrue(videoRating.getRating() > dbVideoRating.getRating());
//        assertEquals(videoRating.getVersion() + 1, dbVideoRating.getVersion());
//
//        assertEquals(userProfile.getVersion() + 1, dbUserProfile.getVersion());
//        assertEquals(userProfile.getVideoVotes().size() + 1, dbUserProfile.getVideoVotes().size());
//    }
//
//    @Test
//    public void testVoteVideoSameVote() {
//
//        final VideoRating videoRating = mongoTemplate.findById("50a6acc3e4b0b4e1d969f3de", VideoRating.class);
//
//        final UserProfile userProfile = mongoTemplate.findById("50ad952ae4b004309b26aebc", UserProfile.class);
//
//        final VideoVote vote = new VideoVote();
//        vote.setCreatedBy(new UserReference(userProfile.getId()));
//        vote.setCreationDate(new Date().getTime());
//        vote.setUpVote(true);
//        vote.setVideoId(videoRating.getVideoId());
//
//        final VideoVoteRequest request = new VideoVoteRequest();
//        request.setUserProfileVersion(userProfile.getVersion());
//        request.setVideoRatingVersion(videoRating.getVersion());
//        request.setVote(vote);
//        request.setPreviousUpVote(true);
//
//        videoDao.voteVideo(request);
//
//        final VideoRating dbVideoRating = mongoTemplate.findById("50a6acc3e4b0b4e1d969f3de", VideoRating.class);
//
//        final UserProfile dbUserProfile = mongoTemplate.findById("50ad952ae4b004309b26aebc", UserProfile.class);
//
//        assertNotNull(request.getVote().getId());
//
//        assertEquals(videoRating.getUpVoteCount(), dbVideoRating.getUpVoteCount());
//        assertEquals(videoRating.getDownVoteCount(), dbVideoRating.getDownVoteCount());
//        assertEquals(videoRating.getVersion() + 1, dbVideoRating.getVersion());
//
//        assertEquals(userProfile.getVersion() + 1, dbUserProfile.getVersion());
//        assertEquals(userProfile.getVideoVotes().size() + 1, dbUserProfile.getVideoVotes().size());
//    }
//
//    @Test(expected=OptimisticLockingException.class)
//    public void testVoteVideoRollback() {
//
//        final VideoRating videoRating = mongoTemplate.findById("50a6acc3e4b0b4e1d969f3de", VideoRating.class);
//
//        final UserProfile userProfile = mongoTemplate.findById("50ad952ae4b004309b26aebc", UserProfile.class);
//
//        final VideoVote vote = new VideoVote();
//        vote.setCreatedBy(new UserReference(userProfile.getId()));
//        vote.setCreationDate(new Date().getTime());
//        vote.setUpVote(false);
//        vote.setVideoId(videoRating.getVideoId());
//
//        final VideoVoteRequest request = new VideoVoteRequest();
//        request.setUserProfileVersion(userProfile.getVersion());
//        request.setVideoRatingVersion(videoRating.getVersion() + 1);
//        request.setVote(vote);
//        request.setPreviousUpVote(true);
//
//        videoDao.voteVideo(request);
//
//        final VideoRating dbVideoRating = mongoTemplate.findById("50a6acc3e4b0b4e1d969f3de", VideoRating.class);
//
//        final UserProfile dbUserProfile = mongoTemplate.findById("50ad952ae4b004309b26aebc", UserProfile.class);
//
//        assertNull(request.getVote().getId());
//
//        assertEquals(videoRating.getUpVoteCount(), dbVideoRating.getUpVoteCount());
//        assertEquals(videoRating.getDownVoteCount(), dbVideoRating.getDownVoteCount());
//        assertEquals(videoRating.getVersion(), dbVideoRating.getVersion());
//
//        assertEquals(userProfile.getVersion() + 1, dbUserProfile.getVersion());
//        assertEquals(userProfile.getVideoVotes().size(), dbUserProfile.getVideoVotes().size());
//    }
}

