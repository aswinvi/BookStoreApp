package com.practice.store.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.practice.store.api.response.BookStoreResponse;
import com.practice.store.model.Book;
import com.practice.store.service.BookStoreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Books Store Controller", description = "Operations pertaining to the Book Store configuration and available books")
public class BooksStoreController {

	private static final String HTTP_STATUS_500 = "500";
	private static final String HTTP_STATUS_204 = "204";
	private static final String HTTP_STATUS_200 = "200";

	private BookStoreService bookStoreService;

	public BooksStoreController(BookStoreService bookStoreService) {
		this.bookStoreService = bookStoreService;
	}

	@Operation(summary = "Retrieve the list of books in the store", description = "This endpoint returns the current BookStore configuration including the list of books.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = HTTP_STATUS_200, description = "Successfully retrieved the book list"),
			@ApiResponse(responseCode = HTTP_STATUS_204, description = "No Content - configuration not loaded"),
			@ApiResponse(responseCode = HTTP_STATUS_500, description = "Internal Server Error") })
	@GetMapping(value = "/bookStore", produces = "application/json")
	public ResponseEntity<BookStoreResponse> browseBookListInTheStore() {

		List<Book> books = bookStoreService.getBooksFromStore();
		if (books.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(new BookStoreResponse("Successfully retrieved books from the store.", books));

	}

	@Operation(summary = "Add a single Book into the Store", description = "This service helps to add new books into the store")
	@ApiResponses(value = {
			@ApiResponse(responseCode = HTTP_STATUS_200, description = "Successfully added the books into the Store"),
			@ApiResponse(responseCode = HTTP_STATUS_204, description = "No Content - configuration not loaded"),
			@ApiResponse(responseCode = HTTP_STATUS_500, description = "Internal Server Error") })
	@PostMapping(value = "/addBook", produces = "application/json")
	public ResponseEntity<BookStoreResponse> addNewBooksInToTheStore(@RequestBody Book newBook) {

		bookStoreService.addNewBookIntoStore(newBook);

		BookStoreResponse response = new BookStoreResponse("Succesfully added the new Book into the Store !!! ",
				bookStoreService.getBooksFromStore());

		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Add a list of New Books into the Store", description = "This service helps to add new books into the store")
	@ApiResponses(value = {
			@ApiResponse(responseCode = HTTP_STATUS_200, description = "Successfully added the books into the Store"),
			@ApiResponse(responseCode = HTTP_STATUS_204, description = "No Content - configuration not loaded"),
			@ApiResponse(responseCode = HTTP_STATUS_500, description = "Internal Server Error") })
	@PostMapping(value = "/addBookList", produces = "application/json")
	public ResponseEntity<BookStoreResponse> addNewBooksInToTheStore(@RequestBody List<Book> newBooks) {

		bookStoreService.addBooks(newBooks);

		BookStoreResponse response = new BookStoreResponse("Succesfully added the new Book into the Store !!! ",
				bookStoreService.getBooksFromStore());

		return ResponseEntity.ok(response);
	}

}
