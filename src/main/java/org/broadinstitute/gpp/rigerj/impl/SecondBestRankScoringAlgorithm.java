// org.broadinstitute.gpp.rigerj.impl.SecondBestRankScoringAlgorithm

package org.broadinstitute.gpp.rigerj.impl;

import java.util.Arrays;

/**
 * Scores the hairpin set as the rank of the second best ranking hairpin in the set.
 *
 * @author <a href="http://www.broadinstitute.org/genetic-perturbation-platform">Broad Institute, Genetic Perturbation Platform</a>
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
