package com.practice.store.api.response;

import java.io.Serializable;
import java.util.List;

import com.practice.store.model.Book;

public class BookStoreResponse implements Serializable{

	    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		private String message;
	    
	    private List<Book> books;

	    public BookStoreResponse(String message, List<Book> books) {
	        this.message = message;
	        this.books = books;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public List<Book> getBooks() {
	        return books;
	    }

}
