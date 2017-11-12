package com.andymur.pg.generators.source;

import java.nio.file.Path;

/**
 * Created by andymur on 11/12/17.
 */
public class SourceBuilder<T> {

    private Path sourceFilePath;
    private String delimiter;

    private SourceBuilder() {

    }

    public static <T> SourceBuilder<T> of() {
        return new SourceBuilder<>();
    }

    public SourceBuilder<T> withPath(Path path) {
        this.sourceFilePath = path;
        return this;
    }

    public Source<T> build() {
        return new FileSource<T>(sourceFilePath.toFile());
    }
}
