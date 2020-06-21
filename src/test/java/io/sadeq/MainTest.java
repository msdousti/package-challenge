package io.sadeq;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.sadeq.utils.FileParserTest.setupGoodPath;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MainTest {

    @Test
    void main() throws IOException {
        String[] args1 = new String[0];
        assertDoesNotThrow(() -> Main.main(args1));

        String[] args2 = new String[]{""};
        assertThrows(IOException.class, () -> Main.main(args2));

        String[] args3 = new String[]{setupGoodPath()};
        assertDoesNotThrow(() -> Main.main(args3));
    }
}