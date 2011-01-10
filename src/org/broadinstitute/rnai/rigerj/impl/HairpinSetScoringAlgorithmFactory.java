// org.broadinstitute.rnai.rigerj.impl.HairpinSetScoringAlgorithmFactory

package org.broadinstitute.rnai.rigerj.impl;

import org.broadinstitute.rnai.rigerj.api.HairpinSetScoringMethod;

/**
 * A factory for building a {@link HairpinSetScoringAlgorithm} based on a {@link HairpinSetScoringMethod}.
 *
 * @author <a href="http://www.broad.mit.edu/rnai_platform">Broad Institute, RNAi Platform</a>
 */
public class HairpinSetScoringAlgorithmFactory {

    public HairpinSetScoringAlgorithm createHairpinSetScoringAlgorithm(HairpinSetScoringMethod hairpinSetScoringMethod) {
        switch (hairpinSetScoringMethod) {
        case WEIGHTED_SUM:
            return new WeightedSumScoringAlgorithm();
        case SECOND_BEST_RANK:
            return new SecondBestRankScoringAlgorithm();
        case KOLMOGOROV_SMIRNOV:
            return new KolmogorovSmirnovScoringAlgorithm();
        default:
            throw new RuntimeException("unrecognized HairpinSetScoringMethod: " + hairpinSetScoringMethod);
        }
    }
}
