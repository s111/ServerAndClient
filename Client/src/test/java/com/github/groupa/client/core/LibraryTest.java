package com.github.groupa.client.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.events.LibraryAddEvent;
import com.github.groupa.client.events.LibraryRemoveEvent;
import com.github.groupa.client.helpers.MockImageObject;
import com.github.groupa.client.library.DescriptionConstraint;
import com.github.groupa.client.library.Library;
import com.github.groupa.client.library.LibraryConstraint;
import com.github.groupa.client.library.TagConstraint;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class LibraryTest {
	private static Library rootLibrary;
	private static Library subLibrary;
	private static EventBus eventBus;
	private ImageObject expectedImage;
	private Library expectedLibrary;
	private boolean expectRootFirst;
	private List<ImageObject> expectedImages;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		eventBus = new EventBus();
	}

	@Before
	public void setUp() throws Exception {
		rootLibrary = new Library(eventBus);
		subLibrary = new Library(rootLibrary);
		expectedImage = null;
		expectedLibrary = null;
		expectRootFirst = false;
		expectedImages = new ArrayList<>();
	}

	@Subscribe
	public void libraryAddListener(LibraryAddEvent event) {
		if (expectedImage != null) {
			assertEquals(expectedImage, event.getImage());
			expectedImage = null;
		}
		if (expectedLibrary != null) {
			if (expectRootFirst) {
				assertSame(rootLibrary, event.getLibrary());
				expectRootFirst = false;
			} else {
				assertSame(expectedLibrary, event.getLibrary());
				expectedLibrary = null;
			}
		}
		if (!expectedImages.isEmpty()) {
			assertNotNull(event.getImages());
			assertEquals(expectedImages.size(), event.getImages().size());
			assertTrue(event.getImages().containsAll(expectedImages));
		}
	}

	@Subscribe
	public void libraryRemoveListener(LibraryRemoveEvent event) {
		if (expectedImage != null) {
			assertEquals(expectedImage, event.getImage());
			expectedImage = null;
		}
		if (!expectedImages.isEmpty()) {
			assertNotNull(event.getImages());
			assertEquals(expectedImages.size(), event.getImages().size());
			assertTrue(event.getImages().containsAll(expectedImages));
		}
	}

	@Test
	public void testEventBusAddRoot() {
		eventBus.register(this);
		for (int i = 1; i <= 3; i++) {
			expectedImage = MockImageObject.get(i, null, null, null, 0);
			expectedLibrary = rootLibrary;
			rootLibrary.add(expectedImage);
		}
		assertEquals(3, rootLibrary.size());
		eventBus.unregister(this);
	}

	@Test
	public void testEventBusAddSub() {
		eventBus.register(this);
		for (int i = 1; i <= 3; i++) {
			expectedImage = MockImageObject.get(i, null, null, null, 0);
			expectedLibrary = subLibrary;
			expectRootFirst = true;
			subLibrary.add(expectedImage);
		}
		assertEquals(3, subLibrary.size());
		eventBus.unregister(this);
	}

	@Test
	public void testEventBusAddAll() {
		eventBus.register(this);
		List<ImageObject> list = new ArrayList<>();
		for (int i = 1; i <= 3; i++) {
			list.add(MockImageObject.get(i, null, null, null, 0));
		}
		expectedImages.addAll(list);
		rootLibrary.addAll(list);
		assertEquals(3, rootLibrary.size());
		eventBus.unregister(this);
	}

	@Test
	public void testEventBusRemove() {
		List<ImageObject> list = new ArrayList<>();
		for (int i = 1; i <= 3; i++) {
			list.add(MockImageObject.get(i, null, null, null, 0));
		}
		rootLibrary.addAll(list);
		assertEquals(3, rootLibrary.size());

		eventBus.register(this);
		expectedImage = list.remove(0);
		eventBus.post(new LibraryRemoveEvent(rootLibrary, expectedImage));
		assertEquals(2, rootLibrary.size());
		expectedImages.addAll(list);
		eventBus.post(new LibraryRemoveEvent(rootLibrary, list));
		assertEquals(0, rootLibrary.size());
		eventBus.unregister(this);
	}

	@Test
	public void testConstraints() {
		ArrayList<String> tag1 = new ArrayList<>();
		tag1.add("tag1");
		ArrayList<String> tag2 = new ArrayList<>();
		tag2.add("tag2");
		subLibrary.add(MockImageObject.get(1, "tagged image", tag1, null, 0));
		subLibrary.add(MockImageObject.get(2, "tagged image 2", tag2, null, 0));
		subLibrary
				.add(MockImageObject.get(3, "described image", null, null, 0));
		subLibrary.add(MockImageObject.get(4, null, null, null, 0));

		assertEquals(4, rootLibrary.size());
		assertEquals(4, subLibrary.size());
		subLibrary.addConstraint(new DescriptionConstraint(true));
		assertEquals(4, rootLibrary.size());
		assertEquals(3, subLibrary.size());
		for (LibraryConstraint c : subLibrary.getConstraints()) {
			if (!(c instanceof DescriptionConstraint))
				continue;
			subLibrary.removeConstraint(c);
		}
		assertEquals(4, subLibrary.size());
		subLibrary.addConstraint(new DescriptionConstraint(true));
		subLibrary.addConstraint(new TagConstraint("tag2"));
		assertEquals(4, rootLibrary.size());
		assertEquals(1, subLibrary.size());
	}
}
