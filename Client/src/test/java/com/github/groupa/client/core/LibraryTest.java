package com.github.groupa.client.core;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.groupa.client.Library;
import com.github.groupa.client.ImageObject;
import com.github.groupa.client.LibraryConstraint;
import com.github.groupa.client.LibrarySort;
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
		assertEquals(0, rootLibrary.imageCount());
		assertNull(rootLibrary.getImage(-1));
		assertNull(rootLibrary.getImage(0));
		for (int i = 1; i <= 3; i++) {
			ImageObject img = MockImageObject.get(i, null, null, null, null, 0);
			rootLibrary.add(img);
			assertEquals(i, rootLibrary.imageCount());
			assertEquals(img, rootLibrary.getImage(i - 1));
		}
		assertNull(rootLibrary.getImage(3));
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
		subLibrary.add(MockImageObject.get(1, "tagged image", null, tag1, null, 0));
		subLibrary.add(MockImageObject.get(2, "tagged image 2", null, tag2, null, 0));
		subLibrary.add(MockImageObject.get(3, "described image", null, null, null, 0));
		subLibrary.add(MockImageObject.get(4, null, null, null, null, 0));

		assertEquals(4, rootLibrary.imageCount());
		assertEquals(4, subLibrary.imageCount());
		subLibrary.addConstraint(new LibraryConstraint(LibraryConstraint.HAS_DESCRIPTION));
		assertEquals(4, rootLibrary.imageCount());
		assertEquals(3, subLibrary.imageCount());
		subLibrary.addConstraint(new LibraryConstraint(LibraryConstraint.HAS_TAG, "tag2"));
		assertEquals(4, rootLibrary.imageCount());
		assertEquals(1, subLibrary.imageCount());
		assertEquals(2, subLibrary.getImage(0).getId());

	}

	@Test
	public void testSortedLibrary() {
		test.cleanup();
		test.setupSubLibrary();

		subLibrary.sort(LibrarySort.SORT_RATING_ASC);
		subLibrary.add(MockImageObject
				.get(1, null, null, null, null, 0));
		subLibrary.add(MockImageObject
				.get(2, null, null, null, null, 3));
		subLibrary.add(MockImageObject
				.get(3, null, null, null, null, 2));
		subLibrary.add(MockImageObject
				.get(4, null, null, null, null, 3));
		subLibrary.add(MockImageObject
				.get(5, null, null, null, null, 5));
		subLibrary.add(MockImageObject
				.get(6, null, null, null, null, 1));

		assertEquals(6, subLibrary.imageCount());
		assertEquals(1, subLibrary.getImage(0).getId());
		assertEquals(6, subLibrary.getImage(1).getId());
		assertEquals(3, subLibrary.getImage(2).getId());
		assertEquals(2, subLibrary.getImage(3).getId());
		assertEquals(4, subLibrary.getImage(4).getId());
		assertEquals(5, subLibrary.getImage(5).getId());

		subLibrary.sort(LibrarySort.SORT_RATING_DESC);

		assertEquals(5, subLibrary.getImage(0).getId());
		assertEquals(2, subLibrary.getImage(1).getId());
		assertEquals(4, subLibrary.getImage(2).getId());
		assertEquals(3, subLibrary.getImage(3).getId());
		assertEquals(6, subLibrary.getImage(4).getId());
		assertEquals(1, subLibrary.getImage(5).getId());

		test.cleanup();
	}

	private void testEventBusAdd(Library lib) {
		for (int i = 1; i <= 3; i++) {
			test.expectedImage = MockImageObject.get(i, null, null, null, null,
					0);
			lib.add(expectedImage);
		}
	}

	private void cleanup() {
		test.expectedImage = null;
		rootLibrary.clear();
	}
}
