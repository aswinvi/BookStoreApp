package com.practice.store.api.response;

import com.practice.store.model.Basket;

public class BooksOrderDetailsResponse {

	private String message;

	private Basket basket;

	public BooksOrderDetailsResponse(String message, Basket basket) {
		this.message = message;
		this.basket = basket;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Basket getBasket() {
		return basket;
	}

	public void setBasket(Basket basket) {
		this.basket = basket;
	}
}
