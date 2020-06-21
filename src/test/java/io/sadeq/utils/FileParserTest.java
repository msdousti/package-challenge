package io.sadeq.utils;

import com.google.common.io.Files;
import io.sadeq.datastructures.ProblemInstance;
import io.sadeq.exceptions.FileFormatException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static io.sadeq.Configs.FILE_ENCODING;
import static io.sadeq.TestCaseSources.GIVEN_TEST_CASE;
import static org.junit.jupiter.api.Assertions.*;

public class FileParserTest {
    private static final Logger logger = LoggerFactory.getLogger(FileParser.class);

    public static String setupGoodPath() throws IOException {
        String text = String.join(System.lineSeparator(), GIVEN_TEST_CASE);
        return getTempFile(text);
    }

    @Test
    void testGood() throws IOException {
        String path = setupGoodPath();
        FileParser fileParser = new FileParser(path);
        List<ProblemInstance> problemInstances = assertDoesNotThrow(fileParser::parse);
        assertEquals(4, problemInstances.size());
        problemInstances.forEach(s -> logger.trace("{}", s == null ? "null" : s.getMaxWeight()));
    }

    @Test
    void testEmpty() throws IOException {
        String path = getTempFile(null);
        FileParser fileParser = new FileParser(path);
        assertThrows(FileFormatException.class, fileParser::parse);
    }


    private static String getTempFile(String text) throws IOException {
        File f = File.createTempFile("items-file", ".txt");
        f.deleteOnExit();
        if (text != null)
            Files.asCharSink(f, FILE_ENCODING).write(text);
        return f.getAbsolutePath();
    }

    @Test
    void testBadLine() throws IOException {
        String text = String.join(System.lineSeparator(),
                List.of("DUMMY", "8 : (1,15.3,€34)", "DUMMY"));
        String path = getTempFile(text);
        FileParser fileParser = new FileParser(path);
        List<ProblemInstance> problemInstances = assertDoesNotThrow(fileParser::parse);
        assertEquals(3, problemInstances.size());
        problemInstances.forEach(s -> logger.trace("{}", s == null ? "null" : s.getMaxWeight()));
    }

    @Test
    void testLargeFile() {
        FileParser fileParser = new FileParser("C:\\pagefile.sys");
        assertThrows(FileFormatException.class, fileParser::parse);
    }

}