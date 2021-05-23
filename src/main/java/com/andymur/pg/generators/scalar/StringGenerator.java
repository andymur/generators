package com.andymur.pg.generators.scalar;

import com.andymur.pg.generators.core.Generator;
import com.andymur.pg.generators.dest.Destination;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

//TODO: withRange and withSymbols conflict
public class StringGenerator implements Generator<String> {

    private static final int DEFAULT_LENGTH = 10;

    private Function<String, Boolean> filterFunction = (x) -> true;
    private Set<Character> fromSymbols;
    private Set<String> fromSet = new HashSet<>();

    private IntGenerator codePointGenerator;
    private IntGenerator lengthGenerator;

    private Range<Character> range = new Range<>('A', 'Z');

    private final int fromLength;
    private final int toLength;


    private StringGenerator(Set<Character> fromSymbols, int fromLength, int toLength) {
        this.fromLength = fromLength;
        this.toLength = toLength;
        this.fromSymbols = fromSymbols;

        buildGenerators();
    }

    private void buildGenerators() {

        if (!fromSymbols.isEmpty()) {

            final Set<Integer> codePoints = fromSymbols.stream().map(this::charToCodePoint).collect(Collectors.toSet());

            codePointGenerator = IntGenerator.IntGeneratorBuilder.of()
                    .fromSet(codePoints).build();
        } else {
            codePointGenerator = IntGenerator.IntGeneratorBuilder.of()
                    .withRange(charToCodePoint(range.from.get()), charToCodePoint(range.to.get())).build();
        }

        lengthGenerator = IntGenerator.IntGeneratorBuilder.of().withRange(fromLength, toLength).build();
    }

	@Override
	public String generate() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < lengthGenerator.generate(); i++) {
            builder.append(codePointToChar(codePointGenerator.generate()));
        }

        return builder.toString();
	}

    @Override
    public Iterator<String> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                // let's say we have infinite generators for now
                return true;
            }

            @Override
            public String next() {
                return generate();
            }
        };
    }

    int charToCodePoint(char c) {
        return Character.codePointAt(new char[] {c}, 0);
    }

    char codePointToChar(int codePoint) {
        char[] chars = Character.toChars(codePoint);

        if (chars.length > 1) {
            throw new IllegalStateException("Code point " + codePoint + " can't be presented as character");
        }

        return chars[0];
    }

    static class StringGeneratorBuilder {
        private Range<Character> range;
        private int fromLength = DEFAULT_LENGTH;
        private int toLength = DEFAULT_LENGTH;

        private Set<Character> fromSymbols = new HashSet<>();


        public static StringGeneratorBuilder of() {
            return new StringGeneratorBuilder();
        }

        public StringGeneratorBuilder withRange(char from, char to) {
            range = new Range<>(from, to);
            return this;
        }

        public StringGeneratorBuilder withSymbols(Character...symbols) {
            fromSymbols = new HashSet<>(Arrays.asList(symbols));
            return this;
        }

        public StringGeneratorBuilder withLength(int length) {
            fromLength = length;
            toLength = length;
            return this;
        }

        public StringGeneratorBuilder withLength(int from, int to) {
            fromLength = from;
            toLength = to;
            return this;
        }

        public StringGenerator build() {
            StringGenerator generator = new StringGenerator(fromSymbols, fromLength, toLength + 1);
            generator.range = range;

            return generator;
        }

    }

}
