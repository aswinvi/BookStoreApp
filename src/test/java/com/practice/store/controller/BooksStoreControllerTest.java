package com.practice.store.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.practice.store.api.response.BookStoreResponse;
import com.practice.store.model.Book;
import com.practice.store.service.BookStoreService;

class BooksStoreControllerTest {

	@InjectMocks
	private BooksStoreController booksStoreController;

	private MockMvc mockMvc;

	@Mock
	private BookStoreService bookStoreService;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(booksStoreController).build();
	}

	@Order(value = 1)
	@Test
	void shouldReturnListOfBooksPresentInTheStore() {
		List<Book> books = List.of(new Book("Book1", "Author1", 50, 2023, 1),
				new Book("Book2", "Author2", 50, 2019, 1));
		when(bookStoreService.getBooksFromStore()).thenReturn(books);

		ResponseEntity<BookStoreResponse> response = booksStoreController.browseBookListInTheStore();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Successfully retrieved books from the store.", response.getBody().getMessage());
		assertEquals(2, response.getBody().getBooks().size());
	}

	@Order(value = 2)
	@Test
	void shouldReturnNoContentErrorCodeWhenBooksNotPresentInTheStore() {
		when(bookStoreService.getBooksFromStore()).thenReturn(Collections.emptyList());

		ResponseEntity<BookStoreResponse> response = booksStoreController.browseBookListInTheStore();

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Order(value = 3)
	@Test
	void shouldReturnOKIfBooksAddedSuccesfullyInToTheStore() {
		Book newBook = new Book("New Book", "New Author", 50, 1989, 1);
		when(bookStoreService.getBooksFromStore()).thenReturn(List.of(newBook));

		ResponseEntity<BookStoreResponse> response = booksStoreController.addNewBooksInToTheStore(newBook);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Succesfully added the new Book into the Store !!! ", response.getBody().getMessage());
		assertEquals(1, response.getBody().getBooks().size());
	}

	@Order(value = 4)
	@Test
	void shouldReturnOKIfListOfBooksAddedSuccesfullyInToTheStore() {
		List<Book> newBooks = List.of(new Book("Book1", "Author1", 50, 1920, 1),
				new Book("Book2", "Author2", 50, 1987, 1));
		when(bookStoreService.getBooksFromStore()).thenReturn(newBooks);

		ResponseEntity<BookStoreResponse> response = booksStoreController.addNewBooksInToTheStore(newBooks);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Succesfully added the new Book into the Store !!! ", response.getBody().getMessage());
		assertEquals(2, response.getBody().getBooks().size());
	}
	
	@Order(value = 5)
	@Test
	void shouldReturnOKIfBookRemovedSuccesfullyFromTheStore() {
		List<Book> newBooks = List.of(new Book("Book1", "Author1", 50, 1920, 1),
				new Book("Book2", "Author2", 50, 1987, 1));
		when(bookStoreService.getBooksFromStore()).thenReturn(newBooks);

		ResponseEntity<BookStoreResponse> response = booksStoreController.removeBooksFromTheStore(newBooks);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Successfully removed the book from the Store !!!", response.getBody().getMessage());
		assertEquals(2, response.getBody().getBooks().size());
	}
	
	@Order(value = 6)
	@Test
	void shouldReturnOKIfBooksRemovedSuccesfullyFromTheStore() {
		Book newBook = new Book("New Book", "New Author", 50, 1989, 1);
		when(bookStoreService.getBooksFromStore()).thenReturn(List.of(newBook));

		ResponseEntity<BookStoreResponse> response = booksStoreController.removeBooksFromTheStore(newBook);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Successfully removed the book from the Store !!!", response.getBody().getMessage());
		assertEquals(1, response.getBody().getBooks().size());
	}


	
}
