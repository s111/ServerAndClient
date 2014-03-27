package com.github.groupa.client;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.groupa.client.modules.NewModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static Injector injector;

	public static void main(String[] args) {
		BasicConfigurator.configure();

		injector = Guice.createInjector(new NewModule());

		Application application = injector.getInstance(Application.class);

		logger.info("Starting application");

		application.start();
	}
}