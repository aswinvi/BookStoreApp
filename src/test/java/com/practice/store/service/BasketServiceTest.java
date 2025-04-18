package com.practice.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.practice.store.model.Basket;
import com.practice.store.model.Book;

class BasketServiceTest {

	@InjectMocks
	private BasketService basketService;

	private Basket basket;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		basket = new Basket();
	}

	@Test
	void shouldAddBooksSuccessfullyIntoBasket() {
		Book book1 = new Book("Book1", "Auther", 50, 2022, 1);
		book1.setQuantity(1);

		Book book2 = new Book("Book2", "Auther O", 50, 2011, 2);
		book2.setQuantity(2);

		List<Book> books = List.of(book1, book2);

		basketService.addBooksToBasket(basket, books);

		assertEquals(2, basket.getBooks().size());
		assertTrue(basket.getBooks().containsKey(book1));
		assertTrue(basket.getBooks().containsKey(book2));
		assertEquals(1, basket.getBooks().get(book1));
		assertEquals(2, basket.getBooks().get(book2));
		assertEquals(150.0, basket.getTotalBasketValue());
	}

	@Test
	void shouldUpdateBooksFromBasketByReducingQuantityOrRemoveBooks() {
		Book book1 = new Book("Book One", "Auther", 50, 2022, 3);
		book1.setQuantity(3);

		Book book2 = new Book("Book Two", "Auther O", 50, 2011, 3);
		book2.setQuantity(3);

		basket.getBooks().put(book1, book1.getQuantity());
		basket.getBooks().put(book2, book2.getQuantity());

		List<Book> booksToRemove = List.of(new Book("Book One", "Auther", 50, 2022, 1),
				new Book("Book Two", "Auther O", 50, 2011, 3));

		basketService.removeBooksFromBasket(basket, booksToRemove);

		assertEquals(2, basket.getBooks().get(book1));
		assertFalse(basket.getBooks().containsKey(book2));
		assertEquals(0.0, basket.getTotalBasketValue());
	}

	@Test
	void shouldIncreaseQuantityForExistingBooks() {
		Book book1 = new Book("Book1", "Auther", 50, 2022, 2);
		book1.setQuantity(2);
		basket.getBooks().put(book1, book1.getQuantity());

		Book newBook = new Book("Book1", "Auther", 50, 2022, 3);
		newBook.setQuantity(3);

		List<Book> booksToAdd = List.of(newBook);

		basketService.addBooksToBasket(basket, booksToAdd);

		assertEquals(5, basket.getBooks().get(book1));
		assertEquals(150.0, basket.getTotalBasketValue());
	}

	@Test
	void shouldNotRemoveNonExistingBooks() {
		Book book1 = new Book("Book1", "Auther", 50, 2022, 2);
		book1.setQuantity(2);
		basket.getBooks().put(book1, book1.getQuantity());

		List<Book> booksToRemove = List.of(new Book("Book Not In Basket", "Author", 40, 1987, 1));

		basketService.removeBooksFromBasket(basket, booksToRemove);

		assertEquals(2, basket.getBooks().get(book1));
		assertEquals(0, basket.getTotalBasketValue());
	}
}