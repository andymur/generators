package com.andymur.pg.generators.scalar;

import com.andymur.pg.generators.core.Generator;
import com.andymur.pg.generators.dest.Destination;
import com.andymur.pg.generators.rand.DefaultRand;
import com.andymur.pg.generators.rand.Rand;
import com.andymur.pg.generators.source.FileSource;
import com.andymur.pg.generators.source.FileSource.FileSourceBuilder;
import com.andymur.pg.generators.source.SourceBuilder;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by andymur on 10/31/17.
 * Non thread safe
 */
public class IntGenerator implements Generator<Integer> {

    private static final int DEFAULT_CEIL = 64;
    private final Range<Integer> range;
    private final Rand randSource;
    private final Function<Integer, Boolean> filterFunction;
    private final Supplier<Integer> generateFunction;

    private final Set<Integer> fromSet;
    private final boolean noRepetition;

    private final Set<Integer> alreadyGenerated = new CopyOnWriteArraySet<>();

    private IntGenerator(final Rand randSource, final Range<Integer> range,
                         final Function<Integer, Boolean> filterFunction,
                         final Supplier<Integer> generateFunction,
                         final Set<Integer> fromSet,
                         final boolean noRepetition
                         ) {
        this.randSource = randSource;
        this.range = range;
        this.filterFunction = filterFunction;
        this.generateFunction = generateFunction;
        this.fromSet = fromSet;
        this.noRepetition = noRepetition;
    }

    @Override
    public Integer generate() {
        if (!fromSet.isEmpty()) {
            return generateAndFilter(this::generateUsingForSetGenerator);
        } else if (generateFunction != null) {
            return generateAndFilter(generateFunction);
        } else {
            int to = range.getTo().orElse(DEFAULT_CEIL);
            int from = range.getFrom().orElse(0);
            // 10, 32 -> random(22) + 10
            return generateAndFilter(() -> randSource.nextInt(from, to));
        }
    }

    @Override
    public void toDestination(Destination<Integer> destination) throws IOException {
        destination.write(this);
    }

    private int generateUsingForSetGenerator() {

        final List<Integer> l = new ArrayList<>(fromSet);

        IntGenerator forSetGenerator = IntGeneratorBuilder.of()
                .withRange(0, fromSet.size())
                .build();

        return l.get(forSetGenerator.generate());
    }

    private Integer generateAndFilter(Supplier<Integer> supplier) {
        while (true) {
            int candidate = supplier.get();
            boolean unique = true;

            if (noRepetition) {

                if (alreadyGenerated.equals(fromSet)) {
                    throw new IllegalStateException("All possible already generated");
                }

                unique = alreadyGenerated.add(candidate);
            }

            if (filterFunction.apply(candidate) && unique && range.contains(candidate)) {
                return candidate;
            }
        }
    }

    public static class IntGeneratorBuilder {

        private Range<Integer> range = Range.empty();
        private Rand randSource;
        private Function<Integer, Boolean> filterFunction;
        private Supplier<Integer> supplier;

        private Set<Integer> fromSet = new HashSet<>();
        private boolean noRepetition = false;
        private FileSourceBuilder<Integer> sourceBuilder;

        public static IntGeneratorBuilder of() {
            return new IntGeneratorBuilder();
        }

        public IntGeneratorBuilder from(Integer from) {

            final Integer newTo = range.getTo().orElse(null);
            range = new Range<>(from, newTo);

            return this;
        }

        public IntGeneratorBuilder to(Integer to) {
            final Integer newFrom = range.getFrom().orElse(null);
            range = new Range<>(newFrom, to);

            return this;
        }

        public IntGeneratorBuilder withRange(int from, int to) {
            this.range = new Range<>(from, to);
            return this;
        }

        public IntGeneratorBuilder withRandSource(Rand randSource) {
            this.randSource = randSource;
            return this;
        }

        public IntGeneratorBuilder withFilter(Function<Integer, Boolean> filterFunction) {
            this.filterFunction = filterFunction;
            return this;
        }

        public IntGeneratorBuilder withGenerator(Supplier<Integer> generateFunction) {
            this.supplier = generateFunction;
            return this;
        }

        //TODO: check that constant is consistent with filter function
        public IntGeneratorBuilder withConstant(Integer constant) {
            return withGenerator(() -> constant);
        }

        public IntGeneratorBuilder fromSet(Integer ... numbers) {
            fromSet = new HashSet<>(Arrays.asList(numbers));
            return this;
        }

        public IntGeneratorBuilder fromSet(Set<Integer> numbers) {
            fromSet = new HashSet<>(numbers);
            return this;
        }

        public IntGeneratorBuilder withNoRepetition() {
            noRepetition = true;
            return this;
        }


        public IntGeneratorBuilder fromFileSource(FileSourceBuilder<Integer> sourceBuilder) {
            this.sourceBuilder = sourceBuilder;
            return this;
        }

        public IntGenerator build() {
            if (sourceBuilder != null) {
                try {
                    fromSet = sourceBuilder.build().readSourceUnique();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            validateState();

            return new IntGenerator(
                    randSource == null ? new DefaultRand() : randSource,
                    range,
                    filterFunction != null ? filterFunction : (x) -> true,
                    supplier,
                    fromSet,
                    noRepetition
            );
        }

        private void validateState() {
            if (supplier != null) {
                if (noRepetition) {
                    throw new IllegalStateException("Generator function cannot be used with no repetition flag");
                }

                if (!fromSet.isEmpty()) {
                    throw new IllegalStateException("Generator function cannot be used along with set of possible generated values");
                }
            }
        }
    }
}
