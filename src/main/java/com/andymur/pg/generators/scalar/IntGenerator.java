package com.andymur.pg.generators.scalar;

import com.andymur.pg.generators.core.Generator;
import com.andymur.pg.generators.rand.DefaultRand;
import com.andymur.pg.generators.rand.Rand;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by andymur on 10/31/17.
 */
//TODO: we have so many options so far, do we have conflicts and how should we do when use several of them together? think about it
public class IntGenerator implements Generator<Integer> {

    private static final int DEFAULT_CEIL = 64;
    //TODO: should we have default values for everything?
    //TODO: do we need to make generator mutable
    private Range<Integer> range;
    private Rand randSource;
    private Function<Integer, Boolean> filterFunction = (x) -> true;
    private Supplier<Integer> generateFunction;

    private Set<Integer> fromSet = new HashSet<>();
    private Set<Integer> alreadyGenerated = new CopyOnWriteArraySet<>();
    private boolean noRepetition = false;

    private IntGenerator() {
    }

    @Override
    public Integer generate() {
        if (!fromSet.isEmpty()) {
            return generateAndFilter(this::generateUsingForSetGenerator);
        } else if (generateFunction != null) {
            return generateAndFilter(generateFunction);
        } else if (range == null) {
            return generateAndFilter(() -> randSource.nextInt(DEFAULT_CEIL));
        } else {
            int to = range.getTo().orElse(DEFAULT_CEIL);
            int from = range.getFrom().orElse(0);

            // 10, 32 -> random(22) + 10
            return generateAndFilter(() -> randSource.nextInt(from, to));
        }
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

            if (filterFunction.apply(candidate) && unique) {
                return candidate;
            }
        }
    }

    static class IntGeneratorBuilder {

        private Range<Integer> range;
        private Rand randSource;
        private Function<Integer, Boolean> filterFunction;
        private Supplier<Integer> supplier;

        private Set<Integer> fromSet = new HashSet<>();
        private boolean noRepetition = false;

        public static IntGeneratorBuilder of() {
            return new IntGeneratorBuilder();
        }

        public IntGeneratorBuilder from(Integer from) {
            if (range == null) {
                range = new Range<>(from, null);
            } else {
                final Integer newTo = range.getTo().orElse(null);
                range = new Range<>(from, newTo);
            }

            return this;
        }

        public IntGeneratorBuilder to(Integer to) {
            if (range == null) {
                range = new Range<>(null, to);
            } else {
                final Integer newFrom = range.getFrom().orElse(null);
                range = new Range<>(newFrom, to);
            }

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
            this.supplier = () -> constant;
            return this;
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


        public IntGenerator build() {
            IntGenerator generator = new IntGenerator();
            generator.range = range;
            generator.randSource = randSource == null ? new DefaultRand() : randSource;
            generator.filterFunction = filterFunction == null ? (x) -> true : filterFunction;
            generator.generateFunction = supplier;
            generator.fromSet = fromSet;
            generator.noRepetition = noRepetition;
            return generator;
        }
    }
}
