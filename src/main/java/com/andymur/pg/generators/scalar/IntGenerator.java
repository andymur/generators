package com.andymur.pg.generators.scalar;

import com.andymur.pg.generators.core.Generator;
import com.andymur.pg.generators.rand.DefaultRand;
import com.andymur.pg.generators.rand.Rand;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by andymur on 10/31/17.
 */
public class IntGenerator implements Generator<Integer> {

    private static final int DEFAULT_CEIL = 64;

    private Range<Integer> range;
    private Rand randSource;
    private Function<Integer, Boolean> filterFunction = (x) -> true;
    private Supplier<Integer> generateFunction;

    private IntGenerator() {
    }

    @Override
    public Integer generate() {
        if (generateFunction != null) {
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

    private Integer generateAndFilter(Supplier<Integer> supplier) {
        while (true) {
            int candidate = supplier.get();

            if (filterFunction.apply(candidate)) {
                return candidate;
            }
        }
    }

    static class IntGeneratorBuilder {

        private Range<Integer> range;
        private Rand randSource;
        private Function<Integer, Boolean> filterFunction;
        private Supplier<Integer> supplier;

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

        public IntGenerator build() {
            IntGenerator generator = new IntGenerator();
            generator.range = range;
            generator.randSource = randSource == null ? new DefaultRand() : randSource;
            generator.filterFunction = filterFunction == null ? (x) -> true : filterFunction;
            generator.generateFunction = supplier;
            return generator;
        }


    }
}
