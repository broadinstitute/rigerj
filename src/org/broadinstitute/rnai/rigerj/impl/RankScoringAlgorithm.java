// rigerj:org.broadinstitute.rnai.rigerj.impl.RankScoringAlgorithm

package org.broadinstitute.rnai.rigerj.impl;

/**
 * Shared code for scoring algorithms that score based on the hairpin ranks.
 *
 * @author <a href="http://www.broad.mit.edu/rnai_platform">Broad Institute, RNAi Platform</a>
 */
public abstract class RankScoringAlgorithm implements HairpinSetScoringAlgorithm {

    /**
     * For second-best and weighted-sum, the gene set size adjustment is the 90th percentile score
     */
    public GeneScoreAdjuster computeGeneSetSizeAdjustment(double[] sortedRandomScores) {
        final double orderStatisticIndex = (double) sortedRandomScores.length * (double) 0.9;
        final double adjustmentFactor = sortedRandomScores[(int) orderStatisticIndex];
        return new GeneScoreAdjuster() {
            
            public double adjustGeneScore(double unadjustedScore) {
                return unadjustedScore / adjustmentFactor;
            }
        };
    }
}
