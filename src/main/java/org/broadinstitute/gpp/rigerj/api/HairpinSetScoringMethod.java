// org.broadinstitute.gpp.rigerj.api.HairpinSetScoringMethod

package org.broadinstitute.gpp.rigerj.api;

/**
 * An enumeration of the different methods available to RIGER for scoring a set of hairpins.
 *
 * @author <a href="http://www.broadinstitute.org/genetic-perturbation-platform">Broad Institute, Genetic Perturbation Platform</a>
 */
public enum HairpinSetScoringMethod {

    WEIGHTED_SUM("WtSum"),

    SECOND_BEST_RANK("SecondBestRank"),

    KOLMOGOROV_SMIRNOV("KSbyScore");
    
    private final String parameterName;
    
    HairpinSetScoringMethod(final String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
    }
}
