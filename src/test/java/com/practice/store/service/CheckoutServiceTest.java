package com.practice.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.practice.store.config.BookStoreConfigLoader;
import com.practice.store.exception.BookStoreIllegalArgumentException;
import com.practice.store.model.Basket;
import com.practice.store.model.Book;
import com.practice.store.model.BookStore;

class CheckoutServiceTest {

	@InjectMocks
	private CheckoutService checkoutService;

	@Mock
	private BookStoreConfigLoader configLoader;

	@Mock
	private DiscountService discountService;

	private Basket basket;

	private List<Book> booksInStore = new ArrayList<Book>();

	private BookStore bookStore = new BookStore();

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		basket = new Basket();
		basket.addBook(new Book("Book1", "Author1", 20.00, 2020, 2), 2);
		basket.addBook(new Book("Book2", "Author2", 30.00, 2019, 5), 1);

		booksInStore.add(new Book("Book1", "Author1", 20.00, 2020, 5));
		booksInStore.add(new Book("Book2", "Author2", 30.00, 2019, 10));
		booksInStore.add(new Book("Book3", "Author3", 25.00, 2021, 2));

		bookStore.setBooksInTheStore(booksInStore);
	}

	@Test
	void shouldReturnCorrectTotalPrice() {

		when(configLoader.getLoadedData()).thenReturn(Optional.ofNullable(bookStore));
		when(discountService.calculateBasketPrice(anyList())).thenReturn(70.00);

		double totalPrice = checkoutService.checkoutBasket(basket);

		assertEquals(70.00, totalPrice);
		assertTrue(basket.getBooks().isEmpty());
		verify(configLoader, Mockito.times(1)).setBooksInToStore(anyList());
	}

	@Test
	void shouldThrowExceptionWhenNoBooksAvailable() {

		when(configLoader.getLoadedData()).thenReturn(Optional.empty());

		BookStoreIllegalArgumentException exception = assertThrows(BookStoreIllegalArgumentException.class,
				() -> checkoutService.checkoutBasket(basket));
		assertEquals("No Books AVailable to checkout!!", exception.getMessage());
	}

	@Test
	void shouldThrowExceptionWhenInsufficientStock() {

		when(configLoader.getLoadedData()).thenReturn(Optional.ofNullable(bookStore));

		basket = new Basket();
		basket.addBook(new Book("Book1", "Author1", 20.00, 2020, 15), 15);

		BookStoreIllegalArgumentException exception = assertThrows(BookStoreIllegalArgumentException.class,
				() -> checkoutService.checkoutBasket(basket));
		assertEquals("Insufficient stock for: Book1", exception.getMessage());
	}

	@Test
	void checkAndUpdateAvailableBooksToCheckout_ShouldUpdateStoreAndBasket() {

		List<Book> basketBooks = new ArrayList<>();
		when(configLoader.getLoadedData()).thenReturn(Optional.ofNullable(bookStore));

		List<Book> updatedStore = checkoutService.checkAndUpdateAvailableBooksToCheckout(basket, booksInStore,
				basketBooks);

		assertNotNull(updatedStore);
		assertEquals(3, basketBooks.size());
		verify(configLoader, never()).setBooksInToStore(updatedStore);
	}
}