package io.sadeq;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 *
 */
public class Configs {
    private Configs() {
    }

    public static final Charset FILE_ENCODING = StandardCharsets.UTF_8;
    public static final long MAX_FILE_SIZE_BYTES = 1_000_000;

    public static final int MAX_ITEMS_PER_LINE = 15;
    public static final BigDecimal MAX_PACKAGE_WEIGHT = BigDecimal.valueOf(100);
    public static final BigDecimal MAX_ITEM_WEIGHT = BigDecimal.valueOf(100);
    public static final BigDecimal MAX_ITEM_PRICE = BigDecimal.valueOf(100);

    /**
     * How many digits to keep digits after the decimal point,
     * when dividing numbers together?
     */
    public static final int SCALE = 8;

    public static final int MAX_INT_WEIGHT_FOR_DP = 10000;
}
