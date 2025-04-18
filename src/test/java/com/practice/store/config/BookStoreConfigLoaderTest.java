package com.practice.store.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.practice.store.exception.BookStoreIOException;
import com.practice.store.model.Book;
import com.practice.store.model.BookStore;

class BookStoreConfigLoaderTest {

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