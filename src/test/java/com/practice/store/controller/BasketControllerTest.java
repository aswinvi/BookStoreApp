package com.practice.store.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.practice.store.CacheFactoryService;
import com.practice.store.api.response.BooksOrderDetailsResponse;
import com.practice.store.model.Basket;
import com.practice.store.model.Book;
import com.practice.store.service.BasketService;

class BasketControllerTest {

	@InjectMocks
	private BasketController basketController;

	@Mock
	private BasketService basketService;

	@Mock
	private CacheFactoryService cache;

	private String sessionId;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		sessionId = "testSessionId";
	}

	@Test
	void shouldAddBooksSuccessfullyInToBasket() {

		List<Book> books = List.of(new Book("Book1", "Author1", 50, 2023, 1),
				new Book("Book2", "Author2", 50, 2019, 1));
		Basket basket = new Basket();

		when(cache.getBasketFromCache(sessionId)).thenReturn(basket);
		when(basketService.createBasket(basket)).thenReturn(basket);
		when(basketService.hasNoValue(Mockito.anyList())).thenReturn(false);

		ResponseEntity<BooksOrderDetailsResponse> response = basketController.addBooksToBasket(books, sessionId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Successfully added books into Basket !!!", response.getBody().getMessage());

		verify(basketService).addBooksToBasket(basket, books);
		verify(cache).addBasketToCache(sessionId, basket);
	}

	@Test
	void shouldRemoveBooksSuccessfullyFromBasket() {

		List<Book> books = List.of(new Book("Book1", "Author1", 50, 2023, 1));
		Basket basket = new Basket();
		basket.addBook(new Book("Book1", "Author1", 50, 2023, 1), 1);
		when(basketService.hasNoValue(Mockito.anyList())).thenReturn(false);

		when(cache.getBasketFromCache(sessionId)).thenReturn(basket);
		when(basketService.createBasket(basket)).thenReturn(basket);
		when(basketService.hasNoValue(books)).thenReturn(false);

		ResponseEntity<BooksOrderDetailsResponse> response = basketController.removeBooksFromBasket(books, sessionId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Successfully removed books from Basket !!!", response.getBody().getMessage());

		verify(basketService).removeBooksFromBasket(basket, books);
		verify(cache).addBasketToCache(sessionId, basket);
	}

	@Test
	void shouldReturnBadRequestErrorWhileAddingEmptyList() {

		List<Book> books = List.of();
		Basket basket = new Basket();

		when(cache.getBasketFromCache(sessionId)).thenReturn(basket);
		when(basketService.createBasket(basket)).thenReturn(basket);
		when(basketService.hasNoValue(Mockito.anyList())).thenReturn(true);

		ResponseEntity<BooksOrderDetailsResponse> response = basketController.addBooksToBasket(books, sessionId);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Error: The books list is empty or invalid.", response.getBody().getMessage());

		verify(basketService, Mockito.never()).addBooksToBasket(basket, books);
		verify(cache, Mockito.never()).addBasketToCache(sessionId, basket);
	}

	@Test
	void shouldReturnBadRequestErrorWhileRemovinggEmptyList() {

		List<Book> books = new ArrayList<>();
		Basket basket = new Basket();

		when(cache.getBasketFromCache(sessionId)).thenReturn(basket);
		when(basketService.createBasket(basket)).thenReturn(basket);
		when(basketService.hasNoValue(Mockito.anyList())).thenReturn(true);

		ResponseEntity<BooksOrderDetailsResponse> response = basketController.removeBooksFromBasket(books, sessionId);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Error: The books list is empty or invalid.", response.getBody().getMessage());

		verify(basketService, Mockito.never()).removeBooksFromBasket(basket, books);
		verify(cache, Mockito.never()).addBasketToCache(sessionId, basket);
	}
}