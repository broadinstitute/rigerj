package org.broadinstitute.rnai.rigerj.impl;

import org.junit.Test;

import java.util.Arrays;
//import java.util.Random;

/**
 * @author <a href="http://www.broadinstitute.org/rnai_platform">Broad Institute, RNAi Platform</a>
 */
public class RigerImplUnitTest {
    // This block is used to generate test data randomly that causes the code to fail
    /*
    @Test
    public void testSort() {
        HairpinData[] data = null;
        try {
            final Random r = new Random();
            for(int i = 1; i < 10001; i++) {
                data = new HairpinData[i];
                for (int j = 0; j < i; j++) {
                    if (r.nextInt() % 9 == 0) {
                        data[j] = new HairpinData(1, "TRCNAN3", Double.NaN, "eGFP.1", 1.0);
                    } else {
                        data[j] = new HairpinData(1, "TRCNAN3", r.nextDouble(), "eGFP.1", 1.0);
                    }
                }
                HairpinData data2[] = Arrays.copyOf(data, data.length);
                Arrays.sort(data2, new RigerImpl.HairpinScoreAscendingComparator());
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            if (data != null) {
                System.out.println("HairpinData[] data = new HairpinData[] {");
                for(int i = 0; i < data.length; i++) {
                // this will not print the NaNs correctly, but you can add them back manually
                    System.out.println("new HairpinData(1, \"TRCN000000067\", " + data[i].getHairpinScore() + ", \"eGFP.1\", 1.0),");
                }
                System.out.println("};");
            }
        }
    }
    */

    @Test
    public void testHairpinScoreAscendingComparator() {
        HairpinData[] data = new HairpinData[] {
                new HairpinData(1, "TRCN000000067", 0.5332725345472525, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.7433432208106739, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", Double.NaN, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.9316403916815215, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", Double.NaN, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.02208746951584728, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.3495953753569008, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.49230910157641083, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.5522122534184788, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.8922030398308332, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.018232029638316827, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", Double.NaN, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.962203224921013, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.24752903146203376, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.06135413398423695, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.6687031800808563, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.859578422958404, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.2931460943924964, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.15788364244117614, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.8010727545459533, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.6233140928171158, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.35747046463748045, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.03945092446258125, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", Double.NaN, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.7918246422151793, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", Double.NaN, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.14276685926359778, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.4388221537141448, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.014082214761943157, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.6403423947106557, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.8364662236191689, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.31892482977968484, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.7140001828742707, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.22848353186148962, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.5458281231720978, "eGFP.1", 1.0),
                new HairpinData(1, "TRCN000000067", 0.30223706028669883, "eGFP.1", 1.0),
        };
        Arrays.sort(data, new RigerImpl.HairpinScoreAscendingComparator());

        // make sure the hairpin data is sorted ascendingly
        double d1 = data[0].getHairpinScore();
        for (final HairpinData datum : data) {
            final double d2 = datum.getHairpinScore();
            if (Double.isNaN(d2)) {
                continue;
            }
            else {
                assert(d1 <= d2);
                d1 = d2;
            }
        }
    }

    @Test
    public void testGeneScoreComparators() {
        double[] scores = new double[] { 1.0, Double.NaN, -3.1, 0.2, 23.0, Double.NaN, 0.1119, 7.22 };
        GeneData[] data = createGeneDataFromScores(scores);
        Arrays.sort(data, new RigerImpl.GeneScoreAscendingComparator());
        assertAscending(getScoresFromGeneData(data));
        Arrays.sort(data, new RigerImpl.GeneScoreDescendingComparator());
        assertDescending(getScoresFromGeneData(data));
    }

    @Test
    public void testGenePValueComparator() {
        double[] pvals = new double[] { 288.32, -17.255, 1.0, Double.NaN, 0.2, 23.0, Double.NaN, 0.1119, 7.22 };
        GeneData[] data = createGeneDataFromPValues(pvals);
        Arrays.sort(data, new RigerImpl.PValueAscendingComparator());
        assertAscending(getPValuesFromGeneData(data));
    }

    private void assertAscending(double[] data) {
        double d1 = data[0];
        for (final double d2 : data) {
            if (Double.isNaN(d1) || Double.isNaN(d2)) {
                continue;
            } else {
                assert d1 <= d2: String.format("%f should be <= %f", d1, d2);
                d1 = d2;
            }
        }
    }

    private void assertDescending(double[] data) {
        double d1 = data[0];
        for (final double d2 : data) {
            if (Double.isNaN(d1) || Double.isNaN(d2)) {
                continue;
            } else {
                assert d1 >= d2: String.format("%f should be >= %f", d1, d2);
                d1 = d2;
            }
        }
    }

    private GeneData createGeneDataFromScore(double score) {
        GeneData gd = new GeneData();
        gd.geneRank = 1;
        gd.geneName = "ABC";
        gd.geneScore = score;
        gd.pValue = 0.05;
        gd.pValueRank = 1;
        return gd;
    }

    private GeneData createGeneDataFromPValue(double pval) {
        GeneData gd = new GeneData();
        gd.geneRank = 1;
        gd.geneName = "ABC";
        gd.geneScore = 0.123;
        gd.pValue = pval;
        gd.pValueRank = 1;
        return gd;
    }

    private GeneData[] createGeneDataFromScores(double[] scores) {
        GeneData[] gds = new GeneData[scores.length];
        for (int i = 0; i < gds.length; i++) {
            gds[i] = createGeneDataFromScore(scores[i]);
        }
        return gds;
    }

    private GeneData[] createGeneDataFromPValues(double[] pvals) {
        GeneData[] gds = new GeneData[pvals.length];
        for (int i = 0; i < gds.length; i++) {
            gds[i] = createGeneDataFromPValue(pvals[i]);
        }
        return gds;
    }

    private double[] getScoresFromGeneData(GeneData[] gds) {
        double[] scores = new double[gds.length];
        for (int i = 0; i < gds.length; i++) {
            scores[i] = gds[i].getGeneScore();
        }
        return scores;
    }

    private double[] getPValuesFromGeneData(GeneData[] gds) {
        double[] pvals = new double[gds.length];
        for (int i = 0; i < gds.length; i++) {
            pvals[i] = gds[i].getPValue();
        }
        return pvals;
    }

}
