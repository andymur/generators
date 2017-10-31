package com.andymur.pg.generators.scalar;

import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Created by andymur on 10/31/17.
 */
public class Range<T extends Comparable<T>> {
    final Optional<T> from;
    final Optional<T> to;

    public Range(T from, T to) {

        if (Objects.isNull(from)) {
            Objects.requireNonNull(to);
        }

        if (Objects.isNull(to)) {
            Objects.requireNonNull(from);
        }

        if (Objects.nonNull(from) && Objects.nonNull(to) && from.compareTo(to) >= 0) {
            throw new IllegalArgumentException(String.format("%s is greater or equal to %s, range is broken", from, to));
        }

        this.from = ofNullable(from);
        this.to = ofNullable(to);
    }

    public Optional<T> getFrom() {
        return from;
    }

    public Optional<T> getTo() {
        return to;
    }
}
