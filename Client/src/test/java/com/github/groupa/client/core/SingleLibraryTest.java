package com.github.groupa.client.core;
import static org.junit.Assert.*;

import org.junit.Test;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.SingleLibrary;
import com.github.groupa.client.events.LibraryAddEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;


public class SingleLibraryTest {
	private SingleLibrary library;
	private EventBus eventBus;
	private ImageObject expectedImage = null;
	
	public void testSetup() {
		eventBus = new EventBus();
		library = new SingleLibrary(eventBus);
	}
	
	public void testCleanup() {
		clearLibrary();
		library = null;
	}
	
	@Test
	public void test() {
		testSetup();
		testAddAndSize();
		testEventBus();
		testCleanup();
	}
	
	@Subscribe
	public void libraryAddListener(LibraryAddEvent event) {
		assertEquals(expectedImage, event.getImage());
		assertEquals(library, event.getLibrary());
	}
	
	private void testAddAndSize() {
		clearLibrary();
		assertEquals(0, library.imageCount());
		assertNull(library.getImage(-1));
		assertNull(library.getImage(0));
		for (int i = 1; i <= 3; i++) {
			ImageObject img = getMockImage(i);
			library.add(img);
			assertEquals(i, library.imageCount());
			assertEquals(img, library.getImage(i - 1));
		}
		assertNull(library.getImage(3));
	}

	private void testEventBus() {
		eventBus.register(this);
		clearLibrary();
		for (int i = 0; i < 3; i++) {
			expectedImage = getMockImage(i);
			library.add(expectedImage);
		}
	}
	
	private ImageObject getMockImage(long id) {
		return new MockImageObject(id);
	}
	
	private void clearLibrary() {
		library.clear();
	}
}
