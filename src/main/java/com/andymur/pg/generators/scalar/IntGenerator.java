package com.andymur.pg.generators.scalar;

import com.andymur.pg.generators.core.Generator;
import com.andymur.pg.generators.rand.DefaultRand;
import com.andymur.pg.generators.rand.Rand;

/**
 * Created by andymur on 10/31/17.
 */
public class IntGenerator implements Generator<Integer> {

    private static final int DEFAULT_CEIL = 64;

    private Range<Integer> range;
    private Rand randSource;

    private IntGenerator() {
    }

    // create generation function, filtration function
    @Override
    public Integer generate() {
        if (range == null) {
            return randSource.nextInt(DEFAULT_CEIL);
        } else {
            int to = range.getTo().orElse(DEFAULT_CEIL);
            int from = range.getFrom().orElse(0);

            // 10, 32 -> random(22) + 10
            return randSource.nextInt(from, to);
        }
    }

    static class IntGeneratorBuilder {

        private Range<Integer> range;
        private Rand randSource;

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

        public IntGeneratorBuilder withRange(Range<Integer> range) {
            this.range = range;
            return this;
        }

        public IntGeneratorBuilder withRandSource(Rand randSource) {
            this.randSource = randSource;
            return this;
        }

        public IntGenerator build() {
            IntGenerator generator = new IntGenerator();
            generator.range = range;
            generator.randSource = randSource == null ? new DefaultRand() : randSource;
            return generator;
        }


    }
}
