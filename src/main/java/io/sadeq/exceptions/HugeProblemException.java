package io.sadeq.exceptions;

import net.jcip.annotations.Immutable;

/**
 * Exception that is thrown if the problem instance is
 * greater than some value specified in the {@code Configs} class.
 */
@Immutable
public class HugeProblemException extends RuntimeException {
    private static final long serialVersionUID = 1429313029444819610L;

    public HugeProblemException(final String msg) {
        super(msg);
    }
}
