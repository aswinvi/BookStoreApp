package com.practice.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.practice.store.config.BookStoreConfigLoader;
import com.practice.store.exception.BookStoreIOException;
import com.practice.store.exception.BookStoreIllegalArgumentException;
import com.practice.store.model.Book;
import com.practice.store.model.BookStore;

class BookStoreServiceTest {

	@Mock
	private BookStoreConfigLoader configLoader;

	@Mock
	private BookStore mockBookStore;

	private List<Book> booksInTheStore;

	@InjectMocks
	private BookStoreService bookStoreService = new BookStoreService(configLoader);

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		booksInTheStore = new ArrayList<>();
		booksInTheStore.add(new Book("The Clean Coder", "Robert Martin", 50, 2011, 10));
		booksInTheStore.add(new Book("Clean Code", "Robert Martin", 45, 2008, 5));

		when(configLoader.getLoadedData()).thenReturn(Optional.ofNullable(mockBookStore));

		when(mockBookStore.getBooksInTheStore()).thenReturn(booksInTheStore);
	}

	@AfterEach
	void afterExecution() {
		booksInTheStore.clear();
	}

	@Test
	void shouldAddBrandNewBookInToTheStore() {
		Book newBook = new Book("Clean Architecture", "Robert Martin", 50, 2017, 3);

		bookStoreService.addNewBookIntoStore(newBook);

		assertTrue(mockBookStore.getBooksInTheStore().contains(newBook));
		assertEquals(3, newBook.getQuantity());
	}

	@Test
	void shouldAddListOfNewBooksInToTheStore() {
		Book newBook = new Book("Clean Architecture", "Robert Martin", 50, 2017, 3);
		Book secondBook = new Book("The Clean Coder", "Robert Martin", 50, 2011, 10);
		List<Book> books = new ArrayList<>();
		books.add(newBook);
		books.add(secondBook);
		bookStoreService.addBooks(books);

		assertEquals("The Clean Coder", booksInTheStore.get(0).getTitle());
		assertEquals("Clean Architecture", booksInTheStore.get(2).getTitle());
		assertEquals(3, newBook.getQuantity());
	}

	@Test
	void shouldAddToExistingQuantityIfNewBookAlreadyPresentIntheStore() {

		Book duplicateBook = new Book("The Clean Coder", "Robert Martin", 50, 2011, 5);

		bookStoreService.addNewBookIntoStore(duplicateBook);

		Book updatedBook = booksInTheStore.stream().filter(book -> book.getTitle().equals("The Clean Coder"))
				.findFirst().orElse(null);

		assertNotNull(updatedBook);
		assertEquals(15, updatedBook.getQuantity());
	}

	@Test
	void shouldFindBookFromTheStoreIfPresent() {

		Optional<Book> foundBook = bookStoreService
				.findBookInStore(new Book("Clean Code", "Robert Martin", 45, 2008, 1), booksInTheStore);

		assertTrue(foundBook.isPresent());
		assertEquals("Clean Code", foundBook.get().getTitle());
	}

	@Test
	void shouldReturnFalseIfBookNotFroundInTheStore() {
		Optional<Book> foundBook = bookStoreService
				.findBookInStore(new Book("Non-Existent Book", "Unknown Author", 100, 2020, 1), booksInTheStore);

		assertFalse(foundBook.isPresent());
	}

	@Test
	void shouldReturnIfBookNotFroundInTheStore() throws BookStoreIOException {

		Book newBook = new Book("Clean Architecture", "Robert Martin", 50, 2017, 3);

		List<Book> newBooks = new ArrayList<>();

		when(mockBookStore.getBooksInTheStore()).thenReturn(newBooks);

		bookStoreService.addNewBookIntoStore(newBook);

		Mockito.verify(configLoader, Mockito.times(1)).writeBackJsonToFilePath();

	}

	@Test
	void shouldReduceTheQuantityFromTheStoreAfterBooksSold() {

		Book bookSold = new Book("The Clean Coder", "Robert Martin", 50, 2011, 5);

		bookStoreService.updateStore(bookSold);

		Book updatedBook = booksInTheStore.stream().filter(book -> book.getTitle().equals("The Clean Coder"))
				.findFirst().orElse(null);

		assertNotNull(updatedBook);
		assertEquals(5, updatedBook.getQuantity());
	}

	@Test
	void shouldThrowExceptionIfBookNotFindInStoreToUpdate() {
		Book nonExistingBook = new Book("Non-Existent Book", "Unknown Author", 100, 2020, 1);

		assertThrows(BookStoreIllegalArgumentException.class, () -> bookStoreService.updateStore(nonExistingBook));
	}
}
