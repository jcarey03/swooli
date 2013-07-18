package com.swooli.bo.ranking;

/**
 * Defines the interface for a video rating strategy.
 *
 * @author jmcarey
 */
public interface VideoRatingAlgorithm {

    /**
     * @return the user-friendly name identifying the strategy.
     */
    String getName();

    /**
     * Computes the rating based on the positive votes and the negative votes of a video.
     *
     * @param upVotes the positive votes
     * @param downVotes the negative votes
     *
     * @return the rating
     */
    double computeRating(int upVotes, int downVotes);
}