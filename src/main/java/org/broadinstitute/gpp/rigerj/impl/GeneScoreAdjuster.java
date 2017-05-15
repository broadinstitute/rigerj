// rigerj:org.broadinstitute.gpp.rigerj.impl.GeneScoreAdjuster

package org.broadinstitute.gpp.rigerj.impl;

/**
 * An adjuster for a gene set score, based on the mull distribution for the given gene set
 * size.
 *
 * @author <a href="http://www.broadinstitute.org/genetic-perturbation-platform">Broad Institute, Genetic Perturbation Platform</a>
 */
public interface GeneScoreAdjuster {

    double adjustGeneScore(double unadjustedScore);
}
