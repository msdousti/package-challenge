package io.sadeq.algorithms;

import io.sadeq.datastructures.Bag;
import io.sadeq.datastructures.ProblemInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BranchAndBoundTest {
    private static final Logger logger = LoggerFactory.getLogger(BranchAndBoundTest.class);

    @ParameterizedTest
    @MethodSource("io.sadeq.TestCaseSources#randomSource")
    void randomCase(final int lineNo, final String line) {
        logger.trace("{} --- {}\n", lineNo, line);

        ProblemInstance p = assertDoesNotThrow(() -> new ProblemInstance(lineNo, line));
        BruteForce bf = new BruteForce(p);
        BranchAndBound bb = new BranchAndBound(p);

        Bag bfBag = bf.getBag();
        Bag bbBag = bb.getBag();

        logger.trace("Brute force result:      " + bfBag);
        logger.trace("Branch & bound result:   " + bbBag);

        BigDecimal bfPrice = bfBag.getResultPrice();
        BigDecimal bbPrice = bbBag.getResultPrice();

        assertEquals(bfPrice, bbPrice);
    }
}