package com.andymur.pg.generators.source;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileSourceTest {

    @Test
    public void testReadSource() throws IOException {
        Path path = Paths.get("src/test/resources/test_source.txt");
        File inputFile = path.toFile();

        FileSource<Integer> fileSource= new FileSource<>(x -> Integer.valueOf(x.trim()), ",", inputFile);
        List<Integer> result = fileSource.readSource();

        assertEquals(11, result.size());
        assertEquals(Arrays.asList(1, 2, 3 , 4 , 5 , 6, 7, 8, 9, 10, 1), result);
    }

    @Test
    public void testReadSourceUnique() throws IOException {
        Path path = Paths.get("src/test/resources/test_source.txt");
        File inputFile = path.toFile();

        FileSource<Integer> fileSource= new FileSource<>(x -> Integer.valueOf(x.trim()), ",", inputFile);
        Set<Integer> result = fileSource.readSourceUnique();

        assertEquals(10, result.size());
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3 , 4 , 5 , 6, 7, 8, 9, 10)), result);
    }

}