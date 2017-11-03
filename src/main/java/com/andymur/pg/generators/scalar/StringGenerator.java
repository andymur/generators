package com.andymur.pg.generators.scalar;

import com.andymur.pg.generators.core.Generator;

public class StringGenerator implements Generator<String> {

	private static final Range<Character> DEFAULT_RANGE = new Range<>('A', 'Z');

	@Override
	public String generate() {
		return null;
	}

}
