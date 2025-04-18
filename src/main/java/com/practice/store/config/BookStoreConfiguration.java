package com.practice.store.config;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import com.practice.store.exception.BookStoreIOException;

import jakarta.annotation.PreDestroy;

@Configuration
public class BookStoreConfiguration {

	private static Logger logger = Logger.getLogger("BookStoreConfiguration");

	private ILoader bookStoreConfigLoader;

	public BookStoreConfiguration(@Qualifier("bookStoreConfigLoader") ILoader bookStoreConfigLoader) {
		this.bookStoreConfigLoader = bookStoreConfigLoader;
	}

	@PreDestroy
	void initialize() throws BookStoreIOException {

		setAppStartUpStatus(checkIfAllConfigurationLoadedSuccessfully());

	}

	private void setAppStartUpStatus(boolean isConfigLoaded) throws BookStoreIOException {
		if (isConfigLoaded) {
			logger.info("Configs are Loaded Succesfully!!! App can process incoming requests");
		} else {
			throw new BookStoreIOException(
					"Error while Loading Configs!!! App should not process incoming requests >> ");
		}
	}

	private boolean checkIfAllConfigurationLoadedSuccessfully() {

		return bookStoreConfigLoader.isConfigLoaded();

	}
}