package com.andymur.pg.generators.rand;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by andymur on 10/31/17.
 */
public class DefaultRand implements Rand {

    @Override
    public int nextInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    @Override
    public int nextInt(int from, int to) {
        return ThreadLocalRandom.current().nextInt(to - from) + from;
    }

    @Override
    public int nextInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }
}
