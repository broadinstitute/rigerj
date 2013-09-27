// org.broadinstitute.rnai.rigerj.RigerJMain

package org.broadinstitute.rnai.rigerj;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.broadinstitute.rnai.rigerj.api.GeneOutput;
import org.broadinstitute.rnai.rigerj.api.HairpinInput;
import org.broadinstitute.rnai.rigerj.api.HairpinSetScoringMethod;
import org.broadinstitute.rnai.rigerj.api.RigerInputs;
import org.broadinstitute.rnai.rigerj.api.RigerJInputException;
import org.broadinstitute.rnai.rigerj.api.RigerOutputs;
import org.broadinstitute.rnai.rigerj.impl.HairpinData;
import org.broadinstitute.rnai.rigerj.impl.RigerImpl;

/**
 * Command line utility for running "RigerJ".
 *
 * @author <a href="http://www.broad.mit.edu/rnai_platform">Broad Institute, RNAi Platform</a>
 */
public class RigerJMain {

    private static final String VERSION_STRING =
        "RigerJ 1.7.0\n\n" +
        "For usage instructions, see https://www.broadinstitute.org/twiki/bin/view/RNAiplatform/RigerJ";

    private static final String INPUT_HEADERS_WITH_WEIGHTS = "Construct\tTarget\tNormalizedScore\tConstruct Rank\tConstruct Weight";
    private static final String INPUT_HEADERS_WITHOUT_WEIGHTS = "Construct\tTarget\tNormalized Score\tConstruct Rank";
    private static final String OUTPUT_HEADERS = "Gene Rank\tGene Name\tScore\tp-value\tp-value Rank\tConstruct Ranks";
    
    private static final String DEFAULT_SCORING_METHOD = HairpinSetScoringMethod.KOLMOGOROV_SMIRNOV.getParameterName();
    private static final boolean DEFAULT_FLATTEN_WEIGHTS = true;
    private static final int DEFAULT_NUM_RANDOM_GENES_PER_SET_SIZE = 10000;
    private static final double DEFAULT_ALPHA = 1.0;
    private static final long DEFAULT_RANDOM_SEED = new Date().getTime();
    private static final boolean DEFAULT_ADJUST_FOR_HAIRPIN_SET_SIZE = true;

    private static CommandLine commandLine;

    public static void main(String[] args) throws Exception {
        initializeCommandLine(args);
        respondToHelpRequest();
        runRigerJ();
    }

    private static void initializeCommandLine(String[] args) throws ParseException {
        final Options options = initializeOptions();
        commandLine = parseCommandLine(args, options);
    }
    
    @SuppressWarnings("static-access")
    private static Options initializeOptions() {
        Options options = new Options();
        options.addOption(OptionBuilder
                          .withLongOpt("help")
                          .hasArg(false)
                          .isRequired(false)
                          .create());
        options.addOption(OptionBuilder
                          .withLongOpt("version")
                          .hasArg(false)
                          .isRequired(false)
                          .create());
        options.addOption(OptionBuilder
                          .withLongOpt("inputFile")
                          .hasArg(true)
                          .withType(String.class)
                          .isRequired(false)
                          .create());
        options.addOption(OptionBuilder
                          .withLongOpt("scoringMethod")
                          .hasArg(true)
                          .withType(String.class)
                          .isRequired(false)
                          .create());
        options.addOption(OptionBuilder
                          .withLongOpt("flattenWeights")
                          .hasArg(true)
                          .withType(Number.class)
                          .isRequired(false)
                          .create());
        options.addOption(OptionBuilder
                          .withLongOpt("numRandomScoresPerGeneSetSize")
                          .hasArg(true)
                          .withType(Number.class)
                          .isRequired(false)
                          .create());
        options.addOption(OptionBuilder
                          .withLongOpt("alpha")
                          .hasArg(true)
                          .withType(Number.class)
                          .isRequired(false)
                          .create());
        options.addOption(OptionBuilder
                          .withLongOpt("randomSeed")
                          .hasArg(true)
                          .withType(Number.class)
                          .isRequired(false)
                          .create());
        options.addOption(OptionBuilder
                          .withLongOpt("outputFile")
                          .hasArg(true)
                          .withType(String.class)
                          .isRequired(false)
                          .create());
        options.addOption(OptionBuilder
                          .withLongOpt("adjustForHairpinSetSize")
                          .hasArg(true)
                          .withType(Number.class)
                          .isRequired(false)
                          .create());
        return options;
    }

    private static CommandLine parseCommandLine(final String[] args,
                                                final Options options) throws ParseException {
        CommandLineParser commandLineParser = new GnuParser();
        return commandLineParser.parse(options, args, true);
    }

    private static void respondToHelpRequest() {
        if (commandLine.hasOption("help") || commandLine.hasOption("version")) {
            System.out.println(VERSION_STRING);
            System.exit(0);
        }
    }

    private static void runRigerJ() throws Exception {
        final RigerInputs rigerInputs = buildRigerInputs();
        final RigerImpl rigerImpl = new RigerImpl();
        try {
            final RigerOutputs rigerOutputs = rigerImpl.execute(rigerInputs);
            writeRigerOutputsToFile(rigerOutputs);
        }
        catch (RigerJInputException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static RigerInputs buildRigerInputs() throws Exception {
        final List<HairpinData> hairpinDatas = createHairpinDatas();
        final HairpinSetScoringMethod hairpinSetScoringMethod = getHairpinSetScoringMethod();
        final boolean flattenWeights = getFlattenWeights();
        final int numRandomScoresPerGeneSetSize = getNumRandomGenesPerSetSize();
        final double alpha = getAlpha();
        final long randomSeed = getRandomSeed();
        final boolean adjustForHaipinSetSize = getAdjustForHaipinSetSize();

        return new RigerInputs() {
            public int getNumHairpins() {
                return hairpinDatas.size();
            }
            public HairpinInput getHairpinInput(int i) {
                return hairpinDatas.get(i);
            }
            public HairpinSetScoringMethod getHairpinSetScoringMethod() {
                return hairpinSetScoringMethod;
            }
            public boolean flattenWeights() {
                return flattenWeights;
            }
            public int getNumRandomScoresPerGeneSetSize() {
                return numRandomScoresPerGeneSetSize;
            }
            public double getAlpha() {
                return alpha;
            }
            public long getRandomSeed() {
                return randomSeed;
            }
            public boolean adjustForHairpinSetSize() {
                return adjustForHaipinSetSize;
            }
        };
    }

    private static List<HairpinData> createHairpinDatas() throws IOException {
        BufferedReader bufferedReader = getBufferedReader();
        parseHeaders(bufferedReader);
        List<HairpinData> hairpinDatas = new ArrayList<HairpinData>();
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            String[] cells = line.split("\t", 0);
            hairpinDatas.add(new HairpinData(new Integer(cells[3]),
                                             cells[0],
                                             new Double(cells[2]),
                                             cells[1],
                                             cells.length == 5 ? new Double(cells[4]) : 1));
            
        }
        return hairpinDatas;
    }

    private static BufferedReader getBufferedReader() throws FileNotFoundException {
        String inputFilename = commandLine.getOptionValue("inputFile");
        if (inputFilename != null) {
            return new BufferedReader(new FileReader(inputFilename));
        }
        else {
            return new BufferedReader(new InputStreamReader(System.in));            
        }
    }

    private static void parseHeaders(BufferedReader bufferedReader) throws IOException {
        String headers = bufferedReader.readLine();
        if (!headers.equals(INPUT_HEADERS_WITH_WEIGHTS) &&
            !headers.equals(INPUT_HEADERS_WITHOUT_WEIGHTS)) {
            System.err.println("header line mismatch. " +
                               "expected: " + INPUT_HEADERS_WITH_WEIGHTS + "\n" +
                               "or: " + INPUT_HEADERS_WITHOUT_WEIGHTS + "\n" +
                               "got: " + headers);
            System.exit(1);
        }
    }

    private static HairpinSetScoringMethod getHairpinSetScoringMethod() {
        String scoringMethodParameter = commandLine.getOptionValue("scoringMethod");
        if (scoringMethodParameter == null) {
            scoringMethodParameter = DEFAULT_SCORING_METHOD;
        }
        for (HairpinSetScoringMethod method : HairpinSetScoringMethod.values()) {
            if (method.getParameterName().equals(scoringMethodParameter)) {
                return method;
            }
        }
        System.err.println("unrecognized scoring method " + scoringMethodParameter);
        System.exit(1);
        return null;
    }

    private static boolean getFlattenWeights() throws ParseException {
        final Number flattenWeights = (Number) commandLine.getParsedOptionValue("flattenWeights");
        if (flattenWeights == null) {
            return DEFAULT_FLATTEN_WEIGHTS;
        }
        return flattenWeights.intValue() != 0;
    }

    private static int getNumRandomGenesPerSetSize() throws ParseException {
        final Number numRandomGenesperSetSize = (Number) commandLine.getParsedOptionValue("numRandomScoresPerGeneSetSize");
        if (numRandomGenesperSetSize == null) {
            return DEFAULT_NUM_RANDOM_GENES_PER_SET_SIZE;
        }
        return numRandomGenesperSetSize.intValue();
    }

    private static double getAlpha() throws ParseException {
        final Number alpha = (Number) commandLine.getParsedOptionValue("alpha");
        if (alpha == null) {
            return DEFAULT_ALPHA;
        }
        return alpha.doubleValue();
    }

    private static long getRandomSeed() throws ParseException {
        final Number randomSeed = (Number) commandLine.getParsedOptionValue("randomSeed");
        if (randomSeed == null) {
            return DEFAULT_RANDOM_SEED;
        }
        return randomSeed.longValue();
    }

    private static boolean getAdjustForHaipinSetSize() throws ParseException {
        final Number adjustForHairpinSetSize = (Number) commandLine.getParsedOptionValue("adjustForHairpinSetSize");
        if (adjustForHairpinSetSize == null) {
            return DEFAULT_ADJUST_FOR_HAIRPIN_SET_SIZE;
        }
        return adjustForHairpinSetSize.intValue() != 0;
    }

    private static void writeRigerOutputsToFile(final RigerOutputs rigerOutputs) throws Exception {
        PrintStream printStream = getPrintStream();
        printStream.println(OUTPUT_HEADERS);
        for (int i = 0; i < rigerOutputs.getNumGenes(); i++) {
            GeneOutput geneOutput = rigerOutputs.getGeneOutput(i);
            printStream.println(geneOutput.getGeneRank() + "\t" +
                                      geneOutput.getGeneName() + "\t" +
                                      geneOutput.getGeneScore() + "\t" +
                                      geneOutput.getPValue() + "\t" +
                                      geneOutput.getPValueRank() + "\t" +
                                      geneOutput.getHairpinRanks());
        }
        printStream.close();
    }

    private static PrintStream getPrintStream() throws FileNotFoundException {
        String outputFilename = commandLine.getOptionValue("outputFile");
        if (outputFilename != null) {
            return new PrintStream(outputFilename);
        }
        else {
            return new PrintStream(System.out);
        }
    }
}
