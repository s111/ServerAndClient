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
	private int expectedIdx = -1;
	
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
		testEdgeWrap();
		testActiveImage();
		testEventBus();
		testCleanup();
	}
	
	private void testAddAndSize() {
		clearLibrary();
		assertEquals(0, library.imageCount());
		for (int i = 1; i <= 3; i++) {
			library.add(getMockImage(i));
			assertEquals(i, library.imageCount());
		}
	}
	
	private void testEdgeWrap() {
		clearLibrary();
		assertNull(library.getActiveImage());
		ImageObject img[] = new ImageObject[3];
		for (int i = 0; i < 3; i++) {
			img[i] = getMockImage(i);
			library.add(img[i]);
		}
		for (int i = 1; i < 10; i++) {
			assertEquals(img[i % 3], library.getNextImage());
		}
		for (int i = 3; i > 0; i--) {
			for (int j = 2; j >= 0; j--)
				assertEquals(img[j], library.getPrevImage());
		}
	}
	
	private void testActiveImage() {
		clearLibrary();
		ImageObject img[] = new ImageObject[3];
		for (int i = 0; i < 3; i++) {
			img[i] = getMockImage(i);
			library.add(img[i]);
		}
		library.setActiveImage(2);
		assertEquals(img[2], library.getActiveImage());
		library.setActiveImage(img[1]);
		assertEquals(img[1], library.getActiveImage());
	}

	private void testEventBus() {
		eventBus.register(this);
		clearLibrary();
		for (expectedIdx = 0; expectedIdx < 3; expectedIdx++) {
			expectedImage = getMockImage(expectedIdx);
			library.add(expectedImage);
		}
	}
	
	@Subscribe
	public void libraryAddListener(LibraryAddEvent event) {
		assertEquals(expectedImage, event.getImage());
		assertEquals(expectedIdx, event.getIdx());
		assertEquals(library, event.getLibrary());
	}
	
	private ImageObject getMockImage(long id) {
		return new MockImageObject(id);
	}
	
	private void clearLibrary() {
		library.clear();
	}
}
