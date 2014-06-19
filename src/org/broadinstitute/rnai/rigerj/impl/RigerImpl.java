// org.broadinstitute.rnai.rigerj.impl.RigerImpl

package org.broadinstitute.rnai.rigerj.impl;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.broadinstitute.rnai.rigerj.api.GeneOutput;
import org.broadinstitute.rnai.rigerj.api.HairpinInput;
import org.broadinstitute.rnai.rigerj.api.HairpinSetScoringMethod;
import org.broadinstitute.rnai.rigerj.api.RigerAlgorithm;
import org.broadinstitute.rnai.rigerj.api.RigerInputs;
import org.broadinstitute.rnai.rigerj.api.RigerJInputException;
import org.broadinstitute.rnai.rigerj.api.RigerOutputs;

/**
 * Standard Java implementation of the RIGER algorithm.
 * 
 * <p>
 * 
 * Roughly speaking, this implementation was written according to the following specifications:
 * 
 * <a href="https://www.broadinstitute.org/twiki/bin/view/RNAiplatform/RIGERAlgorithm">
 * https://www.broadinstitute.org/twiki/bin/view/RNAiplatform/RIGERAlgorithm
 * </a>
 *
 * @author <a href="http://www.broad.mit.edu/rnai_platform">Broad Institute, RNAi Platform</a>
 */
public class RigerImpl implements RigerAlgorithm {

    private static final int MIN_NUM_RANDOM_SCORES_PER_GENE_SET_SIZE = 10000;
    
    /**
     * The algorithm inputs as supplied by the user
     */
    private RigerInputs rigerInputs;

    /**
     * The number of random scores per gene set size, used to calculate
     * null distributions. This differs from {@link
     * RigerInputs#getNumRandomScoresPerGeneSetSize() the input parameter
     * with the same name} in that, the user might have selected a value
     * less than {@link #MIN_NUM_RANDOM_SCORES_PER_GENE_SET_SIZE the minimum
     * allowed by the algorith}.
     */
    private int numRandomScoresPerGeneSetSize;

    /**
     * The random number generator used to produce null distributions. This is seeded by user-selected input
     * parameter {@link RigerInputs#getRandomSeed()}, so the user can run the algorithm reproducibly.
     */
    private Random randomNumberGenerator;

    /**
     * The {@link HairpinSetScoringAlgorithm hairpin set scoring algorithm} to use for the RIGER
     * analysis
     */
    private HairpinSetScoringAlgorithm hairpinSetScoringAlgorithm;

    /**
     * The {@link HairpinData hairpin datas}, ordered by {@link HairpinData#getHairpinScore() score} from smallest to largest
     */
    private HairpinData[] orderedHairpinDatas;

    /**
     * A simple map from the gene name to the {@link GeneData data we are collecting on a gene level}.
     */
    private Map<String,GeneData> geneNameToGeneDataMap;

    /**
     * A map from gene set size to the {@link GeneData gene datas} for genes that have that set size
     */
    private Map<Integer,Set<GeneData>> geneSetSizeToGeneDatasMap;

    public synchronized RigerOutputs execute(RigerInputs rigerInputs) {
        this.rigerInputs = rigerInputs;
        initializeNumRandomScoresPerGeneSetSize();
        initializeRandomNumberGenerator();
        initializeHairpinSetScoringAlgorithm();
        initializeOrderedHairpinDatas();
        initializeGeneNameToGeneDataMap();
        initializeGeneSetSizeToGeneDatasMap();
        checkAllGeneSetSizesGreaterThanOne();
        computeGeneScoresAndPValues();
        GeneData[] geneDatasSortedByPValueRank = computeGeneRanks(); 
        computePValueRanks();
        return buildRigerOutputs(geneDatasSortedByPValueRank);
    }
    
    private void initializeNumRandomScoresPerGeneSetSize() {
        numRandomScoresPerGeneSetSize = rigerInputs.getNumRandomScoresPerGeneSetSize();
        if (numRandomScoresPerGeneSetSize < MIN_NUM_RANDOM_SCORES_PER_GENE_SET_SIZE) {
            numRandomScoresPerGeneSetSize = MIN_NUM_RANDOM_SCORES_PER_GENE_SET_SIZE;
        }
    }

    private void initializeRandomNumberGenerator() {
        randomNumberGenerator = new Random(rigerInputs.getRandomSeed());
    }

    private void initializeHairpinSetScoringAlgorithm() {
        final HairpinSetScoringAlgorithmFactory factory = new HairpinSetScoringAlgorithmFactory();
        final HairpinSetScoringMethod method = rigerInputs.getHairpinSetScoringMethod();
        hairpinSetScoringAlgorithm = factory.createHairpinSetScoringAlgorithm(method);
    }

    private void initializeOrderedHairpinDatas() {
        HairpinData[] hairpinDatas = buildUnorderedHairpinDatas();
        flattenHairpinWeights(hairpinDatas);
        Arrays.sort(hairpinDatas, new HairpinScoreAscendingComparator());
        orderedHairpinDatas = hairpinDatas;
    }

    private HairpinData[] buildUnorderedHairpinDatas() {
        final int numHairpins = rigerInputs.getNumHairpins();
        final HairpinData[] unorderedHairpinDatas = new HairpinData[numHairpins];
        for (int i = 0; i < numHairpins; i++) {
            final HairpinInput hairpinInput = rigerInputs.getHairpinInput(i);
            final HairpinData hairpinData = new HairpinData(hairpinInput);
            unorderedHairpinDatas[i] = hairpinData;
        }
        return unorderedHairpinDatas;
    }

    private void flattenHairpinWeights(final HairpinData[] unorderedHairpinDatas) {
        if (!rigerInputs.flattenWeights()) {
            return;
        }
        for (final HairpinData hairpinData : unorderedHairpinDatas) {
            flattenHairpinWeight(hairpinData);
        }
    }

    private void flattenHairpinWeight(HairpinData hairpinData) {
        final double hairpinScore = hairpinData.getHairpinScore();
        // TODO: maybe use -0.00001 and 0.00001 instead of 0 and 0 below
        if (-0.5 < hairpinScore && hairpinScore < 0) {
            hairpinData.setHairpinScore(-0.5);
        }
        else if (0 < hairpinScore && hairpinScore < 0.5) {
            hairpinData.setHairpinScore(0.5);
        }
    }

    /**
     * Sorts {@link HairpinData HairpinDatas} by {@link HairpinData#getHairpinScore()} in ascending order
     */
    static final class HairpinScoreAscendingComparator implements Comparator<HairpinData> {
        public int compare(HairpinData hairpinData1, HairpinData hairpinData2) {
            final double hairpin1Score = hairpinData1.getHairpinScore();
            final double hairpin2Score = hairpinData2.getHairpinScore();
            return Double.compare(hairpin1Score, hairpin2Score);
        }
    }

    private void initializeGeneNameToGeneDataMap() {
        geneNameToGeneDataMap = new HashMap<String,GeneData>();
        for (int i = 0; i < orderedHairpinDatas.length; i++) {
            final HairpinData hairpinData = orderedHairpinDatas[i];
            final String geneName = hairpinData.getGeneName();
            GeneData geneData = geneNameToGeneDataMap.get(geneName);
            if (geneData == null) {
                geneData = new GeneData();
                geneData.setGeneName(geneName);
                geneNameToGeneDataMap.put(geneName, geneData);
            }
            geneData.addHairpinData(hairpinData);
        }
    }
    
    private void initializeGeneSetSizeToGeneDatasMap() {
        geneSetSizeToGeneDatasMap = new HashMap<Integer,Set<GeneData>>();
        for (GeneData geneData : geneNameToGeneDataMap.values()) {
            final int geneSetSize = geneData.getHairpinDatas().size();
            Set<GeneData> geneDatas = geneSetSizeToGeneDatasMap.get(geneSetSize);
            if (geneDatas == null) {
                geneDatas = new HashSet<GeneData>();
                geneSetSizeToGeneDatasMap.put(geneSetSize, geneDatas);
            }
            geneDatas.add(geneData);
        }
    }

    private void checkAllGeneSetSizesGreaterThanOne() {
        Set<GeneData> geneDatas = geneSetSizeToGeneDatasMap.get(1);
        if (geneDatas != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("the following genes have just one hairpin: ");
            for (GeneData geneData : geneDatas) {
                stringBuilder.append(geneData.getGeneName()).append(" ");
            }
            stringBuilder.append("\nplease exclude these from your input file.");
            throw new RigerJInputException(stringBuilder.toString());
        }
    }

    private void computeGeneScoresAndPValues() {
        for (Map.Entry<Integer,Set<GeneData>> mapEntry : geneSetSizeToGeneDatasMap.entrySet()) {
            final int geneSetSize = mapEntry.getKey();
            final Set<GeneData> geneDatas = mapEntry.getValue();
            computeGeneScoresAndPValues(geneSetSize, geneDatas);
        }
    }

    private void computeGeneScoresAndPValues(final int geneSetSize,
                                             final Set<GeneData> geneDatas) {
        final double[] sortedRandomScores = computeSortedRandomGeneScoresForGeneSetSize(geneSetSize);
        final GeneScoreAdjuster geneSetSizeGeneScoreAdjuster = hairpinSetScoringAlgorithm.computeGeneSetSizeAdjustment(sortedRandomScores);
        for (GeneData geneData : geneDatas) {
            computeScoreAndPValueForGene(geneData, sortedRandomScores, geneSetSizeGeneScoreAdjuster);
        }
    }

    private double[] computeSortedRandomGeneScoresForGeneSetSize(final int geneSetSize) {
        final double[] randomScores = new double[numRandomScoresPerGeneSetSize];
        for (int i = 0; i < numRandomScoresPerGeneSetSize; i++) {
            OrderedHairpinScoresSubset subset = pickRandomHairpinScoresForGeneSetSize(geneSetSize);
            randomScores[i] = hairpinSetScoringAlgorithm.scoreHairpinSet(orderedHairpinDatas.length,
                                                                         subset.targetHairpinScores,
                                                                         subset.targetHairpinScoreIndexes,
                                                                         subset.targetHairpinWeights,
                                                                         rigerInputs.getAlpha());
        }
        Arrays.sort(randomScores);
        return randomScores;
    }

    private OrderedHairpinScoresSubset pickRandomHairpinScoresForGeneSetSize(final int geneSetSize) {
        OrderedHairpinScoresSubset subset = new OrderedHairpinScoresSubset();
        subset.targetHairpinScores = new double[geneSetSize];
        subset.targetHairpinScoreIndexes = new int[geneSetSize];
        subset.targetHairpinWeights = new double[geneSetSize];

        Set<Integer> pickedHairpinScoreIndexes = new HashSet<Integer>();

        // be sure to pick _without_ replacement
        while (pickedHairpinScoreIndexes.size() < geneSetSize) {

            // TODO: the majority of time spent by this algorithm in in this call to #nextInt, regardless of
            // hairpin set scoring method selected. any further performance improvements should focus on this
            int hairpinScoreIndex = randomNumberGenerator.nextInt(orderedHairpinDatas.length);

            if (pickedHairpinScoreIndexes.add(hairpinScoreIndex)) {
                final int subsetIndex = pickedHairpinScoreIndexes.size() - 1;
                subset.targetHairpinScores[subsetIndex] = orderedHairpinDatas[hairpinScoreIndex].getHairpinScore();
                subset.targetHairpinScoreIndexes[subsetIndex] = hairpinScoreIndex;
                subset.targetHairpinWeights[subsetIndex] = orderedHairpinDatas[hairpinScoreIndex].getHairpinWeight();
            }
        }

        return subset;
    }
    
    private static final class OrderedHairpinScoresSubset {
        double[] targetHairpinScores;
        int[] targetHairpinScoreIndexes;
        double[] targetHairpinWeights;
    }

    private void computeScoreAndPValueForGene(final GeneData geneData,
                                              final double[] sortedRandomScores,
                                              final GeneScoreAdjuster geneSetSizeGeneScoreAdjuster) {
        
        OrderedHairpinScoresSubset subset = getHairpinScoresForGeneData(geneData);
        double geneScore = hairpinSetScoringAlgorithm.scoreHairpinSet(orderedHairpinDatas.length,
                                                                      subset.targetHairpinScores,
                                                                      subset.targetHairpinScoreIndexes,
                                                                      subset.targetHairpinWeights,
                                                                      rigerInputs.getAlpha());

        if (rigerInputs.adjustForHairpinSetSize()) {
            final double adjustedGeneScore = geneSetSizeGeneScoreAdjuster.adjustGeneScore(geneScore);
            geneData.setGeneScore(adjustedGeneScore);
        }
        else {
            geneData.setGeneScore(geneScore);
        }
    
        int numBetterRandomScores = computeNumberBetterRandomScores(geneScore, sortedRandomScores);
        double pValue = (double) numBetterRandomScores / sortedRandomScores.length;
        geneData.setPValue(pValue);
    }

    private OrderedHairpinScoresSubset getHairpinScoresForGeneData(final GeneData geneData) {
        int geneSetSize = geneData.getHairpinDatas().size();
        OrderedHairpinScoresSubset subset = new OrderedHairpinScoresSubset();
        subset.targetHairpinScores = new double[geneSetSize];
        subset.targetHairpinScoreIndexes = new int[geneSetSize];
        subset.targetHairpinWeights = new double[geneSetSize];

        for (int i = 0; i < geneSetSize; i++) {
            HairpinData hairpinData = geneData.getHairpinDatas().get(i);
            subset.targetHairpinScores[i] = hairpinData.getHairpinScore();
            
            // the following -1 adjustment assumes the ranks in the input file are 1-based
            subset.targetHairpinScoreIndexes[i] = hairpinData.getHairpinRank() - 1;

            subset.targetHairpinWeights[i] = hairpinData.getHairpinWeight();
        }
        return subset;
    }

    private int computeNumberBetterRandomScores(double geneScore,
                                                final double[] sortedRandomScores) {
        int randomScoresInsertionPoint = Arrays.binarySearch(sortedRandomScores, geneScore);
        if (randomScoresInsertionPoint >= 0) {
            
            // an exact match was found in randomScores.
            // we need to make sure we find the highest indexed random score with this score
            // (i.e., pick the rightmost insertion point)
            while (randomScoresInsertionPoint + 1 < sortedRandomScores.length &&
                sortedRandomScores[randomScoresInsertionPoint + 1] == geneScore) {
                randomScoresInsertionPoint++;
            }
        }
        else {
    
            // no exact match was found. read the javadocs for java.util.Arrays to make sense of this:
            
            // if -1, insert point is 0, and we should go to -1
            // if -2, insert point is 1, and we should go to 0
            // if -3, insert point is 2, and we should go to 1
            // if -length, insert point is length-1, and we should go to length-2
            // if -length-1, insert point is length, and we should go to length-1

            randomScoresInsertionPoint = -1 * randomScoresInsertionPoint - 1;
        }

        if (hairpinSetScoringAlgorithm.lowScoresRankFirst()) {
            return randomScoresInsertionPoint + 1;
        }
        else {
            return sortedRandomScores.length - randomScoresInsertionPoint - 1;
        }
    }

    /**
     * the best gene score gets ranked 1, etc
     */
    private GeneData[] computeGeneRanks() {
        GeneData[] geneDatas = geneNameToGeneDataMap.values().toArray(new GeneData[0]);
        Comparator<GeneData> geneDataComparator =
            hairpinSetScoringAlgorithm.lowScoresRankFirst() ?
                new GeneScoreAscendingComparator() : new GeneScoreDescendingComparator();
        Arrays.sort(geneDatas, geneDataComparator);
        for (int i = 0; i < geneDatas.length; i++) {
            geneDatas[i].setGeneRank(i + 1);
        }
        return geneDatas;
    }

    /**
     * Sorts {@link GeneData GeneDatas} by {@link GeneData#getGeneScore()} in descending order
     */
    static final class GeneScoreDescendingComparator implements Comparator<GeneData> {
        public int compare(GeneData geneData1, GeneData geneData2) {
            final double gene1Score = geneData1.getGeneScore();
            final double gene2Score = geneData2.getGeneScore();
            return Double.compare(gene2Score, gene1Score);
        }
    }

    /**
     * Sorts {@link GeneData GeneDatas} by {@link GeneData#getGeneScore()} in ascending order
     */
    static final class GeneScoreAscendingComparator implements Comparator<GeneData> {
        public int compare(GeneData geneData1, GeneData geneData2) {
            final double gene1Score = geneData1.getGeneScore();
            final double gene2Score = geneData2.getGeneScore();
            return Double.compare(gene1Score, gene2Score);
        }
    }

    /**
     * the lowest p-value gets ranked 1, etc
     */
    private GeneData[] computePValueRanks() {
        GeneData[] geneDatas = geneNameToGeneDataMap.values().toArray(new GeneData[0]);
        Arrays.sort(geneDatas, new PValueAscendingComparator());
        for (int i = 0; i < geneDatas.length; i++) {
            geneDatas[i].setPValueRank(i + 1);
        }
        return geneDatas;
    }

    /**
     * Sorts {@link GeneData GeneDatas} by {@link GeneData#getPValue()} in ascending order
     */
    static final class PValueAscendingComparator implements Comparator<GeneData> {
        public int compare(GeneData geneData1, GeneData geneData2) {
            final double gene1PValue = geneData1.getPValue();
            final double gene2PValue = geneData2.getPValue();
            return Double.compare(gene1PValue, gene2PValue);
        }
    }

    private RigerOutputs buildRigerOutputs(final GeneData[] geneDatasSortedByPValueRank) {
        return new RigerOutputs() {

            public int getNumGenes() {
                return geneDatasSortedByPValueRank.length;
            }           

            public GeneOutput getGeneOutput(int i) {
                return geneDatasSortedByPValueRank[i];
            } 
        };
    }
}
