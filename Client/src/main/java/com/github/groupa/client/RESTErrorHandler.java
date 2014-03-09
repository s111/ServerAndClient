package com.github.groupa.client;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

public class RESTErrorHandler implements ErrorHandler {
	@Override
	public Throwable handleError(RetrofitError error) {
		Throwable cause = error.getCause();

		return cause;
	}

}
