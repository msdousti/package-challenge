package io.sadeq.algorithms;

import io.sadeq.datastructures.Bag;
import io.sadeq.datastructures.Item;
import io.sadeq.datastructures.ProblemInstance;
import io.sadeq.exceptions.HugeProblemException;
import io.sadeq.exceptions.ItemException;
import io.sadeq.exceptions.LineFormatException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;

import static org.junit.jupiter.api.Assertions.*;

class DynamicProgrammingTest {
    private static final Logger logger = LoggerFactory.getLogger(DynamicProgramming.class);

    static Random r = new Random();

    @Test
    void testDpSolve() throws ItemException {
        List<Item> items = List.of(
                new Item(1, "1, 5.3, €10"),
                new Item(2, "2, 4,   €40"),
                new Item(3, "3, 6.7, €30"),
                new Item(4, "4, 3.5, €50")
        );

        SortedSet<Integer> indices = DynamicProgramming.dpSolve(BigDecimal.valueOf(10), 1, items);
        assertEquals(List.of(2, 4), new ArrayList<>(indices));
    }

    @Test
    void testDpSolveEdgeCase() throws ItemException {
        List<Item> items = List.of(
                new Item(1, "1, 6.1, €10"),
                new Item(2, "2, 7, €10"),
                new Item(3, "3, 5, €10"),
                new Item(4, "4, 3, €50")
        );

        SortedSet<Integer> indices = DynamicProgramming.dpSolve(BigDecimal.valueOf(10), 1, items);
        assertEquals(List.of(3, 4), new ArrayList<>(indices));
    }

    @Test
    void testHuge() throws LineFormatException {
        ProblemInstance p = new ProblemInstance(1, "99.8135: (1, 5.33, €10)");
        assertThrows(HugeProblemException.class, () ->
                new DynamicProgramming(p));
    }

    @ParameterizedTest
    @MethodSource("io.sadeq.TestCaseSources#randomSource")
    void randomCase(final int lineNo, final String line) {
        logger.trace("{} --- {}\n", lineNo, line);

        ProblemInstance p = assertDoesNotThrow(() -> new ProblemInstance(lineNo, line));
        BruteForce bf = new BruteForce(p);
        DynamicProgramming dp = new DynamicProgramming(p);

        Bag bfBag = bf.getBag();
        Bag dpBag = dp.getBag();

        logger.trace("Brute force result:      " + bfBag);
        logger.trace("Dynamic programming result:   " + dpBag);

        BigDecimal bfPrice = bfBag.getResultPrice();
        BigDecimal dpPrice = dpBag.getResultPrice();

        assertEquals(bfPrice, dpPrice);
    }
}