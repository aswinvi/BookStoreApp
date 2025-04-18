package com.practice.store.controller;

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

	private static final String HTTP_STATUS_500 = "500";
	private static final String HTTP_STATUS_200 = "200";

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
			@ApiResponse(responseCode = HTTP_STATUS_500, description = "Internal Server Error") })
	@PostMapping(value = "/addBooksToBasket", produces = "application/json")
	public ResponseEntity<BooksOrderDetailsResponse> addBooksToBasket(@RequestBody List<Book> books,
			@Parameter(description = "Session ID", required = true) @RequestParam String sessionId) {

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
			@ApiResponse(responseCode = HTTP_STATUS_200, description = "Successfully deleted the book from the basket"),
			@ApiResponse(responseCode = HTTP_STATUS_500, description = "Internal Server Error") })
	@PostMapping(value = "/removeBooks", produces = "application/json")
	public ResponseEntity<BooksOrderDetailsResponse> removeBooksFromBasket(@RequestBody List<Book> books,
			@Parameter(description = "Session ID", required = true) @RequestParam String sessionId) {

		if (basketService.hasNoValue(books)) {
			return formBadRequestError();
		}

		Basket basket = basketService.createBasket(cache.getBasketFromCache(sessionId));

		basketService.removeBooksFromBasket(basket, books);

		cache.addBasketToCache(sessionId, basket);

		BooksOrderDetailsResponse response = new BooksOrderDetailsResponse("Successfully removed books from Basket !!!",
				basket);

		return ResponseEntity.ok(response);
	}

	private ResponseEntity<BooksOrderDetailsResponse> formBadRequestError() {
		return ResponseEntity.badRequest()
				.body(new BooksOrderDetailsResponse("Error: The books list is empty or invalid.", null));
	}

}
