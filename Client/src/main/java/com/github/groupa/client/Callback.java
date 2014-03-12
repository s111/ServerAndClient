package com.github.groupa.client;

public interface Callback<T> {
	public void success(T t);

	public void failure();
}
