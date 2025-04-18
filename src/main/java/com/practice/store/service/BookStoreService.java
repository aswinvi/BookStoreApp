package com.practice.store.service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.practice.store.config.BookStoreConfigLoader;
import com.practice.store.exception.BookStoreIOException;
import com.practice.store.model.Book;
import com.practice.store.model.BookStore;

@Service
public class BookStoreService extends BookManagementService {

	private Logger logger = Logger.getLogger("BookStoreService");

	private BookStoreConfigLoader configLoader;

	public BookStoreService(BookStoreConfigLoader configLoader) {
		this.configLoader = configLoader;
	}

	public List<Book> getBooksFromStore() {
		Optional<BookStore> booksInStore = configLoader.getLoadedData();
		return booksInStore.map(BookStore::getBooksInTheStore).orElse(null);
	}

	public void addBooks(List<Book> newBooks) {
		newBooks.forEach(this::addNewBookIntoStore);
	}

	public void addNewBookIntoStore(Book newBook) {

		List<Book> booksFromStore = getBooksFromStore();

		if (hasNoValue(booksFromStore)) {
			booksFromStore = createNewBooksList(newBook);
		} else {
			Optional<Book> existingBook = findBookInStore(newBook, booksFromStore);
			if (existingBook.isPresent()) {
				booksFromStore = updateQuantityIfAddingSameBook(newBook, booksFromStore);
			} else {
				booksFromStore.add(newBook);
			}
		}
		configLoader.setBooksInToStore(booksFromStore);
		writeTheModifiedFileToJson();
	}

	private List<Book> updateQuantityIfAddingSameBook(Book newBook, List<Book> booksFromStore) {
		booksFromStore = booksFromStore.stream()
				.map(bookInStore -> isSameBook(bookInStore, newBook) ? addBookQuantity(bookInStore, newBook)
						: bookInStore)
				.collect(Collectors.toList());
		return booksFromStore;
	}

	private void writeTheModifiedFileToJson() {
		try {
			configLoader.writeBackJsonToFilePath();
		} catch (BookStoreIOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}
	
	public void removeBooks(List<Book> bookToUpdate) {
		bookToUpdate.forEach(this::updateStore);
		writeTheModifiedFileToJson();
	}
	
	public void removeSingleBook(Book bookToUpdate) {
		updateStore(bookToUpdate);
		writeTheModifiedFileToJson();
	}

	@Override
	public void updateStore(Book bookToUpdate) {
		configLoader.setBooksInToStore(updateBooksInStorePostSale(bookToUpdate, getBooksFromStore()));
	}

}
