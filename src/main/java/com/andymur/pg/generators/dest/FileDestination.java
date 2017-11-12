package com.andymur.pg.generators.dest;

import com.andymur.pg.generators.core.Generator;
import com.andymur.pg.generators.dest.instructions.Instruction;
import com.andymur.pg.generators.dest.instructions.WriteInstruction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.EnumMap;

/**
 * Created by andymur on 11/12/17.
 */
public class FileDestination<T> implements Destination<T> {

    private static final int DEFAULT_GENERATED_NUMBER = 10;
    private static final int DEFAULT_BUFFER_SIZE = 65535;

    private static final String DEFAULT_DELIMITER = "\n";
    private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();

    private File destinationFile;

    public FileDestination(File destinationFile) {
        this.destinationFile = destinationFile;
    }

    @Override
    public void write(Generator<T> generator) throws IOException {
        write(DEFAULT_GENERATED_NUMBER, generator);
    }

    @Override
    public void write(Generator<T> generator, EnumMap<Instruction, WriteInstruction> instructions) throws IOException {
        WriteInstruction genNumberInstruction = instructions.get(Instruction.GENERATED_NUMBER);

        if (genNumberInstruction == null) {
            write(generator);
            return;
        }

        int generatedNumber = (Integer) genNumberInstruction.value();
        write(generatedNumber, generator);
    }

    private void write(int genNumber, Generator<T> generator) throws IOException {
        CharBuffer buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);

        try (BufferedWriter writer = Files.newBufferedWriter(destinationFile.toPath(), DEFAULT_CHARSET,
                StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {

            for (int i = 0; i < genNumber; i++) {
                final T element = generator.generate();
                final String portion = element.toString().concat(DEFAULT_DELIMITER);
                if (buffer.remaining() > portion.length()) {
                    buffer.append(portion);
                } else {
                    writer.write(Arrays.copyOf(buffer.array(), buffer.position()));
                    buffer.clear();
                    buffer.append(portion);
                }
            }

            writer.write(Arrays.copyOf(buffer.array(), buffer.position()));
        }
    }
}
