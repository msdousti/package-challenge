package io.sadeq.algorithms;

import io.sadeq.datastructures.Bag;
import io.sadeq.datastructures.ProblemInstance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GreedyApproximationTest {
    private static final Logger logger = LoggerFactory.getLogger(GreedyApproximationTest.class);


    static final BigDecimal TWO = BigDecimal.valueOf(2);

    @ParameterizedTest
    @MethodSource("io.sadeq.TestCaseSources#givenTestCase")
    void greedySolve(final int lineNo, final String line, final String solution) {
        ProblemInstance p = assertDoesNotThrow(() -> new ProblemInstance(lineNo, line));

        SortedSet<Integer> sol;
        if (solution.equals("-"))
            sol = null;
        else
            sol = Arrays.stream(solution.split("\\s*,\\s*"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toCollection(TreeSet::new));

        Bag bag = new Bag(p.getMap(), sol);

        GreedyApproximation ga = new GreedyApproximation(p);

        if (sol != null)
            assertTrue(ga.getBag().getResultPrice().compareTo(bag.getResultPrice().multiply(TWO)) < 0);


        logger.trace("Price:  approx. {} vs real {}\n", ga.getBag().getResultPrice(), bag.getResultPrice());
        logger.trace("Weight: approx. {} vs real {}\n", ga.getBag().getResultWeight(), bag.getResultWeight());
    }

    @Test
    void greedyEdgeCase() throws Exception {
        ProblemInstance p = new ProblemInstance(1, "10 : (1, 1, €1) (2, 10, €9)");
        GreedyApproximation ga = new GreedyApproximation(p);
        assertEquals(BigDecimal.valueOf(9), ga.getBag().getResultPrice());
        logger.trace(ga.getBag().toString());
    }

    @ParameterizedTest
    @MethodSource("io.sadeq.TestCaseSources#randomSource")
    void randomCase(final int lineNo, final String line) {
        logger.trace("{} --- {}\n", lineNo, line);

        ProblemInstance p = assertDoesNotThrow(() -> new ProblemInstance(lineNo, line));
        BruteForce bf = new BruteForce(p);
        GreedyApproximation ga = new GreedyApproximation(p);

        Bag bfBag = bf.getBag();
        Bag gaBag = ga.getBag();

        logger.trace("Brute force result:      " + bfBag);
        logger.trace("Greedy approx. result:   " + gaBag);

        BigDecimal bfPrice = bfBag.getResultPrice();
        BigDecimal gaPrice = gaBag.getResultPrice();

        if (!bfPrice.equals(BigDecimal.ZERO))
            assertTrue(bfPrice.compareTo(gaPrice.multiply(TWO)) < 0);
    }
}