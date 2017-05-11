// org.broadinstitute.gpp.rigerj.impl.HairpinSetScoringAlgorithm

package org.broadinstitute.gpp.rigerj.impl;

/**
 * An API for a hairpin set scoring algorithm. Computes a single real score based on the
 * real scores for a set of target hairpins, and their ranks within the overall set of hairpins.
 *
 * @author <a href="http://www.broadinstitute.org/genetic-perturbation-platform">Broad Institute, Genetic Perturbation Platform</a>
 */
public interface HairpinSetScoringAlgorithm {

    /**
     * @param numTotalHairpinScores the total number of hairpins in the input. The actual scores are
     * only needed for the target hairpins
     * @param targetHairpinScores the scores for the hairpins in the hairpin set to be scored, unsorted
     * @param targetHairpinScoreIndexes the indexes of the target hairpin scores in the allHairpinScores array
     * @param targetHairpinWeights the weight factors for the hairpins. should be values between 0 and 1 (both inclusive)
     * @param alpha a weighting factor used by {@link KolmogorovSmirnovScoringAlgorithm} that I don't fully understand -
     * please ask Shuba (shuba@broadinstitute.org)
     * 
     * @return the score for the hairpin set
     */
    double scoreHairpinSet(int numTotalHairpinScores,
                           double[] targetHairpinScores,
                           int[] targetHairpinScoreIndexes,
                           double[] targetHairpinWeights,
                           double alpha);

    /**
     * Given the list of sorted random scores for hairpin sets of a given size, compute an adjustment factor for that gene set size.
     * 
     * @param sortedRandomScores the list of sorted random scores for hairpin sets of a given size
     * 
     * @return an adjustment factor for that gene set size
     */
    GeneScoreAdjuster computeGeneSetSizeAdjustment(double[] sortedRandomScores);

    /**
     * @return true whenever a low score produced by the scoring algorithm is a better score. Generally speaking, algorithms
     * that score by rank produce rank-like scores, in which case, low scores are better scores. For other algorithms,
     * such as Kolmogorov-Smirnov, high scores are better scores.
     */
    boolean lowScoresRankFirst();
}
