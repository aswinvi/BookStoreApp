package com.practice.store.api.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.practice.store.model.Book;

class BookStoreResponseTest {

	@Test
	void shouldReturnBookStoreResponse() {

		String title = "Clean Code";
		String author = "Robert Martin";
		double price = 50;
		int year = 2008;
		int quantity = 1;

		Book book = new Book(title, author, price, year, quantity);

		List<Book> books = new ArrayList<>();
		books.add(book);

		BookStoreResponse response = new BookStoreResponse("Message Loaded", books);

		assertEquals("Message Loaded", response.getMessage());
		assertEquals(book.getTitle(), response.getBooks().get(0).getTitle());
	}

}
