package com.andymur.pg.generators.core;

import com.andymur.pg.generators.dest.Destination;

import java.io.IOException;

/**
 * Class for handful creation and work operations around generators
 */
public class Generators {

    public static <T> ComprehensionBuilder<T> of(Generator<T> generator) {
        return new ComprehensionBuilder<>(generator);
    }

    public static class ComprehensionBuilder<T> {

        private final Generator<T> generator;

        public ComprehensionBuilder(Generator<T> generator) {
            this.generator = generator;
        }

        public void toDestination(final Destination<T> dest) throws IOException {
            dest.write(generator);
        }
    }
}
