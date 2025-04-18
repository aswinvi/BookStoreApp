package com.practice.store.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Basket implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<Book, Integer> books = new HashMap<>();
	
	private double totalBasketValue;

    public void addBook(Book book, int quantity) {
        books.put(book, books.getOrDefault(book, 0) + quantity);
    }

    public Map<Book, Integer> getBooks() {
        return books;
    }

	public double getTotalBasketValue() {
		return totalBasketValue;
	}

	public void setTotalBasketValue(double totalBasketValue) {
		this.totalBasketValue = totalBasketValue;
	}
}