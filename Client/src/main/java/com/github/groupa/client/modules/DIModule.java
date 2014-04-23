package com.github.groupa.client.modules;

import java.lang.reflect.Type;
import java.sql.Timestamp;

import javax.inject.Singleton;
import javax.swing.JMenuBar;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import com.github.groupa.client.ImageListFetcher;
import com.github.groupa.client.RESTErrorHandler;
import com.github.groupa.client.ThreadPool;
import com.github.groupa.client.components.ZoomSlider;
import com.github.groupa.client.factories.ImageObjectFactory;
import com.github.groupa.client.gui.MenuBar;
import com.github.groupa.client.gui.panels.ImagePanel;
import com.github.groupa.client.gui.panels.ImageSidebarPanel;
import com.github.groupa.client.gui.panels.MainPanel;
import com.github.groupa.client.gui.panels.RootPanel;
import com.github.groupa.client.gui.panels.ThumbPanel;
import com.github.groupa.client.library.Library;
import com.github.groupa.client.main.Application;
import com.github.groupa.client.main.Main;
import com.github.groupa.client.servercommunication.ModifyImage;
import com.github.groupa.client.servercommunication.RESTService;
import com.github.groupa.client.servercommunication.ServerConnection;
import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class DIModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(Library.class).in(Singleton.class);
		bind(EventBus.class).in(Singleton.class);
		bind(ThumbPanel.class).in(Singleton.class);
		bind(ZoomSlider.class).in(Singleton.class);
		bind(RootPanel.class).to(MainPanel.class);
		bind(ImagePanel.class).in(Singleton.class);
		bind(ImageSidebarPanel.class).in(Singleton.class);
		bind(ThreadPool.class).in(Singleton.class);
		bind(ServerConnection.class).in(Singleton.class);
		bind(ModifyImage.class).in(Singleton.class);
		bind(ImageListFetcher.class).in(Singleton.class);
		install(new FactoryModuleBuilder().build(ImageObjectFactory.class));
	}

	@Provides
	@Singleton
	private RESTService provideRESTService() {
		JsonDeserializer<Timestamp> timestampDeserializer = createTimestampDeserializer();

		Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,
				timestampDeserializer).create();

		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(Application.BASEURL)
				.setConverter(new GsonConverter(gson))
				.setErrorHandler(new RESTErrorHandler()).build();

		return restAdapter.create(RESTService.class);
	}

	@Provides
	private JMenuBar provideMenuBar() {
		MenuBar menuBar = Main.injector.getInstance(MenuBar.class);

		return menuBar.getMenuBar();
	}

	private JsonDeserializer<Timestamp> createTimestampDeserializer() {
		return new JsonDeserializer<Timestamp>() {
			@Override
			public Timestamp deserialize(JsonElement json, Type type,
					JsonDeserializationContext context)
					throws JsonParseException {
				return new Timestamp(json.getAsJsonPrimitive().getAsLong());
			}
		};
	}
}
