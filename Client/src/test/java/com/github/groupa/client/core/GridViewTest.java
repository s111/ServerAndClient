package com.github.groupa.client.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;

import org.junit.Test;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Main;
import com.github.groupa.client.SingleLibrary;
import com.github.groupa.client.modules.DIModule;
import com.github.groupa.client.views.GridView;
import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class GridViewTest {

	@Test
	public void test() {
		Injector injector = Guice.createInjector(new DIModule());
		SingleLibrary library = injector.getInstance(SingleLibrary.class);
		GridView gridView = injector.getInstance(GridView.class);
		
		ImageObject mockImageObject = mock(ImageObject.class);
		//when(mockImageObject.addTag(anyString()));
		
		library.add(mockImageObject);
	}

}
