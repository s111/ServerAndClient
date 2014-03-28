package com.github.groupa.client.core;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.groupa.client.ConstrainedLibrary;
import com.github.groupa.client.Library;
import com.github.groupa.client.ImageObject;
import com.github.groupa.client.SingleLibrary;
import com.github.groupa.client.events.LibraryAddEvent;
import com.github.groupa.client.helpers.MockImageObject;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class LibraryTest {
	private static LibraryTest test;
	private static SingleLibrary singleLibrary;
	private static ConstrainedLibrary constrainedLibrary;
	private static EventBus eventBus;
	private ImageObject expectedImage = null;

	@BeforeClass
	public static void setup() {
		eventBus = new EventBus();
		singleLibrary = new SingleLibrary(eventBus);
		test = new LibraryTest();
		eventBus.register(test);
	}

	public void setupConstrainedLibrary() {
		constrainedLibrary = new ConstrainedLibrary(eventBus, singleLibrary);
	}

	@Subscribe
	public void libraryAddListener(LibraryAddEvent event) {
		if (expectedImage != null)
			assertEquals(expectedImage, event.getImage());
	}

	@Test
	public void testAddAndSize() {
		test.cleanup();
		assertEquals(0, singleLibrary.imageCount());
		assertNull(singleLibrary.getImage(-1));
		assertNull(singleLibrary.getImage(0));
		for (int i = 1; i <= 3; i++) {
			ImageObject img = MockImageObject.get(i, null, null, null, null, 0);
			singleLibrary.add(img);
			assertEquals(i, singleLibrary.imageCount());
			assertEquals(img, singleLibrary.getImage(i - 1));
		}
		assertNull(singleLibrary.getImage(3));
		test.cleanup();
	}

	@Test
	public void testEventBus() {
		test.cleanup();
		test.testEventBusAdd(singleLibrary);

		test.cleanup();
		test.setupConstrainedLibrary();
		test.testEventBusAdd(constrainedLibrary);
	}

	@Test
	public void testSortedLibrary() {
		test.cleanup();
		test.setupConstrainedLibrary();

		constrainedLibrary.sort(ConstrainedLibrary.SORT_RATING_ASC);
		constrainedLibrary.add(MockImageObject
				.get(1, null, null, null, null, 0));
		constrainedLibrary.add(MockImageObject
				.get(2, null, null, null, null, 3));
		constrainedLibrary.add(MockImageObject
				.get(3, null, null, null, null, 2));
		constrainedLibrary.add(MockImageObject
				.get(4, null, null, null, null, 3));
		constrainedLibrary.add(MockImageObject
				.get(5, null, null, null, null, 5));
		constrainedLibrary.add(MockImageObject
				.get(6, null, null, null, null, 1));

		while (constrainedLibrary.imageCount() != 6)
			;

		assertEquals(6, constrainedLibrary.imageCount());
		assertEquals(1, constrainedLibrary.getImage(0).getId());
		assertEquals(6, constrainedLibrary.getImage(1).getId());
		assertEquals(3, constrainedLibrary.getImage(2).getId());
		assertEquals(2, constrainedLibrary.getImage(3).getId());
		assertEquals(4, constrainedLibrary.getImage(4).getId());
		assertEquals(5, constrainedLibrary.getImage(5).getId());

		constrainedLibrary.sort(ConstrainedLibrary.SORT_RATING_DESC);

		assertEquals(5, constrainedLibrary.getImage(0).getId());
		assertEquals(2, constrainedLibrary.getImage(1).getId());
		assertEquals(4, constrainedLibrary.getImage(2).getId());
		assertEquals(3, constrainedLibrary.getImage(3).getId());
		assertEquals(6, constrainedLibrary.getImage(4).getId());
		assertEquals(1, constrainedLibrary.getImage(5).getId());

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
		singleLibrary.clear();
	}
}
