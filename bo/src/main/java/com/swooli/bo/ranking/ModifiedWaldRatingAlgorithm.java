package com.swooli.bo.ranking;

/**
 * Implements the "modified" Wald algorithm as the rating algorithm for swopic videos.  Details
 * about the algorithm can be found at http://www.measuringusability.com/wald.htm.
 *
 * To correctly handle cases when all votes are down votes, the algorithm is tweaked as follows:
 * <code>
 *   if(upVotes == 0) {
 *     return 1 / modifiedWald;
 *   } else {
 *     return modifiedWald;
 *   }
 * </code>
 *
 * @author jmcarey
 */
public class ModifiedWaldRatingAlgorithm implements VideoRatingAlgorithm {

    private final ZValue zValue;

    /**
     * Default construction, which initializes the "z value" to 80%.
     */
    public ModifiedWaldRatingAlgorithm() {
        this.zValue = ZValue.Z_80;
    }

    public ModifiedWaldRatingAlgorithm(final ZValue zValue) {
        this.zValue = zValue;
    }

    @Override
    public String getName() {
        return "Modified Wald";
    }

    @Override
    public double computeRating(final int upVotes, final int downVotes) {

        final double totalVotes = (double) upVotes + downVotes;
        if(totalVotes == 0) {
            return 0;
        }

        final double z = zValue.zValue();
        final double zSquared = z * z;
        final double zSquaredHalf = zSquared / 2.0;

        final double p = (upVotes + zSquaredHalf) / (totalVotes + zSquared);

        final double modWald = p - z * Math.sqrt( p * (1 - p) / (totalVotes + zSquared) );

        if(upVotes == 0) {
            return 1 / modWald;
        } else {
            return modWald;
        }

    }

}