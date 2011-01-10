// rigerj:org.broadinstitute.rnai.rigerj.api.RigerJInputException

package org.broadinstitute.rnai.rigerj.api;

/**
 * This exception is thrown when unrecoverable errors are encountered in the inputs to
 * {@link RigerAlgorithm}.
 *
 * @author <a href="http://www.broad.mit.edu/rnai_platform">Broad Institute, RNAi Platform</a>
 */
@SuppressWarnings("serial")
public class RigerJInputException extends RuntimeException {

    public RigerJInputException() {
        super();
    }

    public RigerJInputException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public RigerJInputException(String arg0) {
        super(arg0);
    }

    public RigerJInputException(Throwable arg0) {
        super(arg0);
    }
}
