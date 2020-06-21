package io.sadeq.utils;

import io.sadeq.datastructures.ProblemInstance;
import io.sadeq.exceptions.FormatException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.sadeq.Configs.MAX_ITEMS_PER_LINE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProblemInstanceTest {
    private static final transient Logger logger = LoggerFactory.getLogger(ProblemInstanceTest.class);

    @ParameterizedTest
    @MethodSource("io.sadeq.TestCaseSources#badItems")
    void checkBadInstances(final int index, final String line) {
        assertThrows(FormatException.class, ()
                -> new ProblemInstance(index, "100:(" + line + ")"));
    }

    @Test
    void checkEdgeCases() {
        Exception ex;
        ex = assertThrows(FormatException.class, () -> new ProblemInstance(0, null));
        logger.trace("{}", ex.getMessage());
        ex = assertThrows(FormatException.class, () -> new ProblemInstance(0, ""));
        logger.trace("{}", ex.getMessage());
        ex = assertThrows(FormatException.class, () -> new ProblemInstance(0, "1000:(1,1,€2)"));
        logger.trace("{}", ex.getMessage());
        ex = assertThrows(FormatException.class, () -> new ProblemInstance(0, "X:(1,1,€2)"));
        logger.trace("{}", ex.getMessage());
        ex = assertThrows(FormatException.class, () -> new ProblemInstance(0, "100:(0,1,€2)"));
        logger.trace("{}", ex.getMessage());
        ex = assertThrows(FormatException.class, () -> new ProblemInstance(0, "100:(1,1,€2"));
        logger.trace("{}", ex.getMessage());

        StringBuilder sb = new StringBuilder("100:");
        for (int i = 1; i <= MAX_ITEMS_PER_LINE + 1; i++) {
            sb.append("(")
                    .append(i)
                    .append(",2,€3)");
        }

        ex = assertThrows(FormatException.class, () -> new ProblemInstance(0, sb.toString()));
        logger.trace("{}", ex.getMessage());
    }

    @ParameterizedTest
    @MethodSource("io.sadeq.TestCaseSources#givenTestCase")
    void checkValidLine(final int lineNo, final String line) {
        assertDoesNotThrow(() -> new ProblemInstance(lineNo, line));
    }
}
