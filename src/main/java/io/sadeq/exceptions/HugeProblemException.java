package io.sadeq.exceptions;

public class HugeProblemException extends RuntimeException {
    private static final long serialVersionUID = 1429313029444819610L;

    public HugeProblemException(String msg) {
        super(msg);
    }
}
