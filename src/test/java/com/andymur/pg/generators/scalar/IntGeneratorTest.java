package com.andymur.pg.generators.scalar;

import com.andymur.pg.generators.rand.Rand;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

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

        Assert.assertTrue(generator.generate() < 64);
        Assert.assertTrue(generator.generate() > 0);
    }

    @Test
    public void testGenerationWithFullRange() {

        IntGenerator generator = IntGenerator
                .IntGeneratorBuilder
                .of()
                .withRange(new Range<>(10, 22))
                .build();

        for (int i = 0; i < 1_000_000; i++) {
            Assert.assertTrue(generator.generate() >= 10);
            Assert.assertTrue(generator.generate() < 22);
        }
    }
}