package com.github.groupa.client.modules;

import javax.inject.Singleton;
import javax.swing.JMenuBar;

import retrofit.RestAdapter;

import com.github.groupa.client.Library;
import com.github.groupa.client.RESTErrorHandler;
import com.github.groupa.client.SingleLibrary;
import com.github.groupa.client.factories.ImageObjectFactory;
import com.github.groupa.client.gui.panels.IRootPanel;
import com.github.groupa.client.gui.panels.RootPanel;
import com.github.groupa.client.main.Application;
import com.github.groupa.client.main.Main;
import com.github.groupa.client.main.MenuBar;
import com.github.groupa.client.servercommunication.RESTService;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class NewModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(Library.class).to(SingleLibrary.class).in(Singleton.class);
		bind(EventBus.class).in(Singleton.class);
		bind(IRootPanel.class).to(RootPanel.class);

		install(new FactoryModuleBuilder().build(ImageObjectFactory.class));
	}

	@Provides
	@Singleton
	private RESTService provideRESTService() {
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(Application.BASEURL)
				.setErrorHandler(new RESTErrorHandler()).build();

		return restAdapter.create(RESTService.class);
	}

	@Provides
	private JMenuBar provideMenuBar() {
		MenuBar menuBar = Main.injector.getInstance(MenuBar.class);

		return menuBar.getMenuBar();
	}
}
