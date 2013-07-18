db.dropDatabase();

/// Indexes ///

db.UserProfile.ensureIndex( { "email": 1 }, { unique: true } )

db.UserProfile.ensureIndex( { "username": 1 }, { unique: true } )


/// User Profiles ///

userProfile1 = {
    "_id" : ObjectId("50b7abdee4b0befbd29a62b5"),
    "_class" : "com.swooli.bo.user.UserProfile",
    "version" : NumberLong(0),
    "username" : "user 1",
    "photoUri" : {
        "string" : "http://some/photo/uri"
    },
    "password" : "some password",
    "email" : "user1@email.com",
    "country" : "USA",
    "zipCode" : "12345",
    "birthday" : NumberLong("1354214366406"),
    "accountStatus" : "DISABLED",
    "creationDate" : NumberLong("1354214366407"),
    "securityQuestions" : [    {
        "question" : "What's your first name?",
        "answer" : "FU"
    },      {
        "question" : "What's your last name?",
        "answer" : "BU"
    } ]
};
db.UserProfile.insert(userProfile1);

userProfile2 = {
    "_id" : ObjectId("50a5a5b3e4b0b112181908dd"),
    "_class" : "com.swooli.bo.user.UserProfile",
    "version" : NumberLong(0),
    "username" : "user 2",
    "photoUri" : {
        "string" : "http://some/photo/uri"
    },
    "password" : "a4f2455a85c9d5c6323049e289ae7708",
    "email" : "user2@email.com",
    "country" : "USA",
    "zipCode" : "12345",
    "birthday" : NumberLong("1354214366406"),
    "accountStatus" : "PENDING",
    "creationDate" : NumberLong("1354214366407"),
    "securityQuestions" : [    {
        "question" : "What's your first name?",
        "answer" : "FU"
    },      {
        "question" : "What's your last name?",
        "answer" : "BU"
    } ]
};
db.UserProfile.insert(userProfile2);

userProfile3 = {
    "_id" : ObjectId("50a5a5b3e4b0b112181908de"),
    "_class" : "com.swooli.bo.user.UserProfile",
    "version" : NumberLong(0),
    "username" : "user 3",
    "photoUri" : {
        "string" : "http://some/photo/uri"
    },
    "password" : "some password",
    "email" : "user3@email.com",
    "country" : "USA",
    "zipCode" : "12345",
    "birthday" : NumberLong("1354214366406"),
    "accountStatus" : "ACTIVE",
    "creationDate" : NumberLong("1354214366407"),
    "lastLoginDate" : NumberLong("1354214366407"),
    "securityQuestions" : [    {
        "question" : "What's your first name?",
        "answer" : "FU"
    },      {
        "question" : "What's your last name?",
        "answer" : "BU"
    } ],
    "videoCollectionPops" : [  {
        "_id" : "50a5a5b3e4b0b112181908df",
        "videoCollectionId" : "50a5a5b3e4b0b112181908e0",
        "category" : "COOKING",
        "creationDate" : NumberLong("1354214366408")
    },
    {
        "_id" : "50a5a5b3e4b0b112181908e3",
        "videoCollectionId" : "50a5a5b3e4b0b112181908e0",
        "category" : "COOKING",
        "creationDate" : NumberLong("1354214360000")
    },
    {
        "_id" : "50a5a5b3e4b0b112181908e4",
        "videoCollectionId" : "50a5a5b3e4b0b112181908e8",
        "category" : "EVERYTHING",
        "creationDate" : NumberLong("1354214366418")
    }],
    "videoVotes" : [         {
        "_id" : "50a5a5b3e4b0b112181908e1",
        "videoId" : "50a5a5b3e4b0b112181908e2",
        "upVote" : true,
        "creationDate" : NumberLong("1354214366408")
    },
    {
        "_id" : "50a5a5b3e4b0b112181908e6",
        "videoId" : "50a5a5b3e4b0b112181908e7",
        "upVote" : false,
        "creationDate" : NumberLong("1354214366418")
    }],
    "createdVideoCollectionIds" : [ "50a5a5b3e4b0b112181908e0" ],
    "memberOfVideoCollectionIds" : [ "50a5a5b3e4b0b112181908e8" ]
};
db.UserProfile.insert(userProfile3);

userProfile4 = {
    "_id" : ObjectId("50a5a5b3e4b0b112181908e9"),
    "_class" : "com.swooli.bo.user.UserProfile",
    "version" : NumberLong(0),
    "username" : "user 4",
    "photoUri" : {
        "string" : "http://some/photo/uri"
    },
    "password" : "some password",
    "email" : "user4@email.com",
    "country" : "USA",
    "zipCode" : "12345",
    "birthday" : NumberLong("1354214366406"),
    "accountStatus" : "ACTIVE",
    "creationDate" : NumberLong("1354214366407"),
    "lastLoginDate" : NumberLong("1354214366407"),
    "securityQuestions" : [    {
        "question" : "What's your first name?",
        "answer" : "FU"
    },      {
        "question" : "What's your last name?",
        "answer" : "BU"
    } ],
    "createdVideoCollectionIds" : [ "50a5a5b3e4b0b112181908e8" ]
};
db.UserProfile.insert(userProfile4);

videoRoot1 =  {
    "_id" : ObjectId("50b7b59de4b00cf58c440690"),
    "_class" : "com.swooli.bo.video.VideoRoot",
    "metadata" : {
        "version" : NumberLong(0),
        "videoIdentifier" : {
            "_class" : "com.swooli.bo.repository.YouTubeVideoIdentifier",
            "thirdPartyId" : "videoId",
            "originUri" : {
                "string" : "http://video/origin/uri"
            },
            "thumbnailUri" : {
                "string" : "http://video/thumbnail/uri"
            },
            "videoUri" : {
                "string" : "http://video/uri"
            }
        },
        "importedBy" : {
            "userProfileId" : userProfile3._id.valueOf(),
            "username" : userProfile3.username,
            "photoUri" : userProfile3.photoUri
        },
        "importDate" : NumberLong("1354216861569"),
        "title" : "video root 1"
    },
    "swinks" : [        {
        "_id" : "50a5a5b3e4b0b112181908ea",
        "createdBy" : {
            "userProfileId" : userProfile3._id.valueOf(),
            "username" : userProfile3.username,
            "photoUri" : userProfile3.photoUri
        },
        "description" : {
            "title" : "swink title",
            "description" : "swink description",
            "imageUri" : {
                "string" : "http://image/uri"
            },
            "originUri" : {
                "string" : "http://origin/uri"
            }
        },
        "creationDate" : NumberLong("1354216861569")
    } ],
    "swinkCount" : 1,
    "comments" : [    {
        "_id" : "50a5a5b3e4b0b112181908eb",
        "createdBy" : {
            "userProfileId" : userProfile3._id.valueOf(),
            "username" : userProfile3.username,
            "photoUri" : userProfile3.photoUri
        },
        "creationDate" : NumberLong("1354216861569"),
        "comment" : "comment"
    } ],
    "commentCount" : 1,
    "popCount" : 1
};
db.VideoRoot.insert(videoRoot1);

videoRoot2 =  {
    "_id" : ObjectId("50a5a5b3e4b0b112181908ec"),
    "_class" : "com.swooli.bo.video.VideoRoot",
    "metadata" : {
        "version" : NumberLong(0),
        "videoIdentifier" : {
            "_class" : "com.swooli.bo.repository.YouTubeVideoIdentifier",
            "thirdPartyId" : "videoId",
            "originUri" : {
                "string" : "http://video/origin/uri"
            },
            "thumbnailUri" : {
                "string" : "http://video/thumbnail/uri"
            },
            "videoUri" : {
                "string" : "http://video/uri"
            }
        },
        "importedBy" : {
            "userProfileId" : userProfile3._id.valueOf(),
            "username" : userProfile3.username,
            "photoUri" : userProfile3.photoUri
        },
        "importDate" : NumberLong("1354216861569"),
        "title" : "video root 2"
    },
    "swinkCount" : 0,
    "commentCount" : 0,
    "popCount" : 1
};
db.VideoRoot.insert(videoRoot2);

videoRoot3 =  {
    "_id" : ObjectId("50a5a5b3e4b0b112181908ed"),
    "_class" : "com.swooli.bo.video.VideoRoot",
    "metadata" : {
        "version" : NumberLong(0),
        "videoIdentifier" : {
            "_class" : "com.swooli.bo.repository.YouTubeVideoIdentifier",
            "thirdPartyId" : "videoId",
            "originUri" : {
                "string" : "http://video/origin/uri"
            },
            "thumbnailUri" : {
                "string" : "http://video/thumbnail/uri"
            },
            "videoUri" : {
                "string" : "http://video/uri"
            }
        },
        "importedBy" : {
            "userProfileId" : userProfile4._id.valueOf(),
            "username" : userProfile4.username,
            "photoUri" : userProfile4.photoUri
        },
        "importDate" : NumberLong("1354216861569"),
        "title" : "video root 3"
    },
    "swinkCount" : 0,
    "commentCount" : 0,
    "popCount" : 0
};
db.VideoRoot.insert(videoRoot3);

videoCollection1 = {
    "_id" : ObjectId("50a5a5b3e4b0b112181908e0"),
    "_class" : "com.swooli.bo.video.collection.VideoCollection",
    "metadata" : {
        "version" : NumberLong(0),
        "createdBy" : {
            "userProfileId" : userProfile3._id.valueOf(),
            "username" : userProfile3.username,
            "photoUri" : userProfile3.photoUri
        },
        "creationDate" : NumberLong("1354215678334"),
        "description" : {
            "title" : " title",
            "description" : " description",
            "videoIdentifier" : {
                "_class" : "com.swooli.bo.repository.YouTubeVideoIdentifier",
                "thirdPartyId" : "videoId",
                "originUri" : {
                    "string" : "http://video/origin/uri"
                },
                "thumbnailUri" : {
                    "string" : "http://video/thumbnail/uri"
                },
                "videoUri" : {
                    "string" : "http://video/uri"
                }
            }
        },
        "visible" : true,
        "open" : true,
        "category" : "COOKING"
    },
    "members" : [ ],
    "videos" : [    {
        "metadata" : {
            "_id" : "50a5a5b3e4b0b112181908e2",
            "version" : NumberLong(0),
            "title" : "title",
            "thumbnailUri" : {
                "string" : "http://foo"
            },
            "videoRootId" : "50b7b59de4b00cf58c440690",
            "addedBy" : {
                "userProfileId" : userProfile3._id.valueOf(),
                "username" : userProfile3.username,
                "photoUri" : userProfile3.photoUri
            },
            "addedDate" : NumberLong("1354215678338"),
            "spotlighted" : true,
            "spotlightedDate" : NumberLong("1354215678338")
        }
    },
    {
        "metadata" : {
            "_id" : "50a5a5b3e4b0b112181908e7",
            "version" : NumberLong(0),
            "title" : "title",
            "thumbnailUri" : {
                "string" : "http://foo"
            },
            "videoRootId" : "50a5a5b3e4b0b112181908ec",
            "addedBy" : {
                "userProfileId" : userProfile3._id.valueOf(),
                "username" : userProfile3.username,
                "photoUri" : userProfile3.photoUri
            },
            "addedDate" : NumberLong("1354215678338"),
            "spotlighted" : true,
            "spotlightedDate" : NumberLong("1354215678338")
        }
    },
    {
        "metadata" : {
            "_id" : "50a5a5b3e4b0b112181908ef",
            "version" : NumberLong(0),
            "title" : "title",
            "thumbnailUri" : {
                "string" : "http://foo"
            },
            "videoRootId" : "50a5a5b3e4b0b112181908ed",
            "addedBy" : {
                "userProfileId" : userProfile4._id.valueOf(),
                "username" : userProfile4.username,
                "photoUri" : userProfile4.photoUri
            },
            "addedDate" : NumberLong("1354215678338")
        }
    }],
    "videoCount" : 3,
    "popCount" : 2
};
db.VideoCollection.insert(videoCollection1);

videoCollection2 = {
    "_id" : ObjectId("50a5a5b3e4b0b112181908e8"),
    "_class" : "com.swooli.bo.video.collection.VideoCollection",
    "metadata" : {
        "version" : NumberLong(0),
        "createdBy" : {
            "userProfileId" : userProfile4._id.valueOf(),
            "username" : userProfile4.username,
            "photoUri" : userProfile4.photoUri
        },
        "creationDate" : NumberLong("1354215678334"),
        "description" : {
            "title" : " title",
            "description" : " description",
            "videoIdentifier" : {
                "_class" : "com.swooli.bo.repository.YouTubeVideoIdentifier",
                "thirdPartyId" : "videoId",
                "originUri" : {
                    "string" : "http://video/origin/uri"
                },
                "thumbnailUri" : {
                    "string" : "http://video/thumbnail/uri"
                },
                "videoUri" : {
                    "string" : "http://video/uri"
                }
            }
        },
        "visible" : true,
        "open" : false
    },
    "members" : [ {
        "userProfileId" : userProfile3._id.valueOf(),
        "username" : userProfile3.username,
        "photoUri" : userProfile3.photoUri
    }],
    "videos" : [    {
        "metadata" : {
            "_id" : "50a5a5b3e4b0b112181908f0",
            "version" : NumberLong(0),
            "title" : "title",
            "thumbnailUri" : {
                "string" : "http://foo"
            },
            "videoRootId" : "50b7b59de4b00cf58c440690",
            "addedBy" : {
                "userProfileId" : userProfile3._id.valueOf(),
                "username" : userProfile3.username,
                "photoUri" : userProfile3.photoUri
            },
            "addedDate" : NumberLong("1354215678338")
        }
    },
    {
        "metadata" : {
            "_id" : "50a5a5b3e4b0b112181908f1",
            "version" : NumberLong(0),
            "title" : "title",
            "thumbnailUri" : {
                "string" : "http://foo"
            },
            "videoRootId" : "50a5a5b3e4b0b112181908ec",
            "addedBy" : {
                "userProfileId" : userProfile3._id.valueOf(),
                "username" : userProfile3.username,
                "photoUri" : userProfile3.photoUri
            },
            "addedDate" : NumberLong("1354215678338")
        }
    }],
    "videoCount" : 2,
    "popCount" : 0,
    "category" : "EVERYTHING"
};
db.VideoCollection.insert(videoCollection2);


videoRating1 = {
    "_id" : ObjectId("50a5a5b3e4b0b112181908f2"),
    "_class" : "com.swooli.bo.video.VideoRating",
    "videoCollectionId" : "50a5a5b3e4b0b112181908e0",
    "videoId" : "50a5a5b3e4b0b112181908e2",
    "version" : NumberLong(0),
    "upVoteCount" : 3,
    "downVoteCount" : 3,
    "rating" : 1,
    "creationDate" : NumberLong("1354216861568")
};
db.VideoRating.insert(videoRating1);

videoRating2 = {
    "_id" : ObjectId("50a5a5b3e4b0b112181908f3"),
    "_class" : "com.swooli.bo.video.VideoRating",
    "videoCollectionId" : "50a5a5b3e4b0b112181908e0",
    "videoId" : "50a5a5b3e4b0b112181908e7",
    "version" : NumberLong(0),
    "upVoteCount" : 1,
    "downVoteCount" : 1,
    "rating" : 1,
    "creationDate" : NumberLong("1354216861568")
};
db.VideoRating.insert(videoRating2);

videoRating3 = {
    "_id" : ObjectId("50a5a5b3e4b0b112181908f4"),
    "_class" : "com.swooli.bo.video.VideoRating",
    "videoCollectionId" : "50a5a5b3e4b0b112181908e0",
    "videoId" : "50a5a5b3e4b0b112181908ef",
    "version" : NumberLong(0),
    "upVoteCount" : 3,
    "downVoteCount" : 3,
    "rating" : 1,
    "creationDate" : NumberLong("1354216861568")
};
db.VideoRating.insert(videoRating3);

videoRating4 = {
    "_id" : ObjectId("50a5a5b3e4b0b112181908f5"),
    "_class" : "com.swooli.bo.video.VideoRating",
    "videoCollectionId" : "50a5a5b3e4b0b112181908e8",
    "videoId" : "50a5a5b3e4b0b112181908f0",
    "version" : NumberLong(0),
    "upVoteCount" : 1,
    "downVoteCount" : 1,
    "rating" : 1,
    "creationDate" : NumberLong("1354216861568")
};
db.VideoRating.insert(videoRating4);

videoRating5 = {
    "_id" : ObjectId("50a5a5b3e4b0b112181908f6"),
    "_class" : "com.swooli.bo.video.VideoRating",
    "videoCollectionId" : "50a5a5b3e4b0b112181908e8",
    "videoId" : "50a5a5b3e4b0b112181908f1",
    "version" : NumberLong(0),
    "upVoteCount" : 1,
    "downVoteCount" : 1,
    "rating" : 1,
    "creationDate" : NumberLong("1354216861568")
};
db.VideoRating.insert(videoRating5);

videoPop1 = {
    "_id" : "50a5a5b3e4b0b112181908f4",
    "_class" : "com.swooli.bo.video.VideoPop",
    "fromVideoCollectionId" : "50a5a5b3e4b0b112181908e0",
    "toVideoCollectionId" : "50a5a5b3e4b0b112181908e8",
    "fromVideoId" : "50a5a5b3e4b0b112181908e2",
    "toVideoId" : "50a5a5b3e4b0b112181908f0",
    "createdBy" : {
        "userProfileId" : userProfile3._id.valueOf(),
        "username" : userProfile3.username,
        "photoUri" : userProfile3.photoUri
    },
    "creationDate" : NumberLong("1354217014292")
};
db.VideoPop.insert(videoPop1);

videoPop2 = {
    "_id" : "50a5a5b3e4b0b112181908f5",
    "_class" : "com.swooli.bo.video.VideoPop",
    "fromVideoCollectionId" : "50a5a5b3e4b0b112181908e0",
    "toVideoCollectionId" : "50a5a5b3e4b0b112181908e8",
    "fromVideoId" : "50a5a5b3e4b0b112181908e7",
    "toVideoId" : "50a5a5b3e4b0b112181908f1",
    "createdBy" : {
        "userProfileId" : userProfile3._id.valueOf(),
        "username" : userProfile3.username,
        "photoUri" : userProfile3.photoUri
    },
    "creationDate" : NumberLong("1354217014292")
};
db.VideoPop.insert(videoPop2);

videoVote1 = {
    "_id" : "50a5a5b3e4b0b112181908e1",
    "videoId" : "50a5a5b3e4b0b112181908e2",
    "upVote" : true,
    "createdBy" : {
        "userProfileId" : userProfile3._id.valueOf(),
        "username" : userProfile3.username,
        "photoUri" : userProfile3.photoUri
    },
    "creationDate" : NumberLong("1354217014292")
};
db.VideoVote.insert(videoVote1);

videoVote2 = {
    "_id" : "50a5a5b3e4b0b112181908e6",
    "videoId" : "50a5a5b3e4b0b112181908e7",
    "upVote" : false,
    "createdBy" : {
        "userProfileId" : userProfile3._id.valueOf(),
        "username" : userProfile3.username,
        "photoUri" : userProfile3.photoUri
    },
    "creationDate" : NumberLong("1354217014292")
};
db.VideoVote.insert(videoVote2);

videoCollectionPop1 = {
    "_id" : "50a5a5b3e4b0b112181908df",
    "videoCollectionId" : "50a5a5b3e4b0b112181908e0",
    "category" : "COOKING",
    "creationDate" : NumberLong("1354214366408")
};
db.VideoCollectionPop.insert(videoCollectionPop1);

videoCollectionPop2 = {
    "_id" : "50a5a5b3e4b0b112181908e3",
    "videoCollectionId" : "50a5a5b3e4b0b112181908e0",
    "category" : "COOKING",
    "creationDate" : NumberLong("1354214360000"),
    "createdBy" : {
        "userProfileId" : userProfile3._id.valueOf(),
        "username" : userProfile3.username,
        "photoUri" : userProfile3.photoUri
    }
};
db.VideoCollectionPop.insert(videoCollectionPop2);

videoCollectionPop3 = {
    "_id" : "50a5a5b3e4b0b112181908e4",
    "videoCollectionId" : "50a5a5b3e4b0b112181908e8",
    "category" : "EVERYTHING",
    "creationDate" : NumberLong("1354214366418"),
    "createdBy" : {
        "userProfileId" : userProfile3._id.valueOf(),
        "username" : userProfile3.username,
        "photoUri" : userProfile3.photoUri
    }
};
db.VideoCollectionPop.insert(videoCollectionPop3);


//
//
////{
////"_id" : ObjectId("50ad952ae4b004309b26aebc"),
////"_class" : "com.swooli.bo.user.UserProfile",
////"version" : NumberLong(0),
////"username" : "user1",
////"photoUri" : {
////    "string" : "http://user1/photo"
////},
////"password" : "some password",
////"email" : "user1@gmail.com",
////"country" : "USA",
////"zipCode" : "12345",
////"birthday" : NumberLong(1353033241026),
////"accountStatus" : "DISABLED",
////"creationDate" : NumberLong(1353033240027),
////"lastLoginDate" : NumberLong(1353033240027),
////"securityQuestions" : [        {
////        "question" : "What street did you live on in 3rd grade?",
////        "answer" : "123 Mulberry Lane"
////    },       {
////        "question" : "Mother's maiden name?",
////        "answer" : "Bertha"
////    } ],
////"pops" : [        {
////        "_id" : "50a5a5b3e4b0b112181908d5",
////        "videoCollectionId" : "50a68ac3e4b0a4f0294057fc",
////        "category" : "EVERYTHING",
////        "creationDate" : NumberLong(1353553194570)
////    } ],
////"votes" : [  {
////        "_id" : "50a5a5b3e4b0b112181908d6",
////        "videoId" : "50a5a5b3e4b0b112181908cb",
////        "upVote" : true,
////        "creationDate" : NumberLong(1353553194570)
////    } ]
////};
//db.UserProfile.insert(userProfile1);
//
//userProfile2 = {
//    "_id" : ObjectId("50a5a5b3e4b0b112181908c3"),
//    "_class" : "com.swooli.bo.user.UserProfile",
//    "version" : 0,
//    "username" : "user2",
//    "photoUri" : {
//        "string" : "http://user2/photo"
//    },
//    "password" : "some password",
//    "email" : "user2@gmail.com",
//    "country" : "USA",
//    "zipCode" : "12345",
//    "birthday" : NumberLong(1353033241026),
//    "accountStatus" : "DISABLED",
//    "creationDate" : NumberLong(1353033240027),
//    "securityQuestions" : [
//    {
//        "question" : "What street did you live on in 3rd grade?",
//        "answer" : "123 Mulberry Lane"
//    },
//    {
//        "question" : "Mother's maiden name?",
//        "answer" : "Bertha"
//    }
//    ]
//};
//db.UserProfile.insert(userProfile2);
//
//userProfile3 = {
//    "_id" : ObjectId("50a5a5b3e4b0b112181908c4"),
//    "_class" : "com.swooli.bo.user.UserProfile",
//    "version" : 0,
//    "username" : "user3",
//    "photoUri" : {
//        "string" : "http://user3/photo"
//    },
//    "password" : "some password",
//    "email" : "user3@gmail.com",
//    "country" : "USA",
//    "zipCode" : "12345",
//    "birthday" : NumberLong(1353033241026),
//    "accountStatus" : "DISABLED",
//    "creationDate" : NumberLong(1353033240027),
//    "securityQuestions" : [
//    {
//        "question" : "What street did you live on in 3rd grade?",
//        "answer" : "123 Mulberry Lane"
//    },
//    {
//        "question" : "Mother's maiden name?",
//        "answer" : "Bertha"
//    }
//    ]
//};
//db.UserProfile.insert(userProfile3);
//
///// Video Roots ///
//
//videoRoot1 = {
//    "_id" : ObjectId("50a66ad6e4b0decb049e324e"),
//    "_class" : "com.swooli.bo.video.VideoRoot",
//    "metadata" : {
//        "version" : 0,
//        "videoIdentifier" : {
//            "_class" : "com.swooli.bo.repository.YouTubeVideoIdentifier",
//            "thirdPartyId" : "videoId",
//            "originUri" : {
//                "string" : "http://video/origin/uri"
//            },
//            "thumbnailUri" : {
//                "string" : "http://video/thumbnail/uri"
//            },
//            "videoUri" : {
//                "string" : "http://video/uri"
//            }
//        },
//        "importedBy" : {
//            "userProfileId" : userProfile1._id.valueOf(),
//            "_class" : "com.swooli.bo.user.UserReference",
//            "username" : userProfile1.username,
//            "photoUri" : userProfile1.photoUri
//        },
//        "importDate" : NumberLong(1353083606152),
//        "title" : "video root 1"
//    },
//    "swinks" : [    {
//        "_id" : "50a59135e4b08c9b5e3e1ea7",
//        "createdBy" : {
//            "userProfileId" : userProfile1._id.valueOf(),
//            "_class" : "com.swooli.bo.user.UserReference",
//            "username" : userProfile1.username,
//            "photoUri" : userProfile1.photoUri
//        },
//        "description" : {
//            "title" : "title",
//            "description" : "aSwinkDescription",
//            "imageUri" : {
//                "string" : "http://swink/image/uri"
//            },
//            "originUri" : {
//                "string" : "http://swink/origin/uri"
//            }
//        },
//        "creationDate" : NumberLong(1321405493226)
//    },         {
//        "_id" : "50a5a5b3e4b0b112181908c6",
//        "createdBy" : {
//            "userProfileId" : userProfile2._id.valueOf(),
//            "_class" : "com.swooli.bo.user.UserReference",
//            "username" : userProfile2.username,
//            "photoUri" : userProfile2.photoUri
//        },
//        "description" : {
//            "title" : "title",
//            "description" : "aSwinkDescription",
//            "imageUri" : {
//                "string" : "http://swink/image/uri"
//            },
//            "originUri" : {
//                "string" : "http://swink/origin/uri"
//            }
//        },
//        "creationDate" : NumberLong(1353114293226)
//    },       {
//        "_id" : "50a5a5b3e4b0b112181908c7",
//        "createdBy" : {
//            "userProfileId" : userProfile2._id.valueOf(),
//            "_class" : "com.swooli.bo.user.UserReference",
//            "username" : userProfile2.username,
//            "photoUri" : userProfile2.photoUri
//        },
//        "description" : {
//            "title" : "title",
//            "description" : "aSwinkDescription",
//            "imageUri" : {
//                "string" : "http://swink/image/uri"
//            },
//            "originUri" : {
//                "string" : "http://swink/origin/uri"
//            }
//        },
//        "creationDate" : NumberLong(1384686293226)
//    } ],
//    "swinkCount" : 3,
//    "comments" : [    {
//        "_id" : "50a5a5b3e4b0b112181908c8",
//        "createdBy" : {
//            "userProfileId" : userProfile3._id.valueOf(),
//            "_class" : "com.swooli.bo.user.UserReference",
//            "username" : userProfile3.username,
//            "photoUri" : userProfile3.photoUri
//        },
//        "creationDate" : NumberLong(1321405493226),
//        "comment" : "comment1"
//    },       {
//        "_id" : "50a5a5b3e4b0b112181908c9",
//        "createdBy" : {
//            "userProfileId" : userProfile1._id.valueOf(),
//            "_class" : "com.swooli.bo.user.UserReference",
//            "username" : userProfile1.username,
//            "photoUri" : userProfile1.photoUri
//        },
//        "creationDate" : NumberLong(1352768693226),
//        "comment" : "comment2"
//    },       {
//        "_id" : "50a5a5b3e4b0b112181908ca",
//        "createdBy" : {
//            "userProfileId" : userProfile1._id.valueOf(),
//            "_class" : "com.swooli.bo.user.UserReference",
//            "username" : userProfile1.username,
//            "photoUri" : userProfile1.photoUri
//        },
//        "creationDate" : NumberLong(1384340693226),
//        "comment" : "comment3"
//    } ],
//    "commentCount" : 3
//}
//db.VideoRoot.insert(videoRoot1);
//
//videoRoot2 = {
//    "_id" : ObjectId("50a5a5b3e4b0b112181908ce"),
//    "_class" : "com.swooli.bo.video.VideoRoot",
//    "metadata" : {
//        "version" : 0,
//        "videoIdentifier" : {
//            "_class" : "com.swooli.bo.repository.YouTubeVideoIdentifier",
//            "thirdPartyId" : "videoId",
//            "originUri" : {
//                "string" : "http://video/origin/uri"
//            },
//            "thumbnailUri" : {
//                "string" : "http://video/thumbnail/uri"
//            },
//            "videoUri" : {
//                "string" : "http://video/uri"
//            }
//        },
//        "importedBy" : {
//            "userProfileId" : userProfile2._id.valueOf(),
//            "_class" : "com.swooli.bo.user.UserReference",
//            "username" : userProfile2.username,
//            "photoUri" : userProfile2.photoUri
//        },
//        "importDate" : NumberLong(1353083606152),
//        "title" : "video root 1"
//    },
//    "swinks" : [    {
//        "_id" : "50a5a5b3e4b0b112181908cd",
//        "createdBy" : {
//            "userProfileId" : userProfile2._id.valueOf(),
//            "_class" : "com.swooli.bo.user.UserReference",
//            "username" : userProfile2.username,
//            "photoUri" : userProfile2.photoUri
//        },
//        "description" : {
//            "title" : "title",
//            "description" : "aSwinkDescription",
//            "imageUri" : {
//                "string" : "http://swink/image/uri"
//            },
//            "originUri" : {
//                "string" : "http://swink/origin/uri"
//            }
//        },
//        "creationDate" : NumberLong(1321405493226)
//    }],
//    "swinkCount" : 1,
//    "comments" : [    {
//        "_id" : "50a5a5b3e4b0b112181908cc",
//        "createdBy" : {
//            "userProfileId" : userProfile2._id.valueOf(),
//            "_class" : "com.swooli.bo.user.UserReference",
//            "username" : userProfile2.username,
//            "photoUri" : userProfile2.photoUri
//        },
//        "creationDate" : NumberLong(1321405493226),
//        "comment" : "comment1"
//    }],
//    "commentCount" : 1
//}
//db.VideoRoot.insert(videoRoot2);
//
///// Video Collections ///
//
//videoCollection1 = {
//    "_id" : ObjectId("50a68ac3e4b0a4f0294057fc"),
//    "_class" : "com.swooli.bo.video.collection.VideoCollection",
//    "metadata" : {
//        "version" : 0,
//        "createdBy" : {
//            "userProfileId" : userProfile1._id.valueOf(),
//            "_class" : "com.swooli.bo.user.UserReference",
//            "username" : userProfile1.username,
//            "photoUri" : userProfile1.photoUri
//        },
//        "creationDate" : NumberLong(1353091779042),
//        "description" : {
//            "title" : "collection title",
//            "description" : "collection description",
//            "imageIdentifier" : {
//                "originUri" : {
//                    "string" : "http://origin/uri"
//                },
//                "imageUri" : {
//                    "string" : "http://image/uri"
//                }
//            },
//            "videoIdentifier" : {
//                "_class" : "com.swooli.bo.repository.YouTubeVideoIdentifier",
//                "thirdPartyId" : "videoId",
//                "originUri" : {
//                    "string" : "http://video/origin/uri"
//                },
//                "thumbnailUri" : {
//                    "string" : "http://video/thumbnail/uri"
//                },
//                "videoUri" : {
//                    "string" : "http://video/uri"
//                }
//            }
//        },
//        "visible" : true,
//        "open" : false,
//        "categories" : [ "EVERYTHING" ]
//    },
//    "members" : [ ],
//    "videos" : [       {
//        "metadata" : {
//            "_id" : "50a5a5b3e4b0b112181908cb",
//            "version" : 0,
//            "title" : "Title",
//            "videoRootId" : videoRoot1._id.valueOf(),
//            "addedBy" : {
//                "userProfileId" : userProfile1._id.valueOf(),
//                "_class" : "com.swooli.bo.user.UserReference",
//                "username" : userProfile1.username,
//                "photoUri" : userProfile1.photoUri
//            },
//            "thumbnailUri" : {
//                "string" : "http://video/thumbnail/uri"
//            },
//            "addedDate" : NumberLong(1353100483462),
//            "spotlighted" : true,
//            "spotlightedDate" : NumberLong(1353100483462)
//        }
//    },
//    {
//        "metadata" : {
//            "_id" : "50a5a5b3e4b0b112181908cf",
//            "version" : 0,
//            "title" : "Title2",
//            "videoRootId" : videoRoot2._id.valueOf(),
//            "addedBy" : {
//                "userProfileId" : userProfile2._id.valueOf(),
//                "_class" : "com.swooli.bo.user.UserReference",
//                "username" : userProfile2.username,
//                "photoUri" : userProfile2.photoUri
//            },
//            "thumbnailUri" : {
//                "string" : "http://video/thumbnail/uri"
//            },
//            "addedDate" : NumberLong(1353273283462),
//            "spotlighted" : true,
//            "spotlightedDate" : NumberLong(1355692483462)
//        }
//    },
//    {
//        "metadata" : {
//            "_id" : "50a5a5b3e4b0b112181908d0",
//            "version" : 0,
//            "title" : "Title",
//            "videoRootId" : videoRoot1._id.valueOf(),
//            "addedBy" : {
//                "userProfileId" : userProfile1._id.valueOf(),
//                "_class" : "com.swooli.bo.user.UserReference",
//                "username" : userProfile1.username,
//                "photoUri" : userProfile1.photoUri
//            },
//            "thumbnailUri" : {
//                "string" : "http://video/thumbnail/uri"
//            },
//            "addedDate" : NumberLong(1321478083462),
//            "spotlighted" : false
//        }
//    },
//    {
//        "metadata" : {
//            "_id" : "50a5a5b3e4b0b112181908d1",
//            "version" : 0,
//            "title" : "Title",
//            "videoRootId" : videoRoot2._id.valueOf(),
//            "addedBy" : {
//                "userProfileId" : userProfile1._id.valueOf(),
//                "_class" : "com.swooli.bo.user.UserReference",
//                "username" : userProfile1.username,
//                "photoUri" : userProfile1.photoUri
//            },
//            "thumbnailUri" : {
//                "string" : "http://video/thumbnail/uri"
//            },
//            "addedDate" : NumberLong(1289942083462),
//            "spotlighted" : false
//        }
//    }],
//    "videoCount" : 4,
//    "popCount" : 0
//}
//db.VideoCollection.insert(videoCollection1);
//
//videoCollection2 = {
//    "_id" : ObjectId("50a5a5b3e4b0b112181908d9"),
//    "_class" : "com.swooli.bo.video.collection.VideoCollection",
//    "metadata" : {
//        "version" : 0,
//        "createdBy" : {
//            "userProfileId" : userProfile1._id.valueOf(),
//            "_class" : "com.swooli.bo.user.UserReference",
//            "username" : userProfile1.username,
//            "photoUri" : userProfile1.photoUri
//        },
//        "creationDate" : NumberLong(1353091770000),
//        "description" : {
//            "title" : "collection title",
//            "description" : "collection description",
//            "imageIdentifier" : {
//                "originUri" : {
//                    "string" : "http://origin/uri"
//                },
//                "imageUri" : {
//                    "string" : "http://image/uri"
//                }
//            },
//            "videoIdentifier" : {
//                "_class" : "com.swooli.bo.repository.YouTubeVideoIdentifier",
//                "thirdPartyId" : "videoId",
//                "originUri" : {
//                    "string" : "http://video/origin/uri"
//                },
//                "thumbnailUri" : {
//                    "string" : "http://video/thumbnail/uri"
//                },
//                "videoUri" : {
//                    "string" : "http://video/uri"
//                }
//            }
//        },
//        "visible" : true,
//        "open" : false,
//        "categories" : [ "EVERYTHING" ]
//    },
//    "members" : [ ],
//    "videoCount" : 2,
//    "popCount" : 0
//}
//db.VideoCollection.insert(videoCollection2);
//
///// Video Ratings ///
//
//videoRating1 = {
//    "_id" : ObjectId("50a6acc3e4b0b4e1d969f3de"),
//    "_class" : "com.swooli.bo.video.VideoRating",
//    "videoCollectionId" : videoCollection1._id.valueOf(),
//    "videoId" : videoCollection1.videos[0].metadata._id.valueOf(),
//    "version" : 0,
//    "upVoteCount" : 5,
//    "downVoteCount" : 2,
//    "rating" : 0.4,
//    "creationDate" : videoCollection1.videos[0].metadata.addedDate
//}
//db.VideoRating.insert(videoRating1);
//
//videoRating2 = {
//    "_id" : ObjectId("50a5a5b3e4b0b112181908d2"),
//    "_class" : "com.swooli.bo.video.VideoRating",
//    "videoCollectionId" : videoCollection1._id.valueOf(),
//    "videoId" : videoCollection1.videos[1].metadata._id.valueOf(),
//    "version" : 0,
//    "upVoteCount" : 5,
//    "downVoteCount" : 2,
//    "rating" : 0.8,
//    "creationDate" : videoCollection1.videos[1].metadata.addedDate
//}
//db.VideoRating.insert(videoRating2);
//
//videoRating3 = {
//    "_id" : ObjectId("50a5a5b3e4b0b112181908d3"),
//    "_class" : "com.swooli.bo.video.VideoRating",
//    "videoCollectionId" : videoCollection1._id.valueOf(),
//    "videoId" : videoCollection1.videos[2].metadata._id.valueOf(),
//    "version" : 0,
//    "upVoteCount" : 5,
//    "downVoteCount" : 2,
//    "rating" : 0.8,
//    "creationDate" : videoCollection1.videos[2].metadata.addedDate
//}
//db.VideoRating.insert(videoRating3);
//
//videoRating4 = {
//    "_id" : ObjectId("50a5a5b3e4b0b112181908d4"),
//    "_class" : "com.swooli.bo.video.VideoRating",
//    "videoCollectionId" : videoCollection1._id.valueOf(),
//    "videoId" : videoCollection1.videos[3].metadata._id.valueOf(),
//    "version" : 0,
//    "upVoteCount" : 5,
//    "downVoteCount" : 2,
//    "rating" : 0.99,
//    //    "votes" : [  {
//    //        "createdBy" : {
//    //            "userProfileId" : userProfile1._id.valueOf(),
//    //            "_class" : "com.swooli.bo.user.UserReference",
//    //            "username" : userProfile1.username,
//    //            "photoUri" : userProfile1.photoUri
//    //        },
//    //        "upVote" : true,
//    //        "creationDate" : NumberLong(1353100483462)
//    //    },
//    //    {
//    //        "createdBy" : {
//    //            "userProfileId" : userProfile2._id.valueOf(),
//    //            "_class" : "com.swooli.bo.user.UserReference",
//    //            "username" : userProfile2.username,
//    //            "photoUri" : userProfile2.photoUri
//    //        },
//    //        "upVote" : true,
//    //        "creationDate" : NumberLong(1353100483462)
//    //    }],
//    "creationDate" : videoCollection1.videos[3].metadata.addedDate
//}
//db.VideoRating.insert(videoRating4);
//
///// pops ///
//
//pop1 = {
//    "_id" : "50a5a5b3e4b0b112181908d5",
//    "videoCollectionId" : videoCollection1._id.valueOf(),
//    "category" : "EVERYTHING",
//    "creationDate" : NumberLong(1353553194512)
//}
//db.Pop.insert(pop1);
//
//pop2 = {
//    "_id" : "50a5a5b3e4b0b112181908d7",
//    "videoCollectionId" : videoCollection1._id.valueOf(),
//    "category" : "EVERYTHING",
//    "creationDate" : NumberLong(1353553194522)
//}
//db.Pop.insert(pop2);
//
//pop3 = {
//    "_id" : "50a5a5b3e4b0b112181908d8",
//    "videoCollectionId" : videoCollection1._id.valueOf(),
//    "category" : "COOKING",
//    "creationDate" : NumberLong(1353553194500)
//}
//db.Pop.insert(pop3);
//
//pop4 = {
//    "_id" : "50a5a5b3e4b0b112181908da",
//    "videoCollectionId" : videoCollection2._id.valueOf(),
//    "category" : "EVERYTHING",
//    "creationDate" : NumberLong(1353553194510)
//}
//db.Pop.insert(pop4);
//
//pop5 = {
//    "_id" : "50a5a5b3e4b0b112181908db",
//    "videoCollectionId" : videoCollection2._id.valueOf(),
//    "category" : "EVERYTHING",
//    "creationDate" : NumberLong(1353553194501)
//}
//db.Pop.insert(pop5);
//
//pop6 = {
//    "_id" : "50a5a5b3e4b0b112181908dc",
//    "videoCollectionId" : videoCollection2._id.valueOf(),
//    "category" : "COOKING",
//    "creationDate" : NumberLong(1353553194599)
//}
//db.Pop.insert(pop6);
//
//
//
