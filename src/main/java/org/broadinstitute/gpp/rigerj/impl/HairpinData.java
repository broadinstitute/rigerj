// org.broadinstitute.gpp.rigerj.impl.HairpinData

package org.broadinstitute.gpp.rigerj.impl;

import org.broadinstitute.gpp.rigerj.api.HairpinInput;

/**
 * A {@link HairpinInput} with setters available for the properties. This is used by
 * {@link RigerImpl} in order to flatten weights in place. Other useful input transformations
 * are conceivable, but not terribly likely.
 *
 * @author <a href="http://www.broadinstitute.org/genetic-perturbation-platform">Broad Institute, Genetic Perturbation Platform</a>
 */
public class HairpinData implements HairpinInput {

    private int hairpinRank;
    private String hairpinName;
    private double hairpinScore;
    private String geneName;
    private double hairpinWeight;
    
    public HairpinData(HairpinInput hairpinInput) {
        setHairpinRank(hairpinInput.getHairpinRank());
        setHairpinName(hairpinInput.getHairpinName());
        setHairpinScore(hairpinInput.getHairpinScore());
        setGeneName(hairpinInput.getGeneName());
        setHairpinWeight(hairpinInput.getHairpinWeight());
    }
    
    public HairpinData(int hairpinRank,
                       String hairpinName,
                       double hairpinScore,
                       String geneName,
                       double hairpinWeight) {
        setHairpinRank(hairpinRank);
        setHairpinName(hairpinName);
        setHairpinScore(hairpinScore);
        setGeneName(geneName);
        setHairpinWeight(hairpinWeight);
    }
    
    public int getHairpinRank() {
        return hairpinRank;
    }
    public void setHairpinRank(int hairpinRank) {
        this.hairpinRank = hairpinRank;
    }
    public String getHairpinName() {
        return hairpinName;
    }
    public void setHairpinName(String hairpinName) {
        this.hairpinName = hairpinName;
    }
    public double getHairpinScore() {
        return hairpinScore;
    }
    public void setHairpinScore(double hairpinScore) {
        this.hairpinScore = hairpinScore;
    }
    public String getGeneName() {
        return geneName;
    }
    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }
    public double getHairpinWeight() {
        return hairpinWeight;
    }
    public void setHairpinWeight(double hairpinWeight) {
        this.hairpinWeight = hairpinWeight;
    }
}
