package io.sadeq.algorithms;

import io.sadeq.datastructures.Item;
import io.sadeq.datastructures.ProblemInstance;
import io.sadeq.exceptions.HugeProblemException;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static io.sadeq.Configs.MAX_INT_WEIGHT_FOR_DP;

@Immutable
public class DynamicProgramming extends AbstractSolver {
    private static final Logger logger = LoggerFactory.getLogger(DynamicProgramming.class);

    public DynamicProgramming(final ProblemInstance problemInstance) {
        super(problemInstance);
    }

    @Override
    protected SortedSet<Integer> solve(final ProblemInstance problemInstance) {
        final int maxWeightScale = problemInstance.getMaxWeightScale();
        final List<Item> items = problemInstance.getItems();
        return dpSolve(problemInstance.getMaxWeight(), maxWeightScale, items);
    }

    /**
     * @param maxWeight      The maximum weight of items
     * @param maxWeightScale The maximum over the scale of all items,
     *                       as well as {@code maxWeight}
     * @param items          List of items
     * @return A sorted set of indices corresponding to the best items
     */
    static SortedSet<Integer> dpSolve(final BigDecimal maxWeight,
                                      final int maxWeightScale,
                                      final List<Item> items) {

        final BigDecimal multiplier = BigDecimal.TEN.pow(maxWeightScale);
        final int intMaxWeight = toInt(maxWeight, multiplier);

        checkMaxWeight(intMaxWeight);

        final List<Item> sorted = items.stream()
                .sorted(ItemComparators.priceWeight.reversed())
                .collect(Collectors.toUnmodifiableList());

        logger.trace("{}", sorted);

        final BigDecimal[][] priceMemo = new BigDecimal[sorted.size() + 1][intMaxWeight + 1];
        final boolean[][] keep = new boolean[sorted.size() + 1][intMaxWeight + 1];
        final int[] intWeights = new int[sorted.size()];

        for (int i = 0; i < sorted.size(); i++) {
            final Item item = sorted.get(i);
            final BigDecimal price = item.getPrice();
            final int weight = toInt(item.getWeight(), multiplier);
            intWeights[i] = weight;
            for (int j = 0; j <= intMaxWeight; j++) {
                if (priceMemo[i][j] == null)
                    priceMemo[i][j] = BigDecimal.ZERO;
                if (weight > j)
                    priceMemo[i + 1][j] = priceMemo[i][j];
                else {
                    BigDecimal leave = priceMemo[i][j];
                    BigDecimal take = price.add(priceMemo[i][j - weight]);
                    if (take.compareTo(leave) > 0) {
                        priceMemo[i + 1][j] = take;
                        keep[i + 1][j] = true;
                    } else
                        priceMemo[i + 1][j] = leave;
                }
            }
        }

        return findIncluded(intMaxWeight, sorted, intWeights, keep);
    }

    private static void checkMaxWeight(int intMaxWeight) {
        final String description = "This means that the dynamic programming approach will use " +
                "an unacceptable amount of CPU & memory.";

        if (intMaxWeight > MAX_INT_WEIGHT_FOR_DP)
            throw new HugeProblemException(String.format("The integer maximum weight %d exceeds the configured amount %d. %s",
                    intMaxWeight, MAX_INT_WEIGHT_FOR_DP, description));
    }

    static int toInt(final BigDecimal num, final BigDecimal multiplier) {
        return num.multiply(multiplier).intValue();
    }

    static SortedSet<Integer> findIncluded(int maxWeight, List<Item> items, int[] intWeights, boolean[][] keep) {
        int remainingWeight = maxWeight;
        SortedSet<Integer> indices = new TreeSet<>();
        for (int i = items.size(); i >= 1; i--)
            if (keep[i][remainingWeight]) {
                indices.add(items.get(i - 1).getNumber());
                remainingWeight -= intWeights[i - 1];
            }
        return indices;
    }

}
