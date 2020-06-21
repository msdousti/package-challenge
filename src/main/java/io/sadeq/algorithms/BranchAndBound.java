package io.sadeq.algorithms;

import io.sadeq.datastructures.Item;
import io.sadeq.datastructures.ProblemInstance;
import lombok.Getter;
import net.jcip.annotations.Immutable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static io.sadeq.Configs.SCALE;

@Immutable
public final class BranchAndBound extends AbstractSolver {

    private BigDecimal maxWeight;
    private List<Item> sorted;
    private Queue<Node> queue;
    private BigDecimal bestPrice;
    private BigDecimal bestWeight;
    private SortedSet<Integer> bestLabels;

    public BranchAndBound(final ProblemInstance problemInstance) {
        super(problemInstance);
    }

    @Override
    protected SortedSet<Integer> solve(final ProblemInstance problemInstance) {

        sorted = problemInstance.getItems().stream()
                .sorted(ItemComparators.efficiencyPrice.reversed())
                .collect(Collectors.toUnmodifiableList());

        maxWeight = problemInstance.getMaxWeight();
        queue = new PriorityQueue<>(Node.boundPriceWeight.reversed());

        Node dummy = new Node();
        queue.add(dummy);
        bestPrice = BigDecimal.ZERO;

        while (!queue.isEmpty()) {
            Node parent = queue.poll();

            if (parent.bound.compareTo(bestPrice) < 0
                    || parent.level >= sorted.size() - 1) {
                continue;
            }

            // What if we leave the ith item in sorted least?
            processChild(parent, true);

            // What if we take the ith item in sorted least?
            Node n = processChild(parent, false);

            if (n != null) {
                final int cmp = n.price.compareTo(bestPrice);
                if ((cmp > 0) || (cmp == 0 && n.weight.compareTo(bestWeight) < 0)) {
                    bestPrice = n.price;
                    bestWeight = n.weight;
                    bestLabels = n.labels;
                }
            }
        }

        return bestLabels;
    }

    private Node processChild(final Node parent, final boolean leaveChild) {
        final int i = parent.level + 1;
        final BigDecimal weightToRoot;
        final BigDecimal priceToRoot;
        final SortedSet<Integer> labelsToRoot;

        if (leaveChild) {
            weightToRoot = parent.weight;
            priceToRoot = parent.price;
            labelsToRoot = parent.labels;
        } else {
            final Item item = sorted.get(i);
            weightToRoot = parent.weight.add(item.getWeight());

            if (weightToRoot.compareTo(maxWeight) > 0)
                return null;

            labelsToRoot = new TreeSet<>(parent.labels);
            labelsToRoot.add(item.getNumber());
            priceToRoot = parent.price.add(item.getPrice());
        }

        BigDecimal bound = priceToRoot.add(computeBound(i + 1, weightToRoot));
        if (bound.compareTo(bestPrice) >= 0) {
            final Node n = new Node(i, weightToRoot, priceToRoot, bound, labelsToRoot);
            queue.add(n);
            return n;
        }

        return null;
    }

    BigDecimal computeBound(final int start, final BigDecimal currentWeight) {
        BigDecimal maxPrice = BigDecimal.ZERO;
        BigDecimal remainingWeight = maxWeight.subtract(currentWeight);

        for (int i = start; i < sorted.size(); i++) {
            Item item = sorted.get(i);
            BigDecimal price = item.getPrice();
            final BigDecimal weight = item.getWeight();

            if (weight.compareTo(remainingWeight) > 0) {
                BigDecimal fraction = remainingWeight.divide(weight, SCALE, RoundingMode.HALF_UP);
                price = price.multiply(fraction);
                maxPrice = maxPrice.add(price);
                break;
            }
            remainingWeight = remainingWeight.subtract(weight);
            maxPrice = maxPrice.add(price);
        }

        return maxPrice;
    }

    @Immutable
    static final class Node {
        @Getter
        final BigDecimal weight;
        @Getter
        final BigDecimal price;
        @Getter
        final BigDecimal bound;

        final int level;
        final SortedSet<Integer> labels;

        Node() {
            level = -1;
            weight = price = bound = BigDecimal.ZERO;
            labels = Collections.unmodifiableSortedSet(new TreeSet<>());
        }

        public Node(int level, BigDecimal weight, BigDecimal price, BigDecimal bound, SortedSet<Integer> labels) {
            this.level = level;
            this.weight = weight;
            this.price = price;
            this.bound = bound;
            this.labels = Collections.unmodifiableSortedSet(new TreeSet<>(labels));
        }

        static final Comparator<Node> boundPriceWeight =
                Comparator.comparing(Node::getBound)
                        .thenComparing(Node::getPrice)
                        .thenComparing(Node::getWeight, Comparator.reverseOrder());
    }
}

