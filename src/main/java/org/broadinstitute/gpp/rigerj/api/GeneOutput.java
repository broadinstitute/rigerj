// org.broadinstitute.gpp.rigerj.api.GeneOutput

package org.broadinstitute.gpp.rigerj.api;

/**
 * Information needed about a single gene in the output of RIGER.
 *
 * @author <a href="http://www.broadinstitute.org/genetic-perturbation-platform">Broad Institute, Genetic Perturbation Platform</a>
 */
public interface GeneOutput {

    int getGeneRank();
    
    String getGeneName();
    
    double getGeneScore();
    
    double getPValue();
    
    int getPValueRank();
    
    /**
     * @return space-separated list of the hairpin ranks from smallest to largest
     */
    String getHairpinRanks();
}
