package com.practice.store.config;

import java.util.Optional;

import com.practice.store.exception.BookStoreIOException;

public interface ILoader {

	public boolean load() throws BookStoreIOException;

	public boolean isConfigLoaded();

	public <T> Optional<T> getLoadedData();
}
