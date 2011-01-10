// org.broadinstitute.rnai.rigerj.impl.WeightedSumScoringAlgorithm

package org.broadinstitute.rnai.rigerj.impl;

import java.util.Arrays;

/**
 * Scores the hairpin set as a weighted sum of the ranks of the best and the second best
 * ranking hairpins in the set. The best rank is weighted 0.25, and the second best rank is
 * weighted 0.75.
 *
 * @author <a href="http://www.broad.mit.edu/rnai_platform">Broad Institute, RNAi Platform</a>
 */
public class WeightedSumScoringAlgorithm extends RankScoringAlgorithm implements HairpinSetScoringAlgorithm {

    private static final double BEST_RANK_WEIGHT = 0.25;
    private static final double SECOND_BEST_RANK_WEIGHT = 0.75;

    public double scoreHairpinSet(int numTotalHairpinScores,
                                  double[] targetHairpinScores,
                                  int[] targetHairpinScoreIndexes,
                                  double[] targetHairpinWeights,
                                  double alpha) {
        Arrays.sort(targetHairpinScoreIndexes);
        return
        BEST_RANK_WEIGHT * targetHairpinScoreIndexes[0] + 
        SECOND_BEST_RANK_WEIGHT * targetHairpinScoreIndexes[1] +
        1;
    }

    public boolean lowScoresRankFirst() {
        return true;
    }
}
