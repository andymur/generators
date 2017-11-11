package com.andymur.pg.generators.source;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by andymur on 11/11/17.
 */
public interface Source<T> {
    List<T> readSource() throws IOException;
    Set<T> readSourceUnique() throws IOException;
}
