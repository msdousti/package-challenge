package io.sadeq.algorithms;

import io.sadeq.datastructures.Bag;
import io.sadeq.datastructures.ProblemInstance;
import lombok.Getter;

import java.util.SortedSet;

/**
 * The {@code Solver} class provides a base class for various
 * algorithms implementing the solution to the given problem.
 * Its methods have default accessibility rather than being
 * {@code protected}, in accordance with:
 * <p>
 * Effective Java: Item 13, Minimize the accessibility of
 * classes and members
 */

@Getter
public abstract class AbstractSolver {
    private final Bag bag;

    AbstractSolver(final ProblemInstance problemInstance) {
        if(problemInstance == null) {
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

    protected abstract SortedSet<Integer> solve(ProblemInstance problemInstance);
}
