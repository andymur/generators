package com.andymur.pg.generators.dest;

import com.andymur.pg.generators.scalar.IntGenerator;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDestinationTest {

    @Test
    public void testWrite() throws IOException {
        IntGenerator generator = IntGenerator
                .IntGeneratorBuilder
                .of()
                .from(10)
                .build();

        Path path = Paths.get("src/test/resources/test_destination.txt");
        File outputFile = path.toFile();

        FileDestination<Integer> fileDestination = new FileDestination<>(outputFile);

        fileDestination.write(generator);
        Files.delete(path);
    }
}