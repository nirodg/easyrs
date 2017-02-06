package com.dbrage.lib.easyrs.arquillian.utils;

public class Checker extends AssertionError {

	private static final String MESSAGE = "Value doesn't match with the expected one";
	private static final long serialVersionUID = 8124485893052359594L;

	public Checker(String message) {
	}

	public final static void assertEquals(Object actual, Object fetched) {
		// TODO To be implemented
		throw new Checker(MESSAGE);
	}

}
