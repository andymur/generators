package com.andymur.pg.generators.scalar;

import com.andymur.pg.generators.annotations.AnnotationProcessor;
import com.andymur.pg.generators.annotations.Generator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class AnnotatedGeneratorTest {

    @Test
    @Disabled
    public void testAnnotation() {
        final AnnotatedClass annotatedClass = new AnnotatedClass();

        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        annotationProcessor.process(annotatedClass);
        Assertions.assertNotNull(annotatedClass.getNext());
    }

    static class AnnotatedClass {
        @Generator
        private com.andymur.pg.generators.core.Generator<Integer> generator;

        public Integer getNext() {
            return generator.generate();
        }
    }
}
