// org.broadinstitute.rnai.rigerj.api.RigerAlgorithm

package org.broadinstitute.rnai.rigerj.api;

/**
 * An API for implementations of the RIGER algorithm.
 *
 * @author <a href="http://www.broad.mit.edu/rnai_platform">Broad Institute, RNAi Platform</a>
 */
public interface RigerAlgorithm {

    RigerOutputs execute(RigerInputs rigerInputs);
}
