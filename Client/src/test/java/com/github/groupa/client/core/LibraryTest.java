package com.github.groupa.client.core;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.groupa.client.Library;
import com.github.groupa.client.ImageObject;
import com.github.groupa.client.LibraryConstraint;
import com.github.groupa.client.events.LibraryAddEvent;
import com.github.groupa.client.helpers.MockImageObject;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class LibraryTest {
	private static LibraryTest test;
	private static Library rootLibrary;
	private static Library subLibrary;
	private static EventBus eventBus;
	private ImageObject expectedImage = null;

	@BeforeClass
	public static void setup() {
		eventBus = new EventBus();
		rootLibrary = new Library(eventBus);
		test = new LibraryTest();
		eventBus.register(test);
	}

	public void setupSubLibrary() {
		subLibrary = new Library(rootLibrary);
	}

	@Subscribe
	public void libraryAddListener(LibraryAddEvent event) {
		if (expectedImage != null)
			assertEquals(expectedImage, event.getImage());
	}

	@Test
	public void testAddAndSize() {
		test.cleanup();
		assertEquals(0, rootLibrary.size());
		for (int i = 1; i <= 3; i++) {
			ImageObject img = MockImageObject.get(i, null, null, null, 0);
			rootLibrary.add(img);
			assertEquals(i, rootLibrary.size());
			assertTrue(rootLibrary.hasImage(img));
		}
		test.cleanup();
	}

	@Test
	public void testEventBus() {
		test.cleanup();
		test.testEventBusAdd(rootLibrary);

		test.cleanup();
		test.setupSubLibrary();
		test.testEventBusAdd(subLibrary);
	}
	
	@Test
	public void setupConstraints() {
		test.cleanup();
		test.setupSubLibrary();
		ArrayList<String> tag1 = new ArrayList<>();
		tag1.add("tag1");
		ArrayList<String> tag2 = new ArrayList<>();
		tag2.add("tag2");
		subLibrary.add(MockImageObject.get(1, "tagged image", tag1, null, 0));
		subLibrary.add(MockImageObject.get(2, "tagged image 2", tag2, null, 0));
		subLibrary.add(MockImageObject.get(3, "described image", null, null, 0));
		subLibrary.add(MockImageObject.get(4, null, null, null, 0));

		assertEquals(4, rootLibrary.size());
		assertEquals(4, subLibrary.size());
		subLibrary.addConstraint(new LibraryConstraint(LibraryConstraint.HAS_DESCRIPTION));
		assertEquals(4, rootLibrary.size());
		assertEquals(3, subLibrary.size());
		subLibrary.addConstraint(new LibraryConstraint(LibraryConstraint.HAS_TAG, "tag2"));
		assertEquals(4, rootLibrary.size());
		assertEquals(1, subLibrary.size());

	}

	private void testEventBusAdd(Library lib) {
		for (int i = 1; i <= 3; i++) {
			test.expectedImage = MockImageObject.get(i, null, null, null,
					0);
			lib.add(expectedImage);
		}
	}

	private void cleanup() {
		test.expectedImage = null;
		rootLibrary.clear();
	}
}
