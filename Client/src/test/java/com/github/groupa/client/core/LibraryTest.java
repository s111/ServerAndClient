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
	private static Library library;
	private static EventBus eventBus;
	private ImageObject expectedImage;
	private List<ImageObject> expectedImages;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		eventBus = new EventBus();
	}

	@Before
	public void setUp() throws Exception {
		library = new Library(eventBus);
		expectedImage = null;
		expectedImages = new ArrayList<>();
	}

	@Subscribe
	public void libraryAddListener(LibraryAddEvent event) {
		if (expectedImage != null) {
			assertEquals(expectedImage, event.getImage());
			expectedImage = null;
		}
		if (!expectedImages.isEmpty()) {
			assertNotNull(event.getImages());
			assertEquals(expectedImages.size(), event.getImages().size());
			assertTrue(event.getImages().containsAll(expectedImages));
			expectedImages.clear();
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
	public void testEventBusAdd() {
		eventBus.register(this);
		for (int i = 1; i <= 3; i++) {
			expectedImage = MockImageObject.get(i, null, null, null, 0);
			library.add(expectedImage);
		}
		assertEquals(3, library.allImagesCount());
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
		library.addAll(list);
		assertEquals(3, library.allImagesCount());
		eventBus.unregister(this);
	}

	@Test
	public void testEventBusRemove() {
		List<ImageObject> list = new ArrayList<>();
		for (int i = 1; i <= 3; i++) {
			list.add(MockImageObject.get(i, null, null, null, 0));
		}
		library.addAll(list);

		eventBus.register(this);
		expectedImage = list.remove(0);
		library.remove(expectedImage);
		assertEquals(2, library.allImagesCount());
		expectedImage = null;
		
		expectedImages.addAll(list);
		library.removeAll(list);
		assertEquals(0, library.allImagesCount());
		eventBus.unregister(this);
	}

	@Test
	public void testConstraints() {
		ArrayList<String> tag1 = new ArrayList<>();
		tag1.add("tag1");
		ArrayList<String> tag2 = new ArrayList<>();
		tag2.add("tag2");
		library.add(MockImageObject.get(1, "tagged image", tag1, null, 0));
		library.add(MockImageObject.get(2, "tagged image 2", tag2, null, 0));
		library
				.add(MockImageObject.get(3, "described image", null, null, 0));
		library.add(MockImageObject.get(4, null, null, null, 0));

		assertEquals(4, library.allImagesCount());
		assertEquals(4, library.constrainedImagesCount());
		library.addConstraint(new DescriptionConstraint(true));
		assertEquals(4, library.allImagesCount());
		assertEquals(3, library.constrainedImagesCount());
		for (LibraryConstraint c : library.getConstraints()) {
			if (!(c instanceof DescriptionConstraint))
				continue;
			library.removeConstraint(c);
		}
		assertEquals(4, library.constrainedImagesCount());
		library.addConstraint(new DescriptionConstraint(true));
		library.addConstraint(new TagConstraint("tag2"));
		assertEquals(1, library.constrainedImagesCount());
	}
}
