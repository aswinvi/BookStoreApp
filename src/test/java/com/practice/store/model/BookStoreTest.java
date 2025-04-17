package com.practice.store.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BookStoreTest {

	@Test
	void shouldReturnTheListSet() {

		String title = "Clean Code";
		String author = "Robert Martin";
		double price = 50;
		int year = 2008;
		int quantity = 1;

		Book book = new Book(title, author, price, year, quantity);

		List<Book> books = new ArrayList<>();

		BookStore store = new BookStore();

		store.setBooksInTheStore(books);

		Assertions.assertInstanceOf(List.class, store.getBooksInTheStore());
		Assertions.assertAll(() -> {

			assertEquals(title, book.getTitle(), "Title should match.");
			assertEquals(author, book.getAuthor(), "Author should match.");
			assertEquals(price, book.getPrice(), "Price should match.");
			assertEquals(year, book.getYear(), "Year should match.");
		});
	}

}
