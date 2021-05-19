package com.andymur.pg.generators.source;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CsvFileSourceTest {

    @Test
    public void testReadSource() throws IOException {
        Path path = Paths.get("src/test/resources/test_csv_source.csv");
        File inputFile = path.toFile();

        CsvFileSource<Integer> fileSource= new CsvFileSource<>(Integer::valueOf, 1,  ",", "\"", inputFile);
        List<Integer> result = fileSource.readSource();
        System.out.println(result);
        assertEquals(10, result.size());
        assertEquals(Arrays.asList(234, 1, 32, 4, 9, 33, 3, 9, -1, 3), result);
    }
}