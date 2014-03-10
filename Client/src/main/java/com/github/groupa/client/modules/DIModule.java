package com.github.groupa.client.modules;

import javax.inject.Singleton;

import com.github.groupa.client.Application;
import com.github.groupa.client.Library;
import com.github.groupa.client.MainFrame;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;

public class DIModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(Application.class).in(Singleton.class);
		bind(EventBus.class).in(Singleton.class);
		bind(MainFrame.class).in(Singleton.class);
		bind(Library.class).in(Singleton.class);
	}
}
