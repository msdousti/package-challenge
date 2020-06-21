package io.sadeq;

import io.sadeq.algorithms.AbstractSolver;
import io.sadeq.algorithms.BruteForce;
import io.sadeq.exceptions.FileFormatException;
import io.sadeq.utils.FileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException, FileFormatException {
        if (args.length != 1) {
            logger.error("Please give the correct path to test cases as an argument.");
            return;
        }
        FileParser fp = new FileParser(args[0]);
        List<AbstractSolver> lst = fp.parse().parallelStream()
                .map(BruteForce::new)
                .collect(Collectors.toList());

        logger.info("------");
        for (AbstractSolver solver : lst)
            logger.info("{}", solver);
    }
}
