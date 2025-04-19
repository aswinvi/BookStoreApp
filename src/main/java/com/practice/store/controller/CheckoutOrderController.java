package com.practice.store.controller;

import static com.practice.store.constants.BookStoreConstants.*;
import static com.practice.store.constants.BookStoreConstants.CHECKOUT_IS_SUCCESSFUL;
import static com.practice.store.constants.BookStoreConstants.ERROR_THE_BOOKS_LIST_IS_EMPTY_OR_INVALID;
import static com.practice.store.constants.BookStoreConstants.HTTP_STATUS_200;
import static com.practice.store.constants.BookStoreConstants.HTTP_STATUS_404;
import static com.practice.store.constants.BookStoreConstants.HTTP_STATUS_500;
import static com.practice.store.constants.BookStoreConstants.INTERNAL_SERVER_ERROR;
import static com.practice.store.constants.BookStoreConstants.REARRANGE_ORDER;
import static com.practice.store.constants.BookStoreConstants.SESSION_ID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.practice.store.CacheFactoryService;
import com.practice.store.api.response.BooksOrderDetailsResponse;
import com.practice.store.exception.BookStoreIllegalArgumentException;
import com.practice.store.model.Basket;
import com.practice.store.service.CheckoutService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/order")
public class CheckoutOrderController {

	private final CheckoutService checkoutService;

	private final CacheFactoryService cache;

	public CheckoutOrderController(CheckoutService checkoutService, CacheFactoryService cache) {
		super();
		this.checkoutService = checkoutService;
		this.cache = cache;
	}

	@Operation(summary = "Checkout the basket", description = "This service helps to Checkout the basket and create order")
	@ApiResponses(value = { @ApiResponse(responseCode = HTTP_STATUS_200, description = CHECKOUT_IS_SUCCESSFUL),
			@ApiResponse(responseCode = HTTP_STATUS_404, description = BAD_REQUEST),
			@ApiResponse(responseCode = HTTP_STATUS_500, description = INTERNAL_SERVER_ERROR) })
	@PostMapping(value = "/checkout", produces = "application/json")
	public ResponseEntity<BooksOrderDetailsResponse> checkoutBasket(
			@Parameter(description = SESSION_ID, required = true) @RequestParam String sessionId) {

		Basket basket = cache.getBasketFromCache(sessionId);
		
		boolean checkoutStatus = false;
		if (null == basket) {
			return formBadRequestError(ERROR_THE_BOOKS_LIST_IS_EMPTY_OR_INVALID);
		}
		try {
			checkoutStatus = checkoutService.executeCheckout(basket);
		} catch (BookStoreIllegalArgumentException e) {
			return formBadRequestError(e.getMessage() + REARRANGE_ORDER);
		}

		String message = setCheckOutStatusAndClearCache(sessionId, checkoutStatus);

		return ResponseEntity.ok(new BooksOrderDetailsResponse(message, basket));
	}

	private String setCheckOutStatusAndClearCache(String sessionId, boolean checkoutStatus) {
		if (checkoutStatus) {
			cache.deleteBasketFromCache(sessionId);
			return CHECKOUT_IS_SUCCESSFUL;
		} else {
			return PLEASE_RETRY_CHECKING_OUT_LATER;
		}
	}

	private ResponseEntity<BooksOrderDetailsResponse> formBadRequestError(String message) {
		return ResponseEntity.badRequest().body(new BooksOrderDetailsResponse(message, null));
	}

}
