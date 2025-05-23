package com.practice.store.controller;

import static com.practice.store.constants.BookStoreConstants.HTTP_STATUS_200;
import static com.practice.store.constants.BookStoreConstants.HTTP_STATUS_204;
import static com.practice.store.constants.BookStoreConstants.HTTP_STATUS_500;
import static com.practice.store.constants.BookStoreConstants.SUCCESFULLY_ADDED_THE_NEW_BOOK_INTO_THE_STORE;
import static com.practice.store.constants.BookStoreConstants.SUCCESSFULLY_REMOVED_THE_BOOK;
import static com.practice.store.constants.BookStoreConstants.SUCCESSFULLY_RETRIEVED_BOOKS;

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
		return ResponseEntity.ok(new BookStoreResponse(SUCCESSFULLY_RETRIEVED_BOOKS, books));

	}

	@Operation(summary = "Add a single Book into the Store", description = "This service helps to add new books into the store")
	@ApiResponses(value = {
			@ApiResponse(responseCode = HTTP_STATUS_200, description = "Successfully added the books into the Store"),
			@ApiResponse(responseCode = HTTP_STATUS_204, description = "No Content - configuration not loaded"),
			@ApiResponse(responseCode = HTTP_STATUS_500, description = "Internal Server Error") })
	@PostMapping(value = "/addBook", produces = "application/json")
	public ResponseEntity<BookStoreResponse> addNewBooksInToTheStore(@RequestBody Book newBook) {

		bookStoreService.addNewBookIntoStore(newBook);

		BookStoreResponse response = new BookStoreResponse(SUCCESFULLY_ADDED_THE_NEW_BOOK_INTO_THE_STORE,
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

		BookStoreResponse response = new BookStoreResponse(SUCCESFULLY_ADDED_THE_NEW_BOOK_INTO_THE_STORE,
				bookStoreService.getBooksFromStore());

		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "Remove a single Book from the Store", description = "This service helps to remove the books from the store")
	@ApiResponses(value = {
			@ApiResponse(responseCode = HTTP_STATUS_200, description = "Successfully removed the book from the Store"),
			@ApiResponse(responseCode = HTTP_STATUS_500, description = "Internal Server Error") })
	@PostMapping(value = "/removeBook", produces = "application/json")
	public ResponseEntity<BookStoreResponse> removeBooksFromTheStore(@RequestBody Book booktoUpdate) {

		bookStoreService.removeSingleBook(booktoUpdate);

		BookStoreResponse response = new BookStoreResponse(SUCCESSFULLY_REMOVED_THE_BOOK,
				bookStoreService.getBooksFromStore());

		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "Remove a list of New Books from the Store", description = "This service helps to remove the books from the store")
	@ApiResponses(value = {
			@ApiResponse(responseCode = HTTP_STATUS_200, description = "Successfully removed the books from the Store"),
			@ApiResponse(responseCode = HTTP_STATUS_500, description = "Internal Server Error") })
	@PostMapping(value = "/removeBookList", produces = "application/json")
	public ResponseEntity<BookStoreResponse> removeBooksFromTheStore(@RequestBody List<Book> bookstoUpdate) {

		bookStoreService.removeBooks(bookstoUpdate);

		BookStoreResponse response = new BookStoreResponse(SUCCESSFULLY_REMOVED_THE_BOOK,
				bookStoreService.getBooksFromStore());

		return ResponseEntity.ok(response);
	}
	

}
