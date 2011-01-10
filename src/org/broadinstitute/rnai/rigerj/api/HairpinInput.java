// org.broadinstitute.rnai.rigerj.api.HairpinInput

package org.broadinstitute.rnai.rigerj.api;

/**
 * Information about a single hairpin provided in the input of RIGER.
 *
 * @author <a href="http://www.broad.mit.edu/rnai_platform">Broad Institute, RNAi Platform</a>
 */
public interface HairpinInput {

    int getHairpinRank();
    
    String getHairpinName();
    
    double getHairpinScore();
    
    String getGeneName();

    double getHairpinWeight();
}
