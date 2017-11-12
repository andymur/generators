package com.andymur.pg.generators.core;

import com.andymur.pg.generators.dest.Destination;

import java.io.IOException;

/**
 * Created by andymur on 10/31/17.
 */
public interface Generator<T> {
    T generate();
    void toDestination(Destination<T> destination) throws IOException;
}
