package com.github.groupa.client;

import java.net.ConnectException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

public class RESTErrorHandler implements ErrorHandler {
	private static final Logger logger = LoggerFactory
			.getLogger(RESTErrorHandler.class);

	@Override
	public Throwable handleError(RetrofitError error) {
		if (error.getCause() instanceof ConnectException)
			return new ConnectException();

		if (error.getResponse() != null) {
			logger.error("Server response status code: "
					+ error.getResponse().getStatus());
		}

		return error;
	}

}