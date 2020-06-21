package io.sadeq.exceptions;

public class LineFormatException extends FormatException {
    private static final long serialVersionUID = -7697971610531769720L;

    public LineFormatException(int lineNo, String msg) {
        super(String.format("Line #%d: Error - %s.", lineNo, msg));
    }

    public LineFormatException(int lineNo, ItemException e) {
        super(String.format("Line #%d, item %d: Error - %s.", lineNo, e.getItemNo(), e.getMsg()));
    }
}
