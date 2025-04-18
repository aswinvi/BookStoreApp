package com.practice.store.config;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.practice.store.exception.BookStoreIOException;

class BookStoreConfigurationTest {

	BookStoreConfiguration bookStoreConfig;

	@Mock
	BookStoreConfigLoader loaderMock;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		bookStoreConfig = new BookStoreConfiguration(loaderMock);
	}

	@Test
	void testConfigurationLoadedSuccessfully() {

		when(loaderMock.isConfigLoaded()).thenReturn(true);
		Assertions.assertDoesNotThrow(bookStoreConfig::initialize);
	}

	@Test
	void testConfigurationNotLoadedThrowsException() {
		when(loaderMock.isConfigLoaded()).thenReturn(false);

		Assertions.assertThrows(BookStoreIOException.class, bookStoreConfig::initialize);
	}
}