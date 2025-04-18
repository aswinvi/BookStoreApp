package com.practice.store.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BasketTest {

	
	Basket basket;
	
	
	@Test
	void shouldReturnTheStoredBookAndQuantity() {
		
		String title = "The Clean coder";
		String author = "Robert Martin";
		double price = 50;
		int year = 2011;
		int quantity = 1;

		Book book = new Book(title, author, price, year, quantity);
		
		basket = new Basket();
		
		
		basket.addBook(book, 1);
		
		assertEquals(1, basket.getBooks().get(book));
	}

}
