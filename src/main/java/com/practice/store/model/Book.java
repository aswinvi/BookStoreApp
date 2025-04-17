package com.practice.store.model;

import java.io.Serializable;

public class Book implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;
	private String author;
	private double price;
	private int year;
	private int quantity;

	public Book(String title, String author, double price, int year, int quantity) {
		this.title = title;
		this.author = author;
		this.price = price;
		this.year = year;
		this.quantity = quantity;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public double getPrice() {
		return price;
	}

	public int getYear() {
		return year;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Book [title=" + title + ", author=" + author + ", price=" + price + ", year=" + year + ", quantity="
				+ quantity + "]";
	}
}