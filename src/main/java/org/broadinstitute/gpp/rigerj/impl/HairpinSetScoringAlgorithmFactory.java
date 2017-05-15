// org.broadinstitute.gpp.rigerj.impl.HairpinSetScoringAlgorithmFactory

package org.broadinstitute.gpp.rigerj.impl;

import org.broadinstitute.gpp.rigerj.api.HairpinSetScoringMethod;

/**
 * A factory for building a {@link HairpinSetScoringAlgorithm} based on a {@link HairpinSetScoringMethod}.
 *
 * @author <a href="http://www.broadinstitute.org/genetic-perturbation-platform">Broad Institute, Genetic Perturbation Platform</a>
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
