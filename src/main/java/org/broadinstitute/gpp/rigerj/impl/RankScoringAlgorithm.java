// rigerj:org.broadinstitute.gpp.rigerj.impl.RankScoringAlgorithm

package org.broadinstitute.gpp.rigerj.impl;

/**
 * Shared code for scoring algorithms that score based on the hairpin ranks.
 *
 * @author <a href="http://www.broadinstitute.org/genetic-perturbation-platform">Broad Institute, Genetic Perturbation Platform</a>
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
