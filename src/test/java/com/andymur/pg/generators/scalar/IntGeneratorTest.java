package com.andymur.pg.generators.scalar;

import com.andymur.pg.generators.dest.FileDestination;
import com.andymur.pg.generators.rand.Rand;
import com.andymur.pg.generators.source.SourceBuilder;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
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

    @Test(expected = IllegalStateException.class)
    public void testGenerationFromSetWithNoRepetition() {

        Set<Integer> fromSet = new HashSet<>(Arrays.asList(1, 2, 3, 5, 8, 13));
        Set<Integer> resultSet = new HashSet<>(fromSet.size());

        IntGenerator generator = IntGenerator
                .IntGeneratorBuilder
                .of()
                .fromSet(fromSet)
                .withNoRepetition()
                .build();

        for (Integer element: fromSet) {
            resultSet.add(generator.generate());
        }

        assertEquals(fromSet, resultSet);
        generator.generate();
    }

    @Test
    public void testGenerationRangeWithFiltering() {
        IntGenerator generator = IntGenerator
                .IntGeneratorBuilder
                .of()
                .withRange(1, 101)
                .withFilter(x -> x % 2 != 0)
                .build();

        for (int i = 0; i < 1_000_000; i++) {
            int generated = generator.generate();
            assertTrue("Generated value is " + generated + " must be in [1, 100]", generated <= 100 && generated >= 1);
            assertTrue("Generated value is " + generated + " but can't be even!", generated % 2 != 0);
        }
    }

    @Test
    public void testGenerationFromSetWithRangeAndFiltering() {
        Set<Integer> fromSet = new HashSet<>(Arrays.asList(1, 2, 3, 5, 8, 13));

        IntGenerator generator = IntGenerator
                .IntGeneratorBuilder
                .of()
                .withRange(1, 11)
                .fromSet(fromSet)
                .withFilter(x -> x % 2 != 0)
                .build();

        for (int i = 0; i < 1_000_000; i++) {
            int generated = generator.generate();
            assertTrue("Generated value is " + generated + " must be in [1, 2, 3, 5, 8, 13]", fromSet.contains(generated));
            assertTrue("Generated value is " + generated + " must be in [1, 10]", generated <= 11 && generated >= 1);
            assertTrue("Generated value is " + generated + " but can't be even!", generated % 2 != 0);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testGenerationFromSetWithRangeAndFilteringWithoutRepetition() {
        Set<Integer> fromSet = new HashSet<>(Arrays.asList(1, 2, 3, 5, 8, 13));
        Set<Integer> resultSet = new HashSet<>();

        IntGenerator generator = IntGenerator
                .IntGeneratorBuilder
                .of()
                .withRange(1, 11)
                .fromSet(fromSet)
                .withFilter(x -> x % 2 != 0)
                .withNoRepetition()
                .build();

        for (int i = 0; i < 1_000_000; i++) {
            int generated = generator.generate();
            assertTrue("Generated value is " + generated + " must be in [1, 2, 3, 5, 8, 13]", fromSet.contains(generated));
            assertTrue("Generated value is " + generated + " must be in [1, 10]", generated <= 10 && generated >= 1);
            assertTrue("Generated value is " + generated + " but can't be even!", generated % 2 != 0);

            assertTrue("Generated value is " + generated + " have been already generated", resultSet.add(generated));
        }
    }

    @Test
    public void testGenerationFromSourceToDestination() throws IOException {

        IntGenerator generator = IntGenerator
                .IntGeneratorBuilder
                .of()
                .fromSource(SourceBuilder.of().withPath(Paths.get("")))
                .build();

        FileDestination<Integer> destination = new FileDestination<Integer>(Paths.get("").toFile());

        generator.toDestination(destination);
    }
}