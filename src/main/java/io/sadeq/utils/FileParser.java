package io.sadeq.utils;

import io.sadeq.datastructures.ProblemInstance;
import io.sadeq.exceptions.FileFormatException;
import io.sadeq.exceptions.FormatException;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.sadeq.Configs.FILE_ENCODING;
import static io.sadeq.Configs.MAX_FILE_SIZE_BYTES;

/**
 * This class loads the input file, and reads it line-by-line.
 * Each line is passed to an instance of the class {@link ProblemInstance}
 * to create a problem instance.
 */
@Immutable
public class FileParser {
    private static final Logger logger = LoggerFactory.getLogger(FileParser.class);

    private final Path path;

    /**
     * @param path Fully qualified path to the input file
     */
    public FileParser(final String path) {
        this.path = Paths.get(path);
    }

    /**
     * This method parses the input file specified by {@code path}
     * to the constructor. It returns a list of instances of the class
     * {@link ProblemInstance}.
     *
     * @return A list of problem instances
     * @throws IOException         if an I/O error occurs
     * @throws FileFormatException if file is larger than
     *                             {@code MAX_FILE_SIZE_BYTES}, or file is empty
     */
    public List<ProblemInstance> parse() throws IOException, FileFormatException {
        final long size = Files.size(path);
        if (size > MAX_FILE_SIZE_BYTES)
            throw new FileFormatException(
                    String.format("Input file size = %d, which is greater than the maximum allowable size %d",
                            size, MAX_FILE_SIZE_BYTES));

        List<String> strLines = Files.readAllLines(path, FILE_ENCODING);

        if (strLines.isEmpty())
            throw new FileFormatException("File is empty.");

        /*
         * Use an IntStream to traverse the lines in the file.
         * For each line, the line number and the line itself
         * are passed to ProblemInstance constructor.
         */
        return IntStream.range(0, strLines.size()).mapToObj(i -> {
            try {
                return new ProblemInstance(i + 1, strLines.get(i));
            } catch (FormatException e) {
                logger.warn("{} This line was discarded.", e.getMessage());
                return null;
            }
        }).collect(Collectors.toList());
    }
}
