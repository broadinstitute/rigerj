// org.broadinstitute.rnai.rigerj.api.RigerInputs

package org.broadinstitute.rnai.rigerj.api;

/**
 * An API for a provider of the full set of inputs to the RIGER algorithm.
 *
 * @author <a href="http://www.broad.mit.edu/rnai_platform">Broad Institute, RNAi Platform</a>
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
