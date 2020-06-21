package io.sadeq.algorithms;

import io.sadeq.datastructures.Item;
import io.sadeq.datastructures.ProblemInstance;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @see <a href="http://ac.informatik.uni-freiburg.de/lak_teaching/ws11_12/combopt/notes/knapsack.pdf">
 * "Chapter 4. Knapsack" by Alexander Souza
 * </a>
 */

@Immutable
public final class GreedyApproximation extends AbstractSolver {
    private static final Logger logger = LoggerFactory.getLogger(GreedyApproximation.class);

    public GreedyApproximation(final ProblemInstance problemInstance) {
        super(problemInstance);
    }

    protected SortedSet<Integer> solve(final ProblemInstance problemInstance) {
        final List<Item> sorted = problemInstance.getItems().stream()
                .sorted(ItemComparators.efficiencyPrice.reversed())
                .collect(Collectors.toUnmodifiableList());

        logger.trace("sorted = {}.", sorted);

        final SortedSet<Integer> indices = new TreeSet<>();
        BigDecimal weight = BigDecimal.ZERO;
        BigDecimal price = BigDecimal.ZERO;

        BigDecimal maxPrice = BigDecimal.ZERO;
        int maxLabel = -1;

        for (Item item : sorted) {
            BigDecimal tmpWeight = weight.add(item.getWeight());
            if (tmpWeight.compareTo(problemInstance.getMaxWeight()) <= 0) {
                weight = tmpWeight;
                indices.add(item.getNumber());
                price = price.add(item.getPrice());
            }
            if (maxPrice.compareTo(item.getPrice()) < 0) {
                maxLabel = item.getNumber();
                maxPrice = item.getPrice();
            }
        }

        logger.trace("breakPrice = {}.", maxPrice);
        logger.trace("breakNumber = {}.", maxLabel);
        logger.trace("Price = {}.", price);
        logger.trace("Solution = {}.", indices);

        if (maxPrice.compareTo(price) > 0)
            return new TreeSet<>(Collections.singleton(maxLabel));

        return indices;
    }

}
