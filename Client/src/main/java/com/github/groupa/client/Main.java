package com.github.groupa.client;

import com.github.groupa.client.modules.DIModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
	public static Injector injector;

	public static void main(String[] args) {
		injector = Guice.createInjector(new DIModule());

		Application application = injector.getInstance(Application.class);
		application.start();
	}
}