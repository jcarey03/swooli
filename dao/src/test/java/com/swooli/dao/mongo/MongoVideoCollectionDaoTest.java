package com.swooli.dao.mongo;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration("/dao-context.xml")
@Ignore
public class MongoVideoCollectionDaoTest extends TestBase {

    @Autowired
    MongoVideoCollectionDao videoCollectionRepository;

//    @Test
//    public void testRetrieveVideoCollectionPreviewsByLastPopDate() {
//
//        final VideoCollectionPreviewPageResult result = videoCollectionRepository.retrieveVideoCollectionPreviews(VideoCollectionCategory.EVERYTHING, null, null);
//        final List<VideoCollectionPreview> previews = result.getPreviews();
//
//        assertEquals(4, previews.size());
//        assertEquals("50a68ac3e4b0a4f0294057fc", previews.get(0).getMetadata().getId());
//        assertEquals("50a68ac3e4b0a4f0294057fc", previews.get(1).getMetadata().getId());
//        assertEquals("50a5a5b3e4b0b112181908d9", previews.get(2).getMetadata().getId());
//        assertEquals("50a5a5b3e4b0b112181908d9", previews.get(3).getMetadata().getId());
//    }
//
//    @Test
//    public void testRetrieveVideoCollectionPreviewsByLastPopDatePagination() {
//
//        VideoCollectionPreviewPageResult result = videoCollectionRepository.retrieveVideoCollectionPreviews(VideoCollectionCategory.EVERYTHING, null, 1);
//        List<VideoCollectionPreview> previews = result.getPreviews();
//
//        assertEquals("50a68ac3e4b0a4f0294057fc", previews.get(0).getMetadata().getId());
//
//        result = videoCollectionRepository.retrieveVideoCollectionPreviews(result.getCategory(), result.getLastDate(), result.getBatchSize());
//        previews = result.getPreviews();
//        assertEquals("50a68ac3e4b0a4f0294057fc", previews.get(0).getMetadata().getId());
//
//        result = videoCollectionRepository.retrieveVideoCollectionPreviews(result.getCategory(), result.getLastDate(), result.getBatchSize());
//        previews = result.getPreviews();
//        assertEquals("50a5a5b3e4b0b112181908d9", previews.get(0).getMetadata().getId());
//
//        result = videoCollectionRepository.retrieveVideoCollectionPreviews(result.getCategory(), result.getLastDate(), result.getBatchSize());
//        previews = result.getPreviews();
//        assertEquals("50a5a5b3e4b0b112181908d9", previews.get(0).getMetadata().getId());
//
//        result = videoCollectionRepository.retrieveVideoCollectionPreviews(result.getCategory(), result.getLastDate(), result.getBatchSize());
//        assertTrue(result.getPreviews().isEmpty());
//    }
//
//    @Test
//    public void testRetrieveVideoCollectionPreviews() {
//        final Collection<VideoCollectionPreview> previews  = videoCollectionRepository.retrieveVideoCollectionPreviews(Arrays.asList("50a68ac3e4b0a4f0294057fc", "50a5a5b3e4b0b112181908d9"));
//        assertEquals(2, previews.size());
//    }
//
//    @Test
//    public void testCreateVideoCollection() {
//
//        final VideoCollection videoCollection = mongoTemplate.findOne(new Query(), VideoCollection.class);
//        final VideoCollectionMetadata metadata = videoCollection.getMetadata();
//        metadata.setId(null);
//
//        videoCollectionRepository.createVideoCollection(metadata);
//
//        assertNotNull(metadata.getId());
//        assertNotNull(mongoTemplate.findById(metadata.getId(), VideoCollection.class));
//
//    }
//
//    @Test
//    public void testDeleteVideoCollection() {
//
//        final VideoCollection videoCollection = mongoTemplate.findById("50a68ac3e4b0a4f0294057fc", VideoCollection.class);
//
//        videoCollectionRepository.deleteVideoCollection(videoCollection.getId());
//
//        assertNull(mongoTemplate.findById(videoCollection.getId(), VideoCollection.class));
//
//        // get video IDs
//        final Collection<String> videoIds = new ArrayList<>();
//        for(final VideoPreview videoPreview : videoCollection.getVideos()) {
//            videoIds.add(videoPreview.getMetadata().getId());
//        }
//
//        // check video ratings
//        final Criteria videoRatingsCriteria = Criteria.where("videoCollectionId").is(videoCollection.getId());
//        final Query videoRatingsQuery = new Query(videoRatingsCriteria);
//        assertTrue(mongoTemplate.find(videoRatingsQuery, VideoRating.class).isEmpty());
//
//        // check pops
//        final Criteria popsCriteria = Criteria.where("videoCollectionId").is(videoCollection.getId());
//        final Query popsQuery = new Query(popsCriteria);
//        assertTrue(mongoTemplate.find(popsQuery, VideoCollectionPop.class).isEmpty());
//
//        // check video votes
//        final Criteria videoVotesCriteria = Criteria.where("videoId").in(videoIds);
//        final Query videoVotesQuery = new Query(videoVotesCriteria);
//        assertTrue(mongoTemplate.find(videoVotesQuery, VideoVote.class).isEmpty());
//
//    }
//
//    @Test
//    public void testPopVideoCollection() {
//
//        final VideoCollection videoCollection = mongoTemplate.findOne(new Query(), VideoCollection.class);
//        final UserProfile userProfile = mongoTemplate.findOne(new Query(), UserProfile.class);
//
//        final VideoCollectionPopRequest request = new VideoCollectionPopRequest();
//        request.setVideoCollectionVersion(videoCollection.getMetadata().getVersion());
//        request.setUserProfileVersion(userProfile.getVersion());
//        final VideoCollectionPop pop = new VideoCollectionPop();
//        pop.setCategory(VideoCollectionCategory.COOKING);
//        pop.setCreatedBy(new UserReference(userProfile.getId()));
//        pop.setCreationDate(new Date().getTime());
//        pop.setVideoCollectionId(videoCollection.getId());
//        request.setPop(pop);
//
//        videoCollectionRepository.popVideoCollection(request);
//
//        final VideoCollection dbVideoCollection = mongoTemplate.findById(videoCollection.getId(), VideoCollection.class);
//        final UserProfile dbUserProfile = mongoTemplate.findById(userProfile.getId(), UserProfile.class);
//        final VideoCollectionPop dbPop = mongoTemplate.findById(pop.getId(), VideoCollectionPop.class);
//
//        assertEquals(videoCollection.getPopCount() + 1, dbVideoCollection.getPopCount());
//        assertEquals(videoCollection.getMetadata().getVersion() + 1, dbVideoCollection.getMetadata().getVersion());
//        assertEquals(userProfile.getVideoCollectionPops().size() + 1, dbUserProfile.getVideoCollectionPops().size());
//        assertEquals(userProfile.getVersion() + 1, dbUserProfile.getVersion());
//        assertNotNull(dbPop);
//
//    }
//
//    @Test
//    public void testPopVideoCollectionStaleUser() {
//
//        final VideoCollection videoCollection = mongoTemplate.findOne(new Query(), VideoCollection.class);
//        final UserProfile userProfile = mongoTemplate.findOne(new Query(), UserProfile.class);
//
//        final VideoCollectionPopRequest request = new VideoCollectionPopRequest();
//        request.setVideoCollectionVersion(videoCollection.getMetadata().getVersion());
//        request.setUserProfileVersion(userProfile.getVersion() - 1);
//        final VideoCollectionPop pop = new VideoCollectionPop();
//        pop.setCategory(VideoCollectionCategory.COOKING);
//        pop.setCreatedBy(new UserReference(userProfile.getId()));
//        pop.setCreationDate(new Date().getTime());
//        pop.setVideoCollectionId(videoCollection.getId());
//        request.setPop(pop);
//
//        try {
//            videoCollectionRepository.popVideoCollection(request);
//            fail("Failed to throw OptimisticLockingException");
//        } catch(final OptimisticLockingException ole) {}
//
//        final VideoCollection dbVideoCollection = mongoTemplate.findById(videoCollection.getId(), VideoCollection.class);
//        final UserProfile dbUserProfile = mongoTemplate.findById(userProfile.getId(), UserProfile.class);
//        final VideoCollectionPop dbPop = mongoTemplate.findById(pop.getId(), VideoCollectionPop.class);
//
//        assertEquals(videoCollection.getPopCount(), dbVideoCollection.getPopCount());
//        assertEquals(videoCollection.getMetadata().getVersion(), dbVideoCollection.getMetadata().getVersion());
//        assertEquals(userProfile.getVideoCollectionPops().size(), dbUserProfile.getVideoCollectionPops().size());
//        assertEquals(userProfile.getVersion(), dbUserProfile.getVersion());
//        assertNull(dbPop);
//
//    }
//
//    @Test
//    public void testPopVideoCollectionUnknownUser() {
//
//        final VideoCollection videoCollection = mongoTemplate.findOne(new Query(), VideoCollection.class);
//        final UserProfile userProfile = mongoTemplate.findOne(new Query(), UserProfile.class);
//
//        final VideoCollectionPopRequest request = new VideoCollectionPopRequest();
//        request.setVideoCollectionVersion(videoCollection.getMetadata().getVersion());
//        request.setUserProfileVersion(userProfile.getVersion());
//        final VideoCollectionPop pop = new VideoCollectionPop();
//        pop.setCategory(VideoCollectionCategory.COOKING);
//        pop.setCreatedBy(new UserReference("foobar"));
//        pop.setCreationDate(new Date().getTime());
//        pop.setVideoCollectionId(videoCollection.getId());
//        request.setPop(pop);
//
//        try {
//            videoCollectionRepository.popVideoCollection(request);
//            fail("Failed to throw ObjectNotFoundException");
//        } catch(final ObjectNotFoundException onfe) {}
//
//        final VideoCollection dbVideoCollection = mongoTemplate.findById(videoCollection.getId(), VideoCollection.class);
//        final UserProfile dbUserProfile = mongoTemplate.findById(userProfile.getId(), UserProfile.class);
//        final VideoCollectionPop dbPop = mongoTemplate.findById(pop.getId(), VideoCollectionPop.class);
//
//        assertEquals(videoCollection.getPopCount(), dbVideoCollection.getPopCount());
//        assertEquals(videoCollection.getMetadata().getVersion(), dbVideoCollection.getMetadata().getVersion());
//        assertEquals(userProfile.getVideoCollectionPops().size(), dbUserProfile.getVideoCollectionPops().size());
//        assertEquals(userProfile.getVersion(), dbUserProfile.getVersion());
//        assertNull(dbPop);
//
//    }
//
//    @Test
//    public void testPopVideoCollectionStaleVideoCollection() {
//
//        final VideoCollection videoCollection = mongoTemplate.findOne(new Query(), VideoCollection.class);
//        final UserProfile userProfile = mongoTemplate.findOne(new Query(), UserProfile.class);
//
//        final VideoCollectionPopRequest request = new VideoCollectionPopRequest();
//        request.setVideoCollectionVersion(videoCollection.getMetadata().getVersion() - 1);
//        request.setUserProfileVersion(userProfile.getVersion());
//        final VideoCollectionPop pop = new VideoCollectionPop();
//        pop.setCategory(VideoCollectionCategory.COOKING);
//        pop.setCreatedBy(new UserReference(userProfile.getId()));
//        pop.setCreationDate(new Date().getTime());
//        pop.setVideoCollectionId(videoCollection.getId());
//        request.setPop(pop);
//
//        try {
//            videoCollectionRepository.popVideoCollection(request);
//            fail("Failed to throw OptimisticLockingException");
//        } catch(final OptimisticLockingException ole) {}
//
//        final VideoCollection dbVideoCollection = mongoTemplate.findById(videoCollection.getId(), VideoCollection.class);
//        final UserProfile dbUserProfile = mongoTemplate.findById(userProfile.getId(), UserProfile.class);
//        final VideoCollectionPop dbPop = mongoTemplate.findById(pop.getId(), VideoCollectionPop.class);
//
//        assertEquals(videoCollection.getPopCount(), dbVideoCollection.getPopCount());
//        assertEquals(videoCollection.getMetadata().getVersion(), dbVideoCollection.getMetadata().getVersion());
//        assertEquals(userProfile.getVideoCollectionPops().size(), dbUserProfile.getVideoCollectionPops().size());
//        assertEquals(userProfile.getVersion(), dbUserProfile.getVersion());
//        assertNull(dbPop);
//
//    }
//
//    @Test
//    public void testPopVideoCollectionUnknownVideoCollection() {
//
//        final VideoCollection videoCollection = mongoTemplate.findOne(new Query(), VideoCollection.class);
//        final UserProfile userProfile = mongoTemplate.findOne(new Query(), UserProfile.class);
//
//        final VideoCollectionPopRequest request = new VideoCollectionPopRequest();
//        request.setVideoCollectionVersion(videoCollection.getMetadata().getVersion());
//        request.setUserProfileVersion(userProfile.getVersion());
//        final VideoCollectionPop pop = new VideoCollectionPop();
//        pop.setCategory(VideoCollectionCategory.COOKING);
//        pop.setCreatedBy(new UserReference(userProfile.getId()));
//        pop.setCreationDate(new Date().getTime());
//        pop.setVideoCollectionId("foobar");
//        request.setPop(pop);
//
//        try {
//            videoCollectionRepository.popVideoCollection(request);
//            fail("Failed to throw ObjectNotFoundException");
//        } catch(final ObjectNotFoundException onfe) {}
//
//        final VideoCollection dbVideoCollection = mongoTemplate.findById(videoCollection.getId(), VideoCollection.class);
//        final UserProfile dbUserProfile = mongoTemplate.findById(userProfile.getId(), UserProfile.class);
//        final VideoCollectionPop dbPop = mongoTemplate.findById(pop.getId(), VideoCollectionPop.class);
//
//        assertEquals(videoCollection.getPopCount(), dbVideoCollection.getPopCount());
//        assertEquals(videoCollection.getMetadata().getVersion(), dbVideoCollection.getMetadata().getVersion());
//        assertEquals(userProfile.getVideoCollectionPops().size(), dbUserProfile.getVideoCollectionPops().size());
//        assertEquals(userProfile.getVersion(), dbUserProfile.getVersion());
//        assertNull(dbPop);
//
//    }

}

