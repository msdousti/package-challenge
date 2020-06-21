package io.sadeq;

import io.sadeq.algorithms.AbstractSolver;
import io.sadeq.algorithms.BranchAndBound;
import io.sadeq.exceptions.FileFormatException;
import io.sadeq.utils.FileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main entry point to program
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args) throws IOException, FileFormatException {
        if (args.length != 1) {
            logger.error("Please give the correct path to test cases as an argument.");
            return;
        }
        FileParser fp = new FileParser(args[0]);

        /*
         * The default solver is BranchAndBound, but
         * it can be changed here. Also, notice the use of
         * parallelStream() for further efficiency
         */
        List<AbstractSolver> lst = fp.parse().parallelStream()
                .map(BranchAndBound::new)
                .collect(Collectors.toList());

        logger.info("------");
        for (AbstractSolver solver : lst)
            logger.info("{}", solver);
    }
}
