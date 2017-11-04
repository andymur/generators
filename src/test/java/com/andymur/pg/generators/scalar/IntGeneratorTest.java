package com.andymur.pg.generators.scalar;

import com.andymur.pg.generators.rand.Rand;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class IntGeneratorTest {

    private static final int UNIVERSAL_ANSWER = 33;

    @Test
    public void testGenerationWithoutRange() {

        final Rand rand = Mockito.mock(Rand.class);

        when(rand.nextInt(anyInt(), anyInt())).thenReturn(UNIVERSAL_ANSWER);
        when(rand.nextInt()).thenReturn(UNIVERSAL_ANSWER);
        when(rand.nextInt(anyInt())).thenReturn(UNIVERSAL_ANSWER);

        IntGenerator generator = IntGenerator
                .IntGeneratorBuilder
                .of()
                .withRandSource(rand)
                .build();

        assertTrue(generator.generate() < 64);
        assertTrue(generator.generate() > 0);
    }

    @Test
    public void testGenerationWithFullRange() {

        IntGenerator generator = IntGenerator
                .IntGeneratorBuilder
                .of()
                .withRange(10, 22)
                .build();

        for (int i = 0; i < 1_000_000; i++) {
            assertTrue(generator.generate() >= 10);
            assertTrue(generator.generate() < 22);
        }
    }

    @Test
    public void testGenerationFromRange() {

        IntGenerator generator = IntGenerator
                .IntGeneratorBuilder
                .of()
                .from(10)
                .build();

        for (int i = 0; i < 1_000_000; i++) {
            assertTrue(generator.generate() >= 10);
        }
    }

    @Test
    public void testGenerationToRange() {

        IntGenerator generator = IntGenerator
                .IntGeneratorBuilder
                .of()
                .to(22)
                .build();

        for (int i = 0; i < 1_000_000; i++) {
            assertTrue(generator.generate() < 22);
        }
    }

    @Test
    public void testGenerationWithFilter() {

        IntGenerator generator = IntGenerator
                .IntGeneratorBuilder
                .of()
                .withFilter((x) -> x % 2 == 0)
                .build();

        for (int i = 0; i < 1_000_000; i++) {
            assertTrue(generator.generate() % 2 == 0);
        }
    }

    @Test
    public void testGenerationWithConstant() {

        IntGenerator generator = IntGenerator
                .IntGeneratorBuilder
                .of()
                .withConstant(42)
                .build();

        for (int i = 0; i < 1_000_000; i++) {
            assertTrue(generator.generate() == 42);
        }
    }

    @Test
    public void testGenerationFromSet() {

        Set<Integer> fromSet = new HashSet<>(Arrays.asList(1, 2, 3, 5, 8, 13));

        IntGenerator generator = IntGenerator
                .IntGeneratorBuilder
                .of()
                .fromSet(1, 2, 3, 5, 8, 13)
                .build();

        for (int i = 0; i < 1_000_000; i++) {
            int number = generator.generate();
            assertTrue(number + " doesn't contain in set", fromSet.contains(number));
        }
    }

    @Test
    public void testGenerationFromSetWithFiltering() {

        Set<Integer> fromSet = new HashSet<>(Arrays.asList(1, 2, 3, 5, 8));

        IntGenerator generator = IntGenerator
                .IntGeneratorBuilder
                .of()
                .withFilter(x -> x != 13)
                .fromSet(1, 2, 3, 5, 8, 13)
                .build();

        for (int i = 0; i < 1_000_000; i++) {
            int number = generator.generate();
            assertTrue(number + " doesn't contain in set", fromSet.contains(number));
        }
    }

    @Test
    public void testGenerationFromSetWithNoRepetition() {

        Set<Integer> fromSet = new HashSet<>(Arrays.asList(1, 2, 3, 5, 8));

        IntGenerator generator = IntGenerator
                .IntGeneratorBuilder
                .of()
                .fromSet(1, 2, 3, 5, 8, 13)
                .withNoRepetition()
                .build();

        System.out.println(generator.generate());
        System.out.println(generator.generate());
        System.out.println(generator.generate());
        System.out.println(generator.generate());
        System.out.println(generator.generate());
        System.out.println(generator.generate());
        System.out.println(generator.generate());
    }
}