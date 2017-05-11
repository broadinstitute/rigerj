// rigerj:org.broadinstitute.gpp.rigerj.api.RigerJInputException

package org.broadinstitute.gpp.rigerj.api;

/**
 * This exception is thrown when unrecoverable errors are encountered in the inputs to
 * {@link RigerAlgorithm}.
 *
 * @author <a href="http://www.broadinstitute.org/genetic-perturbation-platform">Broad Institute, Genetic Perturbation Platform</a>
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
