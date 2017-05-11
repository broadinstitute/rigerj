// org.broadinstitute.gpp.rigerj.api.RigerOutputs

package org.broadinstitute.gpp.rigerj.api;

/**
 * An API for a provider of the full set of outputs to the RIGER algorithm.
 *
 * @author <a href="http://www.broadinstitute.org/genetic-perturbation-platform">Broad Institute, Genetic Perturbation Platform</a>
 */
public interface RigerOutputs {

    public int getNumGenes();

    public GeneOutput getGeneOutput(int i);
}
