package com.practice.store.controller;

import static com.practice.store.constants.BookStoreConstants.ERROR_THE_BOOKS_LIST_IS_EMPTY_OR_INVALID;
import static com.practice.store.constants.BookStoreConstants.HTTP_STATUS_200;
import static com.practice.store.constants.BookStoreConstants.HTTP_STATUS_500;
import static com.practice.store.constants.BookStoreConstants.INTERNAL_SERVER_ERROR;
import static com.practice.store.constants.BookStoreConstants.SESSION_ID;
import static com.practice.store.constants.BookStoreConstants.SUCCESSFULLY_DELETED_THE_BOOK_FROM_THE_BASKET;
import static com.practice.store.constants.BookStoreConstants.SUCCESSFULLY_REMOVED_BOOKS_FROM_BASKET;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.practice.store.CacheFactoryService;
import com.practice.store.api.response.BooksOrderDetailsResponse;
import com.practice.store.model.Basket;
import com.practice.store.model.Book;
import com.practice.store.service.BasketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/basket")
public class BasketController {

	private final BasketService basketService;

	private final CacheFactoryService cache;

	public BasketController(BasketService basketService, CacheFactoryService cache) {
		super();
		this.basketService = basketService;
		this.cache = cache;
	}

	@Operation(summary = "Add Books into Basket for checkout", description = "This service helps to Add Books into Basket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = HTTP_STATUS_200, description = "Successfully added the book into the basket"),
			@ApiResponse(responseCode = HTTP_STATUS_500, description = INTERNAL_SERVER_ERROR) })
	@PostMapping(value = "/addBooksToBasket", produces = "application/json")
	public ResponseEntity<BooksOrderDetailsResponse> addBooksToBasket(@RequestBody List<Book> books,
			@Parameter(description = SESSION_ID, required = true) @RequestParam String sessionId) {

		if (basketService.hasNoValue(books)) {
			return formBadRequestError();
		}

		Basket basket = basketService.createBasket(cache.getBasketFromCache(sessionId));

		basketService.addBooksToBasket(basket, books);

		cache.addBasketToCache(sessionId, basket);

		BooksOrderDetailsResponse response = new BooksOrderDetailsResponse("Successfully added books into Basket !!!",
				basket);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Remove Books from Basket", description = "This service helps to remove Books from Basket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = HTTP_STATUS_200, description = SUCCESSFULLY_DELETED_THE_BOOK_FROM_THE_BASKET),
			@ApiResponse(responseCode = HTTP_STATUS_500, description = INTERNAL_SERVER_ERROR) })
	@PostMapping(value = "/removeBooks", produces = "application/json")
	public ResponseEntity<BooksOrderDetailsResponse> removeBooksFromBasket(@RequestBody List<Book> books,
			@Parameter(description = SESSION_ID, required = true) @RequestParam String sessionId) {

		if (basketService.hasNoValue(books)) {
			return formBadRequestError();
		}

		Basket basket = basketService.createBasket(cache.getBasketFromCache(sessionId));

		basketService.removeBooksFromBasket(basket, books);

		cache.addBasketToCache(sessionId, basket);

		BooksOrderDetailsResponse response = new BooksOrderDetailsResponse(SUCCESSFULLY_REMOVED_BOOKS_FROM_BASKET,
				basket);

		return ResponseEntity.ok(response);
	}

	private ResponseEntity<BooksOrderDetailsResponse> formBadRequestError() {
		return ResponseEntity.badRequest()
				.body(new BooksOrderDetailsResponse(ERROR_THE_BOOKS_LIST_IS_EMPTY_OR_INVALID, null));
	}

}
