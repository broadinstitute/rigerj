// org.broadinstitute.rnai.rigerj.impl.SecondBestRankScoringAlgorithm

package org.broadinstitute.rnai.rigerj.impl;

import java.util.Arrays;

/**
 * Scores the hairpin set as the rank of the second best ranking hairpin in the set.
 *
 * @author <a href="http://www.broad.mit.edu/rnai_platform">Broad Institute, RNAi Platform</a>
 */
public class SecondBestRankScoringAlgorithm extends RankScoringAlgorithm implements HairpinSetScoringAlgorithm {

    public double scoreHairpinSet(int numTotalHairpinScores,
                                  double[] targetHairpinScores,
                                  int[] targetHairpinScoreIndexes,
                                  double[] targetHairpinWeights,
                                  double alpha) {
        Arrays.sort(targetHairpinScoreIndexes);
        return targetHairpinScoreIndexes[1] + 1;
    }

    public boolean lowScoresRankFirst() {
        return true;
    }
}
