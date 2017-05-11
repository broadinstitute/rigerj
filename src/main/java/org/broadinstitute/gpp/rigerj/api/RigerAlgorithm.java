// org.broadinstitute.gpp.rigerj.api.RigerAlgorithm

package org.broadinstitute.gpp.rigerj.api;

/**
 * An API for implementations of the RIGER algorithm.
 *
 * @author <a href="http://www.broadinstitute.org/genetic-perturbation-platform">Broad Institute, Genetic Perturbation Platform</a>
 */
public interface RigerAlgorithm {

    RigerOutputs execute(RigerInputs rigerInputs);
}
