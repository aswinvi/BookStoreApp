package com.practice.store.config;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.practice.store.exception.BookStoreIOException;
import com.practice.store.model.Book;
import com.practice.store.model.BookStore;

import jakarta.annotation.PostConstruct;

@Component("bookStoreConfigLoader")
public class BookStoreConfigLoader implements ILoader {

	private String filePath = "./BookShelf.json";

	Optional<BookStore> booksInTheStore = Optional.empty();

	private boolean loaderStatus = false;

	Gson gson = new Gson();

	public BookStoreConfigLoader() {
		//
	}

	@PostConstruct
	public void initialize() throws BookStoreIOException {
		loaderStatus = load();
	}

	@Override
	public boolean load() throws BookStoreIOException {
		try {

			booksInTheStore = readBooksDetailsFromBookStore(getInputStreamOf(filePath));

		} catch (Exception e) {
			throw new BookStoreIOException("EXCEPTION>> While Loading the Book store config File :" + e);
		}

		return booksInTheStore.isPresent();
	}

	private Optional<BookStore> readBooksDetailsFromBookStore(InputStream inputStream) {
		if (null == inputStream) {
			return Optional.empty();
		}

		InputStreamReader reader = new InputStreamReader(inputStream);
		return Optional.ofNullable(gson.fromJson(reader, BookStore.class));
	}

	private InputStream getInputStreamOf(String filePath) throws IOException {
		return new ClassPathResource(filePath).getInputStream();
	}

	@Override
	public Optional<BookStore> getLoadedData() {
		return booksInTheStore;
	}

	public void setBooksInToStore(List<Book> books) {
		this.booksInTheStore.get().setBooksInTheStore(books);
	}

	public void writeBackJsonToFilePath() throws BookStoreIOException {

		try (FileWriter writer = new FileWriter(filePath)) {

			gson.toJson(booksInTheStore.get(), writer);
		} catch (IOException e) {
			throw new BookStoreIOException("Writing into the Book store configuration has failed !!");
		}

	}

	@Override
	public boolean isConfigLoaded() {
		return loaderStatus;
	}

}
