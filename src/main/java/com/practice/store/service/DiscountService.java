package com.practice.store.service;

import static com.practice.store.constants.BookStoreConstants.DEFAULT_PRICE;
import static com.practice.store.constants.BookStoreConstants.FIVE_SET_OF_BOOKS;
import static com.practice.store.constants.BookStoreConstants.FOUR_SET_OF_BOOKS;
import static com.practice.store.constants.BookStoreConstants.OFFER_FOR_10_PERCENTAGE;
import static com.practice.store.constants.BookStoreConstants.OFFER_FOR_20_PERCENTAGE;
import static com.practice.store.constants.BookStoreConstants.OFFER_FOR_25_PERCENTAGE;
import static com.practice.store.constants.BookStoreConstants.OFFER_FOR_5_PERCENTAGE;
import static com.practice.store.constants.BookStoreConstants.THREE_SET_OF_BOOKS;
import static com.practice.store.constants.BookStoreConstants.TWO_SET_OF_BOOKS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.practice.store.model.Book;

@Service
public class DiscountService extends BookManagementService {

	public double calculateBasketPrice(List<Book> basketBooks) {

		double totalPrice = 0.0;

		while (exitIfBasketIsEmpty(basketBooks)) {
			Set<Book> uniqueSet = createSetOfDistinctBooks(basketBooks);

			totalPrice = calculateFinalPriceWithDiscount(basketBooks, uniqueSet);

			basketBooks.clear();
		}

		return totalPrice;
	}

	private boolean exitIfBasketIsEmpty(List<Book> basketBooks) {
		return !basketBooks.isEmpty();
	}

	private double calculateFinalPriceWithDiscount(List<Book> basketBooks, Set<Book> uniqueSet) {

		double setPrice = getPriceForBookSetHavingDiscount(uniqueSet);

		double remainingBooksPrice = getPriceForRemainingBooks(basketBooks, uniqueSet);

		setPrice = applyDiscount(uniqueSet.size(), setPrice);

		return remainingBooksPrice + setPrice;
	}

	double applyDiscount(int setSize, double setPrice) {
		setPrice -= setPrice * getDiscountPercentage(setSize);
		return setPrice;
	}

	private double getPriceForRemainingBooks(List<Book> basketBooks, Set<Book> uniqueSet) {
		return basketBooks.stream().filter(book -> notInUniqueSet(uniqueSet, book)).mapToDouble(Book::getPrice).sum();
	}

	private boolean notInUniqueSet(Set<Book> uniqueSet, Book book) {
		return !uniqueSet.contains(book);
	}

	double getPriceForBookSetHavingDiscount(Set<Book> uniqueSet) {
		return uniqueSet.stream().mapToDouble(Book::getPrice).sum();
	}

	protected Set<Book> createSetOfDistinctBooks(List<Book> books) {
		Set<Book> uniqueSet = new HashSet<>();
		for (Book book : books) {
			if (uniqueSet.stream().noneMatch(existingBook -> isSameBook(existingBook, book))) {
				uniqueSet.add(book);
			}
			if (uniqueSet.size() == FIVE_SET_OF_BOOKS)
				break;
		}
		return uniqueSet;
	}

	double getDiscountPercentage(int setSize) {
		switch (setSize) {
		case TWO_SET_OF_BOOKS:
			return OFFER_FOR_5_PERCENTAGE;
		case THREE_SET_OF_BOOKS:
			return OFFER_FOR_10_PERCENTAGE;
		case FOUR_SET_OF_BOOKS:
			return OFFER_FOR_20_PERCENTAGE;
		case FIVE_SET_OF_BOOKS:
			return OFFER_FOR_25_PERCENTAGE;
		default:
			return DEFAULT_PRICE;
		}
	}

	@Override
	public void updateStore(Book bookToUpdate) {
		// TODO Auto-generated method stub

	}
}
