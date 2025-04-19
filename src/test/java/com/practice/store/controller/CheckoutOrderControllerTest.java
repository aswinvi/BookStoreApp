package com.practice.store.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.practice.store.exception.BookStoreIllegalArgumentException;
import com.practice.store.model.Basket;
import com.practice.store.service.CheckoutService;

class CheckoutOrderControllerTest {

	@InjectMocks
	private CheckoutOrderController checkoutOrderController;

	@Mock
	private CheckoutService checkoutService;

	@Mock
	private CacheFactoryService cache;

	private String sessionId;

	private Basket basket;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		sessionId = "testSessionId";
		basket = new Basket();
	}

	@Test
	void shouldReturnSuccessfulResponseWhenCheckoutCompletes() {
		Mockito.when(checkoutService.executeCheckout(basket)).thenReturn(true);
		when(cache.getBasketFromCache(sessionId)).thenReturn(basket);

		ResponseEntity<BooksOrderDetailsResponse> response = checkoutOrderController.checkoutBasket(sessionId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Checkout is Successful and the order will be dispactched soon !!!",
				response.getBody().getMessage());
		verify(checkoutService).executeCheckout(basket);
		verify(cache).deleteBasketFromCache(sessionId);
	}

	@Test
	void checkoutBasket_ShouldReturnBadRequest_WhenBasketIsEmpty() {
		when(cache.getBasketFromCache(sessionId)).thenReturn(null);

		ResponseEntity<BooksOrderDetailsResponse> response = checkoutOrderController.checkoutBasket(sessionId);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Error: The books list is empty or invalid.", response.getBody().getMessage());
	}

	@Test
	void checkoutBasket_ShouldReturnBadRequest_WhenCheckoutFails() {
		when(cache.getBasketFromCache(sessionId)).thenReturn(basket);
		doThrow(new BookStoreIllegalArgumentException("Insufficient stock")).when(checkoutService)
				.executeCheckout(basket);

		ResponseEntity<BooksOrderDetailsResponse> response = checkoutOrderController.checkoutBasket(sessionId);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains("Insufficient stock"));
	}
}