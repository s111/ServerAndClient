package com.github.groupa.client;

import java.net.ConnectException;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

public class RESTErrorHandler implements ErrorHandler {
	@Override
	public Throwable handleError(RetrofitError error) {
		Throwable cause = error.getCause();

		String message = null;

		if (cause != null) {
			message = cause.getMessage();
		}

		if (error.getCause() instanceof ConnectException) {
			return new ConnectException(message);
		}

		return new UnknownError(message);
	}

}
