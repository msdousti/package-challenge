package io.sadeq.algorithms;

import io.sadeq.datastructures.Bag;
import io.sadeq.datastructures.ProblemInstance;
import lombok.Getter;

import java.util.SortedSet;

/**
 * The {@code Solver} class provides a base class for various
 * algorithms implementing the solution to the given problem.
 * It has an {@code abstract} method called {@code solve}.
 * Various algorithms which want to solve the problem can
 * implement this method.
 */
@Getter
public abstract class AbstractSolver {
    private final Bag bag;

    /**
     * The constructor receives an instance of ProblemInstance,
     * calls solve on it, and initializes the field {@code bag}
     * upon receiving the response.
     *
     * @param problemInstance An instance of the problem
     */
    AbstractSolver(final ProblemInstance problemInstance) {
        if (problemInstance == null) {
            bag = null;
            return;
        }
        SortedSet<Integer> sortedSolution = solve(problemInstance);
        bag = new Bag(problemInstance.getMap(), sortedSolution);
    }

    @Override
    public final String toString() {
        return (bag == null) ? "ERR" : bag.getResult();
    }

    /**
     * Solves the package problem for the given instance.
     *
     * @param problemInstance An instance of the problem
     * @return A sorted set containing the indices of items in the solution
     */
    protected abstract SortedSet<Integer> solve(final ProblemInstance problemInstance);
}
