package com.andymur.pg.generators.source;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Function;

/**
 * Created by andymur on 11/11/17.
 */
public class CsvFileSource<T> implements Source<T> {

    private static final String DEFAULT_DELIMITER = ",";
    private static final String DEFAULT_QUOTE = "\"";
    //TODO add charset
    private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();

    private File sourceFile;
    private final String delimiter;
    private final String quotation;
    private final int columnNum;

    private Function<String, T> transformer;

    private List<T> elements = new ArrayList<>();
    private Set<T> uniqueElements = new HashSet<>();

    @SuppressWarnings("unchecked")
    public CsvFileSource(int columnNum, File sourceFile) {
        this((x) -> (T) x, columnNum, DEFAULT_DELIMITER, DEFAULT_QUOTE, sourceFile);
    }

    @SuppressWarnings("unchecked")
    public CsvFileSource(int columnNum, String delimiter, File sourceFile) {
        this((x) -> (T) x, columnNum, delimiter, DEFAULT_QUOTE, sourceFile);
    }

    public CsvFileSource(Function<String, T> transformer, int columnNum, String delimiter, String quotation, File sourceFile) {
        this.delimiter = delimiter;
        this.quotation = quotation;
        this.columnNum = columnNum;
        this.sourceFile = sourceFile;
        this.transformer = transformer;
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

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(sourceFile))) {
            String line;

            while((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(delimiter);

                String element = parts[columnNum].trim();
                elements.add(transformer.apply(element.replace(quotation, "")));
            }
        }

        return elements;
    }
}
