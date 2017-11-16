package com.andymur.pg.generators.source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;

/**
 * Created by andymur on 11/11/17.
 */
public class FileSource<T> implements Source<T> {

    private static final int DEFAULT_BUFFER_SIZE = 65535;
    private static final String DEFAULT_DELIMITER = "\n";

    //TODO: use charset
    private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();

    private File sourceFile;
    private List<T> elements = new ArrayList<>();
    private Set<T> uniqueElements = new HashSet<>();
    private final String delimiter;

    private Function<String, T> transformer;

    public FileSource(File sourceFile) {
        this(DEFAULT_DELIMITER, sourceFile);
    }

    @SuppressWarnings("unchecked")
    public FileSource(String delimiter, File sourceFile) {
        this(x -> (T) x, DEFAULT_DELIMITER, sourceFile);
    }

    public FileSource(Function<String, T> transformer, String delimiter, File sourceFile) {
        this.transformer = transformer;
        this.delimiter = delimiter;
        this.sourceFile = sourceFile;
    }

    @Override
    public List<T> readSource() throws IOException {
        return (List<T>) read(elements);
    }

    @Override
    public Set<T> readSourceUnique() throws IOException {
        return (Set<T>) read(uniqueElements);
    }

    private Collection<T> read(Collection<T> elements) throws IOException {
        elements.clear();

        CharBuffer buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);

        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFile))) {
            int size = 0;
            String prefix = "";

            while ((size = reader.read(buffer)) != -1) {

                String rawStr = prefix.concat(new String(Arrays.copyOfRange(buffer.array(), 0, size)));

                String[] rawParts = rawStr.split(delimiter);
                prefix = rawParts[rawParts.length - 1];

                if (rawStr.endsWith(delimiter)) {
                    prefix = prefix.concat(delimiter);
                }

                T[] parts = transform(Arrays.copyOfRange(rawParts, 0, rawParts.length - 1));

                Arrays.stream(parts).forEach(
                        elements::add
                );

                buffer.clear();
            }

            elements.add(transformer.apply(prefix));
        }

        return elements;
    }

    @SuppressWarnings("unchecked")
    private T[] transform(String[] parts) {
        T[] result = (T[]) new Object[parts.length];
        int i = 0;

        for (String part: parts) {
            result[i++] = transformer.apply(part);
        }

        return result;
    }

    public static class FileSourceBuilder<T> {

        private Function<String, T> transformerFunction;
        private String delimiter;
        private Path pathToFile;

        public static <T> FileSourceBuilder<T> of() {
            return new FileSourceBuilder<>();
        }

        public FileSourceBuilder<T> withPath(Path path) {
            this.pathToFile = path;
            return this;
        }

        public FileSourceBuilder<T> withPath(String pathToFile) {
            this.pathToFile = Paths.get(pathToFile);
            return this;
        }

        public FileSourceBuilder<T> withDelimiter(String delimiter) {
            //TODO: add check for null in one style everywhere
            this.delimiter = delimiter;
            return this;
        }

        public FileSourceBuilder<T> withTransformerFunction(Function<String, T> transformerFunction) {
            this.transformerFunction = transformerFunction;
            return this;
        }

        public FileSource<T> build() {

            if (pathToFile == null) {
                throw new IllegalStateException("Path to File should be provided");
            }

            if (delimiter == null) {
                delimiter = DEFAULT_DELIMITER;
            }

            if (transformerFunction == null) {
                transformerFunction = x -> (T) x;
            }

            return new FileSource<>(transformerFunction, delimiter, pathToFile.toFile());
        }

    }
}
