package com.github.groupa.client.modules;

import javax.inject.Singleton;
import javax.swing.JMenuBar;

import retrofit.RestAdapter;

import com.github.groupa.client.ActiveImage;
import com.github.groupa.client.Library;
import com.github.groupa.client.RESTErrorHandler;
import com.github.groupa.client.components.ZoomSlider;
import com.github.groupa.client.factories.ImageObjectFactory;
import com.github.groupa.client.gui.MenuBar;
import com.github.groupa.client.gui.panels.ImagePanel;
import com.github.groupa.client.gui.panels.MainPanel;
import com.github.groupa.client.gui.panels.RootPanel;
import com.github.groupa.client.gui.panels.ThumbPanel;
import com.github.groupa.client.main.Application;
import com.github.groupa.client.main.Main;
import com.github.groupa.client.servercommunication.RESTService;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class DIModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(Library.class).in(Singleton.class);
		bind(EventBus.class).in(Singleton.class);
		bind(ThumbPanel.class).in(Singleton.class);
		bind(ActiveImage.class).in(Singleton.class);
		bind(ZoomSlider.class).in(Singleton.class);
		bind(RootPanel.class).to(MainPanel.class);
		bind(ImagePanel.class).in(Singleton.class);

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
