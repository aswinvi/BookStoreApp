package com.practice.store.service;

import static com.practice.store.constants.BookStoreConstants.CHECKOUT_IS_SUCCESSFUL;
import static com.practice.store.constants.BookStoreConstants.INSUFFICIENT_STOCK_FOR;
import static com.practice.store.constants.BookStoreConstants.NO_BOOKS_AVAILABLE_TO_CHECKOUT;
import static com.practice.store.constants.BookStoreConstants.PLEASE_RETRY_CHECKING_OUT_LATER;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;
import com.practice.store.CacheFactoryService;
import com.practice.store.config.BookStoreConfigLoader;
import com.practice.store.exception.BookStoreIllegalArgumentException;
import com.practice.store.model.Basket;
import com.practice.store.model.Book;
import com.practice.store.model.BookStore;

@Service
public class CheckoutService extends BookManagementService {

	private static ILogger logger = Logger.getLogger(BookManagementService.class);

	private BookStoreConfigLoader configLoader;
	private DiscountService discountService;

	public CheckoutService(BookStoreConfigLoader configLoader, DiscountService discountService) {
		this.configLoader = configLoader;
		this.discountService = discountService;
	}

	public boolean executeCheckout(Basket basket) {

		return paymentGateway(checkoutBasket(basket));

	}

	private boolean paymentGateway(double price) {
		logger.info("Payment completed for amount  : " + price);
		return true;
	}

	public double checkoutBasket(Basket basket) {

		List<Book> booksAvailable = configLoader.getLoadedData().map(BookStore::getBooksInTheStore)
				.orElseThrow(() -> new BookStoreIllegalArgumentException(NO_BOOKS_AVAILABLE_TO_CHECKOUT));

		List<Book> basketBooks = new ArrayList<>();

		booksAvailable = checkAndUpdateAvailableBooksToCheckout(basket, booksAvailable, basketBooks);

		double totalPrice = discountService.calculateBasketPrice(basketBooks);

		basket.getBooks().clear();

		configLoader.setBooksInToStore(booksAvailable);

		return totalPrice;
	}

	List<Book> checkAndUpdateAvailableBooksToCheckout(Basket basket, List<Book> booksAvailable,
			List<Book> basketBooks) {

		for (Map.Entry<Book, Integer> entry : basket.getBooks().entrySet()) {
			Book book = entry.getKey();
			int quantity = entry.getValue();

			validateStock(booksAvailable, book, quantity);
			addBooksToBasketListBasedOnQuantity(basketBooks, book, quantity);

			booksAvailable = updateBooksInStorePostSale(book, booksAvailable);
		}
		return booksAvailable;
	}

	private void validateStock(List<Book> booksAvailable, Book book, int quantity) {
		if (!isBookInStock(book, quantity, booksAvailable)) {
			throw new BookStoreIllegalArgumentException(INSUFFICIENT_STOCK_FOR + book.getTitle());
		}
	}

	private void addBooksToBasketListBasedOnQuantity(List<Book> basketBooks, Book book, int quantity) {
		for (int i = 0; i < quantity; i++) {
			basketBooks.add(book);
		}
	}
	
	public String setCheckOutStatusAndClearCache(String sessionId, boolean checkoutStatus, CacheFactoryService cache) {
		if (checkoutStatus) {
			cache.deleteBasketFromCache(sessionId);
			return CHECKOUT_IS_SUCCESSFUL;
		} else {
			return PLEASE_RETRY_CHECKING_OUT_LATER;
		}
	}

	@Override
	public void updateStore(Book bookToUpdate) {
		// TODO Auto-generated method stub

	}

}
