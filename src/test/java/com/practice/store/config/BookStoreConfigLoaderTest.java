package com.practice.store.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.gson.Gson;
import com.practice.store.exception.BookStoreIOException;
import com.practice.store.model.Book;
import com.practice.store.model.BookStore;

class BookStoreConfigLoaderTest {

	@InjectMocks
	BookStoreConfigLoader loader = new BookStoreConfigLoader();

	@Mock
	private Gson mockGson;

	@Mock
	private FileWriter fileWriter;

	@Mock
	Optional<BookStore> booksInTheStore;

	BookStoreConfigLoader configLoader;

	@BeforeEach
	void setup() {
		configLoader = new BookStoreConfigLoader();
	}

	@Test
	void shouldReturnTrueIfBooksAreLoadedSuccessfully() throws BookStoreIOException {

		configLoader.initialize();

		Assertions.assertTrue(configLoader.isConfigLoaded());
	}

	@Test
	void shouldThrowExceptionIfLoadFailure() {

		ReflectionTestUtils.setField(configLoader, "filePath", "./BookShel.json");

		Assertions.assertThrows(BookStoreIOException.class, configLoader::load);
	}

	@Test
	void shouldWriteSuccessfullyInToTheFilePath() throws Exception {

		String title = "The Clean coder";
		String author = "Robert Martin";
		double price = 50;
		int year = 2011;
		int quantity = 1;

		Book book = new Book(title, author, price, year, quantity);
		List<Book> books = new ArrayList<>();
		books.add(book);

		BookStore bookStore = new BookStore();

		bookStore.setBooksInTheStore(books);

		MockitoAnnotations.openMocks(this);

		when(booksInTheStore.get()).thenReturn(bookStore);

		doNothing().when(mockGson).toJson(any(BookStore.class), any(FileWriter.class));

		loader.writeBackJsonToFilePath();

		verify(mockGson, times(1)).toJson(any(BookStore.class), any(FileWriter.class));
	}

	@Test
	void shouldThrowExceptionDuringWritingError() {

		ReflectionTestUtils.setField(loader, "filePath", "");

		assertThrows(BookStoreIOException.class, () -> loader.writeBackJsonToFilePath());
	}

	@Test
	void shouldReturnTheSetBookStore() {

		String title = "The Clean coder";
		String author = "Robert Martin";
		double price = 50;
		int year = 2011;
		int quantity = 1;

		Book book = new Book(title, author, price, year, quantity);
		List<Book> books = new ArrayList<>();
		books.add(book);

		try {
			configLoader.load();
		} catch (BookStoreIOException e) {
			e.printStackTrace();
		}

		configLoader.setBooksInToStore(books);

		Optional<BookStore> result = configLoader.getLoadedData();

		assertThat(result.get().getBooksInTheStore().contains(book));
	}
}