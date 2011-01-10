// org.broadinstitute.rnai.rigerj.impl.GeneData

package org.broadinstitute.rnai.rigerj.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.broadinstitute.rnai.rigerj.api.GeneOutput;
import org.broadinstitute.rnai.rigerj.api.RigerAlgorithm;
import org.broadinstitute.rnai.rigerj.api.RigerOutputs;

/**
 * A POJO for constructing the {@link GeneOutput} for a gene in the {@link RigerOutputs}.
 * The {@link RigerImpl}, and potentially other implementations of the {@link RigerAlgorithm},
 * can use the setters to construct the algorithm outputs. Exposed to the users as a
 * {@link GeneOutput}, only the getters are available for use.
 *
 * @author <a href="http://www.broad.mit.edu/rnai_platform">Broad Institute, RNAi Platform</a>
 */
public class GeneData implements GeneOutput {

    int geneRank;
    String geneName;
    double geneScore;
    double pValue;
    int pValueRank;
    List<HairpinData> hairpinDatas = new ArrayList<HairpinData>();

    public int getGeneRank() {
        return geneRank;
    }

    public void setGeneRank(int geneRank) {
        this.geneRank = geneRank;
    }

    public String getGeneName() {
        return geneName;
    }

    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }

    public double getGeneScore() {
        return geneScore;
    }

    public void setGeneScore(double geneScore) {
        this.geneScore = geneScore;
    }

    public double getPValue() {
        return pValue;
    }

    public void setPValue(double pValue) {
        this.pValue = pValue;
    }

    public int getPValueRank() {
        return pValueRank;
    }

    public void setPValueRank(int pValueRank) {
        this.pValueRank = pValueRank;
    }

    public List<HairpinData> getHairpinDatas() {
        return hairpinDatas;
    }

    public void addHairpinData(HairpinData hairpinData) {
        hairpinDatas.add(hairpinData);
    }

    public String getHairpinRanks() {
        List<Integer> hairpinRanks = new ArrayList<Integer>();
        for (HairpinData hairpinData : hairpinDatas) {
            hairpinRanks.add(hairpinData.getHairpinRank());
        }
        Collections.sort(hairpinRanks);
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer hairpinRank : hairpinRanks) {
            stringBuilder.append(hairpinRank);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }
}
