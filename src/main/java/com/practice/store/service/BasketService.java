package com.practice.store.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.practice.store.model.Basket;
import com.practice.store.model.Book;

@Service
public class BasketService extends BookManagementService {

	public void addBooksToBasket(Basket basket, List<Book> books) {
		validateBooks(books);

		for (Book bookToAdd : books) {
			boolean bookExistsInBasket = false;

			bookExistsInBasket = updateQuantityOfExistingBooksInTheBasket(basket, bookToAdd);

			if (!bookExistsInBasket) {
				basket.getBooks().put(bookToAdd, bookToAdd.getQuantity());
			}
		}

	}

	private boolean updateQuantityOfExistingBooksInTheBasket(Basket basket, Book bookToAdd) {
		for (Book existingBook : basket.getBooks().keySet()) {
			if (isSameBook(existingBook, bookToAdd)) {
				addBookQuantity(existingBook, bookToAdd);
				basket.getBooks().put(existingBook, existingBook.getQuantity());
				return true;
			}
		}
		return false;
	}

	public void removeBooksFromBasket(Basket basket, List<Book> books) {
		validateBooks(books);
		for (Book bookToremove : books) {
			reduceQuantityOfExistingBooksInTheBasket(basket, bookToremove);
		}

	}

	private boolean reduceQuantityOfExistingBooksInTheBasket(Basket basket, Book bookToremove) {
		for (Book existingBook : basket.getBooks().keySet()) {
			if (isSameBook(existingBook, bookToremove)) {
				updateBookQuantity(existingBook, bookToremove);
				if (existingBook.getQuantity() == 0) {
					basket.getBooks().remove(existingBook);
				} else {
					basket.getBooks().replace(existingBook, existingBook.getQuantity());
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void updateStore(Book bookToUpdate) {
		// Inherited method
	}

}
