package io.sadeq.algorithms;

import io.sadeq.datastructures.Item;
import io.sadeq.datastructures.ProblemInstance;
import net.jcip.annotations.Immutable;

import java.math.BigDecimal;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

@Immutable
public final class BruteForce extends AbstractSolver {
    public BruteForce(final ProblemInstance problemInstance) {
        super(problemInstance);
    }

    protected SortedSet<Integer> solve(final ProblemInstance problemInstance) {
        final List<Item> items = problemInstance.getItems();
        final int count = items.size();

        SortedSet<Integer> solution = new TreeSet<>();
        BigDecimal maxPrice = BigDecimal.ZERO;
        BigDecimal itsWeight = BigDecimal.ZERO;

        for (long i = 0; i < (1L << count); i++) {
            BigDecimal totalWeight = new BigDecimal(0);
            BigDecimal totalPrice = new BigDecimal(0);
            SortedSet<Integer> subset = new TreeSet<>();


            for (int j = 0; j < count; j++) {
                long mask = (i & (1L << j));
                Item item = items.get(j);
                BigDecimal next = totalWeight.add(item.getWeight());
                if (mask != 0 && next.compareTo(problemInstance.getMaxWeight()) <= 0) {
                    subset.add(item.getNumber());
                    totalWeight = next;
                    totalPrice = totalPrice.add(item.getPrice());
                }
            }
            if ((maxPrice.compareTo(totalPrice) < 0) ||
                    (maxPrice.compareTo(totalPrice) == 0 && itsWeight.compareTo(totalWeight) > 0)) {
                maxPrice = totalPrice;
                itsWeight = totalWeight;
                solution = subset;
            }
        }

        return solution;
    }
}