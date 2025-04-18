package com.practice.store.exception;

import java.io.IOException;

public class BookStoreIOException extends IOException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BookStoreIOException(String message) {
		super(message);
	}
}
