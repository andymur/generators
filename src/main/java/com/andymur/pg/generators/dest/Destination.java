package com.andymur.pg.generators.dest;

import com.andymur.pg.generators.core.Generator;
import com.andymur.pg.generators.dest.instructions.Instruction;
import com.andymur.pg.generators.dest.instructions.WriteInstruction;

import java.io.IOException;
import java.util.EnumMap;

/**
 * Created by andymur on 11/12/17.
 */
public interface Destination<T> {

    void write(Generator<T> generator) throws IOException;

    void write(Generator<T> generator, EnumMap<Instruction, WriteInstruction<T>> instructions) throws IOException;
}
