// org.broadinstitute.rnai.rigerj.impl.KolmogorovSmirnovScoringAlgorithm

package org.broadinstitute.rnai.rigerj.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The Kolmogorov Smirnov hairpin set scoring algorithm.
 *
 * @author <a href="http://www.broad.mit.edu/rnai_platform">Broad Institute, RNAi Platform</a>
 */
public class KolmogorovSmirnovScoringAlgorithm implements HairpinSetScoringAlgorithm {

    public double scoreHairpinSet(final int numTotalHairpinScores,
                                  final double[] targetHairpinScores,
                                  final int[] targetHairpinScoreIndexes,
                                  double[] targetHairpinWeights,
                                  final double alpha) {

        double nonTargetSetScoreWeight = (double) -1 / (numTotalHairpinScores - targetHairpinScores.length);

        double[] weightedTargetSetScores = new double[targetHairpinScores.length];
        double sumOfWeightedTargetSetScores = 0;
        for (int i = 0; i < targetHairpinScores.length; i++) {
            final double rawTargetHairpinScore = targetHairpinScores[i];
            final double weightedTargetHairpinScore = rawTargetHairpinScore * targetHairpinWeights[i];
            final double weightedTargetSetScore = Math.pow(Math.abs(weightedTargetHairpinScore), alpha);
            weightedTargetSetScores[i] = weightedTargetSetScore;
            sumOfWeightedTargetSetScores += weightedTargetSetScore;
        }
        
        Map<Integer,Double> targetHairpinIndexToScoreMap = new HashMap<Integer,Double>();
        for (int i = 0; i < targetHairpinScores.length; i++) {
            targetHairpinIndexToScoreMap.put(targetHairpinScoreIndexes[i],
                                             weightedTargetSetScores[i] / sumOfWeightedTargetSetScores);
        }

        final int[] orderedTargetHairpinScoreIndexes = copyAndSortTargetHairpinScoreIndexes(targetHairpinScoreIndexes);

        double cumulativeScore = 0;
        double maxCumulativeScore = 0;
        double minCumulativeScore = 0;

        int lastTargetHairpinIndex = -1;
        for (int i = 0; i < orderedTargetHairpinScoreIndexes.length; i++) {
            int targetHairpinIndex = orderedTargetHairpinScoreIndexes[i];
            int numSkippedNonTargetSetScores = targetHairpinIndex - lastTargetHairpinIndex - 1;

            // because nonTargetSetScoreWeight is negative, no need to update maxCumulativeScore here
            cumulativeScore += numSkippedNonTargetSetScores * nonTargetSetScoreWeight;
            minCumulativeScore = Math.min(minCumulativeScore, cumulativeScore);

            // because the target hairpin score is positive, no need to update minCumulativeScore here
            cumulativeScore += targetHairpinIndexToScoreMap.get(targetHairpinIndex);
            maxCumulativeScore = Math.max(maxCumulativeScore, cumulativeScore);
            
            lastTargetHairpinIndex = targetHairpinIndex;
        }
        int numSkippedNonTargetSetScores = numTotalHairpinScores - lastTargetHairpinIndex - 1;

        // because nonTargetSetScoreWeight is negative, no need to update maxCumulativeScore here
        cumulativeScore += numSkippedNonTargetSetScores * nonTargetSetScoreWeight;
        minCumulativeScore = Math.min(minCumulativeScore, cumulativeScore);

        double hairpinSetScore;
        if (Math.abs(maxCumulativeScore) > Math.abs(minCumulativeScore)) {
            hairpinSetScore = maxCumulativeScore;
        }
        else {
            hairpinSetScore = minCumulativeScore;
        }
        return roundToSignificantFigures(hairpinSetScore, 5);
    }

    private int[] copyAndSortTargetHairpinScoreIndexes(final int[] targetHairpinScoreIndexes) {
        
        // could probably use Object#clone() for this. cannot use Arrays.copyOf as it is not Java5-compliant
        final int[] orderedTargetHairpinScoreIndexes = new int[targetHairpinScoreIndexes.length];
        for (int i = 0; i < targetHairpinScoreIndexes.length; i++) {
            orderedTargetHairpinScoreIndexes[i] = targetHairpinScoreIndexes[i];
        }

        Arrays.sort(orderedTargetHairpinScoreIndexes);
        return orderedTargetHairpinScoreIndexes;
    }

    // thanks to http://stackoverflow.com/questions/202302/rounding-to-an-arbitrary-number-of-significant-digits
    public static double roundToSignificantFigures(final double num, final int n) {
        if (num == 0) {
            return 0;
        }

        final double d = Math.ceil(Math.log10(num < 0 ? -num: num));
        final int power = n - (int) d;

        final double magnitude = Math.pow(10, power);
        final long shifted = Math.round(num * magnitude);
        return shifted / magnitude;
    }

    /**
     * For KS, the gene set size adjustment is the mean of the random scores
     */
    public GeneScoreAdjuster computeGeneSetSizeAdjustment(double[] sortedRandomScores) {
        double sumOfPositiveRandomScores = 0;
        int numPositiveRandomScores = 0;
        double sumOfNegativeRandomScores = 0;
        int numNegativeRandomScores = 0;
        for (int i = 0; i < sortedRandomScores.length; i++) {
            final double randomScore = sortedRandomScores[i];
            if (randomScore >= 0) {
                sumOfPositiveRandomScores += randomScore;
                numPositiveRandomScores++;                
            }
            else {
                sumOfNegativeRandomScores += randomScore;
                numNegativeRandomScores++;
            }
        }
        final double positiveScoreAdjustmentFactor = sumOfPositiveRandomScores / ((double) numPositiveRandomScores); 
        final double negativeScoreAdjustmentFactor = ((double) -1) * sumOfNegativeRandomScores / ((double) numNegativeRandomScores);

        return new GeneScoreAdjuster() {
            
            public double adjustGeneScore(double unadjustedScore) {
                if (unadjustedScore >= 0) {
                    return unadjustedScore / positiveScoreAdjustmentFactor;
                }
                else {
                    return unadjustedScore / negativeScoreAdjustmentFactor;
                }
            }
        };
    }

    public boolean lowScoresRankFirst() {
        return false;
    }
}
