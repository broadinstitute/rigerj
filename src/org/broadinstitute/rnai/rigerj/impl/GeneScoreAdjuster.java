// rigerj:org.broadinstitute.rnai.rigerj.impl.GeneScoreAdjuster

package org.broadinstitute.rnai.rigerj.impl;

/**
 * An adjuster for a gene set score, based on the mull distribution for the given gene set
 * size.
 *
 * @author <a href="http://www.broad.mit.edu/rnai_platform">Broad Institute, RNAi Platform</a>
 */
public interface GeneScoreAdjuster {

    double adjustGeneScore(double unadjustedScore);
}
