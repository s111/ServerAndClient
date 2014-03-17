package com.github.groupa.client.modules;

import javax.inject.Singleton;

import retrofit.RestAdapter;

import com.github.groupa.client.Application;
import com.github.groupa.client.ImageListFetcher;
import com.github.groupa.client.MainFrame;
import com.github.groupa.client.RESTErrorHandler;
import com.github.groupa.client.SingleLibrary;
import com.github.groupa.client.factories.ImageObjectFactory;
import com.github.groupa.client.servercommunication.RESTService;
import com.github.groupa.client.views.GridView;
import com.github.groupa.client.views.ImageView;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class DIModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(Application.class).in(Singleton.class);
		bind(EventBus.class).in(Singleton.class);
		bind(MainFrame.class).in(Singleton.class);
		bind(ImageView.class).in(Singleton.class);
		bind(GridView.class).in(Singleton.class);
		bind(SingleLibrary.class).in(Singleton.class);
		bind(ImageListFetcher.class).in(Singleton.class);

		install(new FactoryModuleBuilder().build(ImageObjectFactory.class));
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
