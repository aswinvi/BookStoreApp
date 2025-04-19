package com.practice.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.practice.store.model.Book;

class DiscountServiceTest {

	@InjectMocks
	private DiscountService discountService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void shouldReturnCorrectTotalAfterAdjustingDiscount() {

		List<Book> basketBooks = new ArrayList<>();
		basketBooks.add(new Book("Book1", "Author1", 50.00, 1987, 1));
		basketBooks.add(new Book("Book2", "Author2", 50.00, 2000, 2));
		basketBooks.add(new Book("Book3", "Author3", 50.00, 1999, 2));
		basketBooks.add(new Book("Book4", "Author4", 50.00, 1959, 2));

		double totalPrice = discountService.calculateBasketPrice(basketBooks);

		assertTrue(totalPrice > 0);
		assertEquals(160, totalPrice);
	}

	@Test
	void shouldReducePriceCorrectlyAfterApplyingDiscount() {

		double initialPrice = 100.00;
		int setSize = 3;
		double discountedPrice = discountService.applyDiscount(setSize, initialPrice);

		assertEquals(90.00, discountedPrice);
	}

	@Test
	void shouldReturnCorrectPercentage() {

		assertEquals(0.05, discountService.getDiscountPercentage(2));
		assertEquals(0.10, discountService.getDiscountPercentage(3));
		assertEquals(0.20, discountService.getDiscountPercentage(4));
		assertEquals(0.25, discountService.getDiscountPercentage(5));
	}

	@Test
	void shouldTotalPriceForBookSetHavingDiscount() {
		Set<Book> uniqueSet = Set.of(new Book("Book1", "Author1", 50.00, 1987, 1),
				new Book("Book2", "Author2", 30.00, 2000, 1));

		double setPrice = discountService.getPriceForBookSetHavingDiscount(uniqueSet);

		assertEquals(80.00, setPrice);
	}

	@Test
	void shouldReturnUniqueBookSetFromList() {
		List<Book> basketBooks = new ArrayList<>();

		basketBooks.add(new Book("Book1", "Author1", 50.00, 1987, 1));
		basketBooks.add(new Book("Book2", "Author2", 30.00, 2000, 2));
		basketBooks.add(new Book("Book1", "Author1", 50.00, 1987, 1));

		Set<Book> uniqueSet = discountService.createSetOfDistinctBooks(basketBooks);

		assertEquals(2, uniqueSet.size());
	}
}
