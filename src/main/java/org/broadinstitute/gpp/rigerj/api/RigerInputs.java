// org.broadinstitute.gpp.rigerj.api.RigerInputs

package org.broadinstitute.gpp.rigerj.api;

/**
 * An API for a provider of the full set of inputs to the RIGER algorithm.
 *
 * @author <a href="http://www.broadinstitute.org/genetic-perturbation-platform">Broad Institute, Genetic Perturbation Platform</a>
 */
public interface RigerInputs {

    int getNumHairpins();

    HairpinInput getHairpinInput(int i);

    HairpinSetScoringMethod getHairpinSetScoringMethod();

    boolean flattenWeights();

    int getNumRandomScoresPerGeneSetSize();

    double getAlpha();

    long getRandomSeed();
    
    boolean adjustForHairpinSetSize();
}
