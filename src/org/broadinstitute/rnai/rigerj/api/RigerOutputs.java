// org.broadinstitute.rnai.rigerj.api.RigerOutputs

package org.broadinstitute.rnai.rigerj.api;

/**
 * An API for a provider of the full set of outputs to the RIGER algorithm.
 *
 * @author <a href="http://www.broad.mit.edu/rnai_platform">Broad Institute, RNAi Platform</a>
 */
public interface RigerOutputs {

    public int getNumGenes();

    public GeneOutput getGeneOutput(int i);
}
