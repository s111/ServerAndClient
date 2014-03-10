package com.github.groupa.client.modules;

import javax.inject.Singleton;

import retrofit.RestAdapter;

import com.github.groupa.client.Application;
import com.github.groupa.client.Library;
import com.github.groupa.client.MainFrame;
import com.github.groupa.client.RESTErrorHandler;
import com.github.groupa.client.servercommunication.RESTService;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class DIModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(Application.class).in(Singleton.class);
		bind(EventBus.class).in(Singleton.class);
		bind(MainFrame.class).in(Singleton.class);
		bind(Library.class).in(Singleton.class);
	}

	@Provides
	@Singleton
	private RESTService provideRESTService() {
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(Application.serverAPIBaseURL)
				.setErrorHandler(new RESTErrorHandler()).build();

		return restAdapter.create(RESTService.class);
	}
}
