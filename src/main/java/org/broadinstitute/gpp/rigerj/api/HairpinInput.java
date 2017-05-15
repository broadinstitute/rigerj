// org.broadinstitute.gpp.rigerj.api.HairpinInput

package org.broadinstitute.gpp.rigerj.api;

/**
 * Information about a single hairpin provided in the input of RIGER.
 *
 * @author <a href="http://www.broadinstitute.org/genetic-perturbation-platform">Broad Institute, Genetic Perturbation Platform</a>
 */
public interface HairpinInput {

    int getHairpinRank();
    
    String getHairpinName();
    
    double getHairpinScore();
    
    String getGeneName();

    double getHairpinWeight();
}
