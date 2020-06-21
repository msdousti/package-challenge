package io.sadeq.exceptions;

import lombok.Getter;

public class ItemException extends FormatException {
    private static final long serialVersionUID = -7697971610531769720L;

    @Getter
    private final int itemNo;

    @Getter
    private final String msg;

    public ItemException(int itemNo, String msg) {
        super(String.format("Item #%d: Error - %s.", itemNo, msg));
        this.itemNo = itemNo;
        this.msg = msg;
    }
}
