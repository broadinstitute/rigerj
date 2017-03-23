[![Build Status](https://travis-ci.com/broadinstitute/rigerj.svg?token=N3vsppoZquQSGZBMS1Fh&branch=master)](https://travis-ci.com/broadinstitute/rigerj)

README last updated: 3/23/2017

rigerj
======

RigerJ - a Java implementation of various "hairpin-set" enichment methods, used in RNAi screening analysis.


--- RIGER and GENE-E ---

Riger can also be used as a plugin for the analysis software GENE-E, found here:

http://www.broadinstitute.org/cancer/software/GENE-E/

Follow the instructions under the Extentions link to install and run RIGER as a GENE-E extension.

--- Command Line Usage ---

Running RigerJ from the command line will vary slightly depending on what platform you are running on, 
(e.g., Linux, Windows, Mac OS), and how Java is installed on your machine. But you should be able to 
run it more or less like this:
> java -jar rigerj-1.7.0.jar [options]

All options are optional, and are described below:

--- Options ---

-help                             Prints version and usage information, and exits
-version                          Prints version and usage information, and exits
-inputFile                        The name of the input file to read.  Defaults to STDIN.  The input
                                  file format is described below.
-scoringMethod                    The scoring method to use.  Defaults to KSbyScore.  Available
                                  scoring methods are described below.
-flattenWeights                   If non-zer, scored between -0.5 and 0 are converted to -0.5; scores
                                  between 0 and 0.5 are converted to 0.5; scores of 0 are left as-is.
                                  Only applicable to gene-score scoring methods.  Defaults to true.
-numRandomScoresPerGeneSetSize    The number of random scores to computer per gene set size.  This
                                  affects the quality of the computed p-values.  A minimum size of
                                  10 000 is required.  Defaults to 10 000.
-alpha                            A scoring factor used by KSbyScore that I don't completely
                                  understand.  Ask Shuba (shuba@braodinstitute.org) for more details.
                                  Defaults to 1.0.
-randomSeed                       The seed used to generate random gene sets.  Using the same value here
                                  should cause the p-value results to be exactly the same.  Defaults
                                  to a number generated from the current time.
-outputFile                       The name of the output file to write.  Defaults to STDOUT.  The 
                                  output file format is described below.
-adjustForHairpinSetSize          If non-zero, scores for genes are adjusted according to a normalizing
                                  factor for its hairpin set size.  Defaults for true.
                                  
--- Available Scoring Methods ---

Scoring methods fall into two categories: gene-score based scoring methods; and gene-rank based scoring
methods. Currently, one gene-score based method (KSbyScore) and two gene-rank based methods (WtSum and 
SecondBestRank) are available.

* KSbyScore - Short for "KS by Score", this is the Kolmogorov-Smirnov algorithm, modified for hairpin set enrichment.

* WtSum - Short for "Weighted Sum", this method computes the score as .75 times the second best rank plus .25 
times the best rank.

* Second Best Rank - This method scores the gene as the rank of the second best ranking hairpin for the gene.

--- Notes on Scoring and Statistical Methods ---

The following descriptions of how the scoring and statistical methods are computed within RIGER were adapted by 
answers provided by Shuba Gopal, and may be useful to describe these methods to laypersons.

Weighted Sum (WtSum): rank orders the observed scores for shRNAs for a given gene and then heavily weights 
the first and second highest ranking shRNAs before computing a sum of all the shRNA scores (see description above).

Second Best: similar to Weighted Sum, but takes the score of the second highest ranking shRNA as the score for
a given gene.

Kolmogorov-Smirnov (KSbyScore): computes a cumulative score based on rank ordering *all* the shRNAs in the pool, 
regardless of gene mapping.  Then starting at the top of the rank ordering and moving sequentially down the list, 
for each shRNA that maps to the gene of interest, adds the score of the shRNA to a running sum.  For each shRNA 
in the rank ordered list that is not mapped to the gene of interest, you subtract a fraction of its score from 
the running sum.  This essentially scores genes based on whether all their shRNAs score consistently at 
the top of the rank ordering as very strong hits and genes with a more mixed profile of some high scoring and 
some moderate scoring shRNAs as weaker hits.

p-value calculation: calculated by creating a null distribution. This involves scrambling the mapping of shRNAs to genes, 
and then randomly assembling sets of shRNAs to map to each gene.  For each score from a real mapping of shRNAs to a gene, 
the score is compared to all the scores from the scrambled mapping of shRNAs to genes.  The proportion of scores in the 
scrambled set that are as good as or better than the observed real score is used to compute the p-value.

--- Input File Format ---

The input file format is a little stringent, requiring exactly the following column headers, in the given 
order, separated by tabs:

* Construct
  * The name of the hairpin.
* GeneSymbol
  * A unique name for the gene.
* NormalizedScore
  * The hairpin score.
* Construct Rank
  * The hairpin rank.
* HairpinWeight
  * A weight to apply to the hairpin. Should be between 0 and 1. 0 means completely discount the hairpin.
    1 means give it full weight. This is currently only used by KSbyScore scoring algorithm.

Unfortunately, you will have to provide the hairpin ranks yourself.

--- Output File Format ---
RigerJ outputs the data with the following column headers:

* Gene Rank
  * The Gene rank.
* Gene Name
  * Gene Name, as provided in input file.
* Score
  * Gene based score.
* p-value
  * The calculated p-value.
* p-value rank
  * Rank of the p-value for the gene.
* Hairpin ranks
  * List of the individual hairpin ranks for given gene.

--- Release History ___

* RigerJ 1.0 - Initial Java implementation of RIGER. Release date circa 2009-11-25.
* RigerJ 1.1 - Fixed bug where low gene ranks were given high p-values for 2nd-best and weighted-sum 
               scoring methods. Release date circa 2009-12-17.
* RigerJ 1.2 - Added some code to bulletproof against the occurrence of a gene in the input file with just 
               a single hairpin. This would previously cause ArrayIndexOutOfBounds exceptions for 
               SecondBestRank and WeightedSum scoring methods. Now it causes a more user-friendly error, 
               regardless of scoring method. Release date circa 2010-03-02.
* RigerJ 1.3 - Enhanced to account for number of hairpins per gene in scoring. Release date circa 2010-03-24.
* RigerJ 1.4 - Fixed bug where we were failing to adjust for 1-based rank indices in the input files. 
               Release date circa 2010-03-29.
* RigerJ 1.5b - Experimental extension to RIGER to apply weights to the hairpins. Original beta 2010-05-14.
* RigerJ 1.6 - Fix bug where -adjustForHairpinSetSize command line option was not working.
               Fix off-by-one bug for gene scores with WtSum and!SecondBestRank scoring methods.
* RigerJ 1.7 - Internal changes to address testability issues for JDK8
