package com.github.groupa.client.main;

import org.apache.log4j.BasicConfigurator;

import com.github.groupa.client.modules.NewModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
	public static Injector injector;

	public static void main(String[] args) {
		BasicConfigurator.configure();

		injector = Guice.createInjector(new NewModule());

		Application application = injector.getInstance(Application.class);
		application.run();
	}
}
