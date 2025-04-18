package com.practice.store.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	protected boolean hasNoValue(List<Book> books) {
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

	public abstract void updateStore(Book bookToUpdate);

}
