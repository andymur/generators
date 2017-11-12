package com.andymur.pg.generators.dest.instructions;

/**
 * Created by andymur on 11/12/17.
 */
public interface WriteInstruction<T> {
    Instruction name();
    T value();
}
