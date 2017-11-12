package com.andymur.pg.generators.dest.instructions;

/**
 * Created by andymur on 11/12/17.
 */
public class GeneratedNumberInstruction implements WriteInstruction<Integer> {

    private final int generatedNumber;

    public GeneratedNumberInstruction(int generatedNumber) {
        this.generatedNumber = generatedNumber;
    }

    @Override
    public Instruction name() {
        return Instruction.GENERATED_NUMBER;
    }

    @Override
    public Integer value() {
        return generatedNumber;
    }
}
