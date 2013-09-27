import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.broadinstitute.rnai.rigerj.api.GeneOutput;
import org.broadinstitute.rnai.rigerj.api.HairpinInput;
import org.broadinstitute.rnai.rigerj.api.HairpinSetScoringMethod;
import org.broadinstitute.rnai.rigerj.api.RigerInputs;
import org.broadinstitute.rnai.rigerj.api.RigerOutputs;
import org.broadinstitute.rnai.rigerj.impl.HairpinData;
import org.broadinstitute.rnai.rigerj.impl.RigerImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Integration tests for {@link RigerImpl}.
 * 
 * <p>
 * 
 * I don't have time to write up full documentation for this test suite, but here are some hints:
 * 
 * <ul>
 * <li>If you want to add a new test, you will want to be adding a call to {@link #addTestParams} in method
 * {@link #initializeTestParamsList}.
 * <li>If a test is broken due to a change in RigerImpl that is not actually a bug, you simply need to fix the test.
 * You will want to focus on the second and third line of input parameters to {@link #addTestParams}. Uncomment
 * the call to {@link #dumpFirstAndLast(RigerOutputs)} to generate code snips you can cut and paste into the test
 * for the second and third lines of input params.
 * </ul>
 *
 * @author <a href="http://www.broad.mit.edu/rnai_platform">Broad Institute, RNAi Platform</a>
 */
@RunWith(Parameterized.class)
public class RigerImplTest {

    private static final String TEST_INPUT_FILENAME = "test/inputFile.txt";
    private static final String INPUT_HEADERS = "Construct\tTarget\tNormalized Score\tConstruct Rank";
    private static final long RANDOM_SEED = 1003;

    private static List<HairpinInput> hairpinInputs;
    private static Collection<Object[]> testParamsList;
    
    static {
        try {
            initializeHairpinInputs();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        initializeTestParamsList();
    }

    private static void initializeHairpinInputs() throws IOException {
        hairpinInputs = new ArrayList<HairpinInput>();
        BufferedReader bufferedReader = getBufferedReader();
        parseHeaders(bufferedReader);
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            String[] cells = line.split("\t", 0);
            hairpinInputs.add(new HairpinData(new Integer(cells[3]),
                                              cells[0],
                                              new Double(cells[2]),
                                              cells[1],
                                              1));
        }
    }

    private static BufferedReader getBufferedReader() throws FileNotFoundException {
        return new BufferedReader(new FileReader(TEST_INPUT_FILENAME));
    }

    private static void parseHeaders(BufferedReader bufferedReader) throws IOException {
        String headers = bufferedReader.readLine();
        if (! headers.equals(INPUT_HEADERS)) {
            System.out.println("header line mismatch. " +
                               "expected: " + INPUT_HEADERS + "\n" +
                               "got: " + headers);
            System.exit(1);
        }
    }

    private static void initializeTestParamsList() {
        testParamsList = new ArrayList<Object[]>();
        
        // adjustForHairpinSetSize
        addTestParams(true, 1.0, HairpinSetScoringMethod.KOLMOGOROV_SMIRNOV, 10000, RANDOM_SEED, true,
                      "SFRS2", 3.276132782650146, 1, -1.0E-4, 48, "991 1188 1280 1285 1371 1495 2386 2722 4934 5700 5985 6860 7484 8754 9471 9791 12989 14118 16937 17327 17735 20735 21343 21763 21944 22604 23861 24848 29370 40777 41843 ",
                      "MXRA5", -1.8018072138310683, 9480, 0.9998, 9480, "24143 30533 43076 44960 44961 ");
        addTestParams(true, 1.0, HairpinSetScoringMethod.WEIGHTED_SUM, 10000, RANDOM_SEED, true,
                      "EIF5B", 2.2433890130023088E-4, 1, 1.0E-4, 4, "3 7 91 39242 ",
                      "LPXN", 1.7934964358004049, 9480, 1.0001, 9480, "38968 41356 42093 44005 44855 ");
        addTestParams(true, 1.0, HairpinSetScoringMethod.SECOND_BEST_RANK, 10000, RANDOM_SEED, true,
                      "EIF5B", 2.2893772893772894E-4, 1, 1.0E-4, 3, "3 7 91 39242 ",
                      "SLC16A6", 1.6563798676312145, 9480, 1.0001, 9476, "16965 43046 43981 44091 44794 ");

        // !adjustForHairpinSetSize
        addTestParams(true, 1.0, HairpinSetScoringMethod.KOLMOGOROV_SMIRNOV, 10000, RANDOM_SEED, false,
                      "EIF5B", 0.96912, 1, -1.0E-4, 28, "3 7 91 39242 ",
                      "CHST6", -0.98941, 9480, 0.9994, 9479, "44475 44843 ");
        addTestParams(true, 1.0, HairpinSetScoringMethod.WEIGHTED_SUM, 10000, RANDOM_SEED, false,
                      "EIF5B", 6.0, 1, 1.0E-4, 4, "3 7 91 39242 ",
                      "CHST6", 44751.0, 9480, 0.9998, 9476, "44475 44843 ");
        addTestParams(true, 1.0, HairpinSetScoringMethod.SECOND_BEST_RANK, 10000, RANDOM_SEED, false,
                      "EIF5B", 7.0, 1, 1.0E-4, 3, "3 7 91 39242 ",
                      "CHST6", 44843.0, 9480, 0.9944, 9409, "44475 44843 ");
    }

    private static void addTestParams(boolean flattenWeights,
                                      double alpha,
                                      HairpinSetScoringMethod hairpinSetScoringMethod,
                                      int numRandomScoresPerGeneSetSize,
                                      long randomSeed,
                                      boolean adjustForHaipinSetSize,
                                      String firstGeneName,
                                      double firstGeneScore,
                                      int firstGeneRank,
                                      double firstPValue,
                                      int firstPValueRank,
                                      String firstHairpinRanks,
                                      String lastGeneName,
                                      double lastGeneScore,
                                      int lastGeneRank,
                                      double lastPValue,
                                      int lastPValueRank,
                                      String lastHairpinRanks
                                      ) {
        testParamsList.add(new Object[] { new RigerInputsImpl(flattenWeights,
                                                              alpha,
                                                              hairpinSetScoringMethod,
                                                              numRandomScoresPerGeneSetSize,
                                                              randomSeed,
                                                              adjustForHaipinSetSize),
                                          new GeneOutputImpl(firstGeneName,
                                                             firstGeneScore,
                                                             firstGeneRank,
                                                             firstPValue,
                                                             firstPValueRank,
                                                             firstHairpinRanks),
                                          new GeneOutputImpl(lastGeneName,
                                                             lastGeneScore,
                                                             lastGeneRank,
                                                             lastPValue,
                                                             lastPValueRank,
                                                             lastHairpinRanks)
                                        });
    }
    
    private static class RigerInputsImpl implements RigerInputs {

        private boolean flattenWeights;
        private double alpha;
        private HairpinSetScoringMethod hairpinSetScoringMethod;
        private int numRandomScoresPerGeneSetSize;
        private long randomSeed;
        private boolean adjustForHaipinSetSize;
        
        public RigerInputsImpl(boolean flattenWeights,
                               double alpha,
                               HairpinSetScoringMethod hairpinSetScoringMethod,
                               int numRandomScoresPerGeneSetSize,
                               long randomSeed,
                               boolean adjustForHaipinSetSize) {
            this.flattenWeights = flattenWeights;
            this.alpha = alpha;
            this.hairpinSetScoringMethod = hairpinSetScoringMethod;
            this.numRandomScoresPerGeneSetSize = numRandomScoresPerGeneSetSize;
            this.randomSeed = randomSeed;
            this.adjustForHaipinSetSize = adjustForHaipinSetSize;
        }

        public boolean flattenWeights() {
            return flattenWeights;
        }

        public double getAlpha() {
            return alpha;
        }

        public HairpinSetScoringMethod getHairpinSetScoringMethod() {
            return hairpinSetScoringMethod;
        }

        public int getNumRandomScoresPerGeneSetSize() {
            return numRandomScoresPerGeneSetSize;
        }

        public long getRandomSeed() {
            return randomSeed;
        }

        public HairpinInput getHairpinInput(int i) {
            return hairpinInputs.get(i);
        }

        public int getNumHairpins() {
            return hairpinInputs.size();
        }

        public boolean adjustForHairpinSetSize() {
            return adjustForHaipinSetSize;
        }
    }

    private static class GeneOutputImpl implements GeneOutput {

        private String geneName;
        private double geneScore;
        private int geneRank;
        private double pValue;
        private int pValueRank;
        private String hairpinRanks;

        public GeneOutputImpl(String geneName, double geneScore, int geneRank,
                              double pValue, int pValueRank, String hairpinRanks) {
            this.geneName = geneName;
            this.geneScore = geneScore;
            this.geneRank = geneRank;
            this.pValue = pValue;
            this.pValueRank = pValueRank;
            this.hairpinRanks = hairpinRanks;
        }

        public String getGeneName() {
            return geneName;
        }

        public double getGeneScore() {
            return geneScore;
        }

        public int getGeneRank() {
            return geneRank;
        }

        public double getPValue() {
            return pValue;
        }

        public int getPValueRank() {
            return pValueRank;
        }

        public String getHairpinRanks() {
            return hairpinRanks;
        }
    }

    @Parameters
    public static Collection<Object[]> testParamsList() {
        return testParamsList;
    }

    private final RigerImpl rigerImpl;
    private final RigerInputs rigerInputs;
    private final GeneOutput expectedFirstOutput;
    private final GeneOutput expectedLastOutput;

    public RigerImplTest(RigerInputs rigerInputs,
                         GeneOutput expectedFirstOutput,
                         GeneOutput expectedLastOutput) {
        this.rigerImpl = new RigerImpl();
        this.rigerInputs = rigerInputs;
        this.expectedFirstOutput = expectedFirstOutput;
        this.expectedLastOutput = expectedLastOutput;
    }
    
    @Test
    public void testRigerImpl() {
        RigerOutputs outputs = rigerImpl.execute(rigerInputs);

        // uncomment to get some sense of expected test results
        //dumpFirstAndLast(outputs);

        GeneOutput firstOutput = outputs.getGeneOutput(0);
        assertSame(expectedFirstOutput, firstOutput);
        GeneOutput lastOutput = outputs.getGeneOutput(outputs.getNumGenes() - 1);
        assertSame(expectedLastOutput, lastOutput);
    }

    private void assertSame(GeneOutput expectedOutput, GeneOutput actualOutput) {
        assertEquals(expectedOutput.getGeneName(), actualOutput.getGeneName());
        assertEquals(expectedOutput.getGeneScore(), actualOutput.getGeneScore(), 0.001);
        assertEquals(expectedOutput.getGeneRank(), actualOutput.getGeneRank());
        assertEquals(expectedOutput.getPValue(), actualOutput.getPValue(), 0.001);
        assertEquals(expectedOutput.getPValueRank(), actualOutput.getPValueRank());
        assertEquals(expectedOutput.getHairpinRanks(), actualOutput.getHairpinRanks());
    }

    /**
     * This method is used to regenerate code for {@link #initializeTestParamsList()} when
     * {@link RigerImpl} changes in ways that break the tests.
     */
    @SuppressWarnings("unused")
    private void dumpFirstAndLast(RigerOutputs outputs) {
        System.out.println("START DUMP");
        GeneOutput firstOutput = outputs.getGeneOutput(0);
        dumpGeneOutput(firstOutput);
        GeneOutput lastOutput = outputs.getGeneOutput(outputs.getNumGenes() - 1);
        dumpGeneOutput(lastOutput);
        System.out.println("FINIS DUMP");
    }

    private void dumpGeneOutput(GeneOutput geneOutput) {
        System.out.print("\"" + geneOutput.getGeneName() + "\", ");
        System.out.print(geneOutput.getGeneScore() + ", ");
        System.out.print(geneOutput.getGeneRank() + ", ");
        System.out.print(geneOutput.getPValue() + ", ");
        System.out.print(geneOutput.getPValueRank() + ", ");
        System.out.println("\"" + geneOutput.getHairpinRanks() + "\",");
    }
}
