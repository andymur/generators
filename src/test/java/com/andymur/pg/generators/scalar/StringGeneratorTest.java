package com.andymur.pg.generators.scalar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StringGeneratorTest {

    @Test
    public void testCommonFunctions() {
        StringGenerator generator = StringGenerator.StringGeneratorBuilder.of().build();
        assertEquals(65, generator.charToCodePoint('A'));
        assertEquals('A', generator.codePointToChar(65));
        System.out.println(generator.charToCodePoint('А'));
        System.out.println(generator.charToCodePoint('Я'));
        System.out.println(generator.charToCodePoint('Ё'));

        for (int i = 1040; i < 1071; i++) {
            System.out.println(generator.codePointToChar(i));
        }
    }

    @Test
    public void testDefaultGeneration() {
        StringGenerator generator = StringGenerator.StringGeneratorBuilder.of().build();

        String generated = generator.generate();

        assertNotNull(generated, "Generated cannot be null");
        assertEquals(10, generated.length(), "Generated length is " + generated.length() + " while should be ten");

        for (int i = 0; i < 1_000_000; i++) {
            generator.generate();
        }
    }

    @Test
    public void testGenerationFromSymbols() {
        StringGenerator generator = StringGenerator.StringGeneratorBuilder.of()
                .withSymbols('X', 'Y', 'Z')
                .withLength(3)
                .build();

        String generated = generator.generate();

        assertNotNull(generated, "Generated cannot be null");
        assertEquals(3, generated.length(), "Generated length is " + generated.length() + " while should be three");

        for (int i = 0; i < 100; i++) {
            System.out.println(generator.generate());
        }
    }

}