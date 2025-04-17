package com.practice.store.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BookTest {

	@Test
	void testConstructorAndGetters() {
		String title = "The Clean coder";
		String author = "Robert Martin";
		double price = 50;
		int year = 2011;
		int quantity = 1;

		Book book = new Book(title, author, price, year, quantity);

		assertEquals(title, book.getTitle(), "Title should match.");
		assertEquals(author, book.getAuthor(), "Author should match.");
		assertEquals(price, book.getPrice(), "Price should match.");
		assertEquals(year, book.getYear(), "Year should match.");
		assertEquals(quantity, book.getQuantity(), "Quantity should match.");
	}

	@Test
	void shouldReturnToString() {
		Book book = new Book("Clean Code", "Robert Martin", 30, 2008, 1);

		String expected = "Book [title=Clean Code, author=Robert Martin, price=30.0, year=2008, quantity=1]";

		String actual = book.toString();

		assertEquals(expected, actual, "toString() output should match the expected string.");
	}
}
