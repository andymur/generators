package com.andymur.pg.generators.scalar;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

        assertNotNull("Generated cannot be null", generated);
        assertEquals("Generated length is " + generated.length() + " while should be ten", 10, generated.length());

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

        assertNotNull("Generated cannot be null", generated);
        assertEquals("Generated length is " + generated.length() + " while should be three", 3, generated.length());

        for (int i = 0; i < 100; i++) {
            System.out.println(generator.generate());
        }
    }

}