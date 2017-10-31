package com.andymur.pg.generators.rand;

/**
 * Created by andymur on 10/31/17.
 */
public interface Rand {
    int nextInt();
    int nextInt(int from, int to);
    int nextInt(int bound);
}
