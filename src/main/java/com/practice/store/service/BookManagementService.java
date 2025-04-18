package com.practice.store.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.practice.store.exception.BookStoreIllegalArgumentException;
import com.practice.store.model.Book;

public abstract class BookManagementService {

	public Optional<Book> findBookInStore(Book bookToFind, List<Book> booksFromTheStore) {
		return booksFromTheStore.stream().filter(bookInTheStore -> isSameBook(bookInTheStore, bookToFind)).findFirst();
	}

	public boolean isSameBook(Book bookInTheStore, Book bookToFind) {
		return bookInTheStore.getTitle().equalsIgnoreCase(bookToFind.getTitle())
				&& bookInTheStore.getAuthor().equalsIgnoreCase(bookToFind.getAuthor())
				&& bookInTheStore.getYear() == bookToFind.getYear();
	}

	public boolean hasNoValue(List<Book> books) {
		return books == null || books.isEmpty();
	}

	protected Book addBookQuantity(Book existingBook, Book newBook) {
		existingBook.setQuantity(existingBook.getQuantity() + newBook.getQuantity());
		return existingBook;
	}

	protected List<Book> createNewBooksList(Book newBook) {
		List<Book> booksFromStore;
		booksFromStore = new ArrayList<>();
		booksFromStore.add(newBook);
		return booksFromStore;
	}

	public List<Book> updateBooksInStorePostSale(Book bookToUpdate, List<Book> booksFromStore) {
		if (hasNoValue(booksFromStore)) {
			booksFromStore = createNewBooksList(bookToUpdate);
		} else {
			Optional<Book> existingBook = findBookInStore(bookToUpdate, booksFromStore);

			if (existingBook.isPresent()) {
				booksFromStore = booksFromStore.stream()
						.map(bookInStore -> isSameBook(bookInStore, bookToUpdate)
								? updateBookQuantity(bookInStore, bookToUpdate)
								: bookInStore)
						.collect(Collectors.toList());
			} else {
				throw new BookStoreIllegalArgumentException("Book not found in store for update");
			}
		}
		return booksFromStore;
	}

	protected Book updateBookQuantity(Book existingBook, Book bookSold) {
		existingBook.setQuantity(Math.max(0, existingBook.getQuantity() - bookSold.getQuantity()));
		return existingBook;
	}

	protected void validateBooks(List<Book> books) {
		if (hasNoValue(books)) {
			throw new IllegalArgumentException("Books cannot be null or empty.");
		}
	}

	public abstract void updateStore(Book bookToUpdate);

}
