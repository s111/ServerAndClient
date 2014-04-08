package com.github.groupa.client.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.groupa.client.Callback;
import com.github.groupa.client.ImageObject;
import com.github.groupa.client.ServerConnection;
import com.github.groupa.client.helpers.MockImageInfo;
import com.github.groupa.client.helpers.MockServerConnection;
import com.github.groupa.client.jsonobjects.ImageFull;
import com.github.groupa.client.jsonobjects.ImageInfo;
import com.google.common.eventbus.EventBus;

public class ImageObjectTest {
	private static EventBus eventBus;
	private static List<ImageObject> images;
	private static ServerConnection serverConnection;
	
	@BeforeClass
	public static void beforeClass() {
		eventBus = new EventBus();
		images = new ArrayList<>();
	}
	
	@Before
	public void setUp() throws Exception {
		images.clear();
		serverConnection = MockServerConnection.get();
	}

	@Test
	public void testRating() throws InterruptedException {
		for (int i = 1; i <= 5; i++) {
			ImageObject img = new ImageObject(eventBus, serverConnection, i);
			ImageFull imageFull = new ImageFull();
			ImageInfo imageInfo = MockImageInfo.get(imageFull);
			when(serverConnection.getImageInfo(i)).thenReturn(imageInfo);
			
			assertEquals(0, img.getRating());
			when(serverConnection.rate(i, i)).thenReturn(true);
			img.rate(i);
			Thread.sleep(100);
			assertEquals(i, img.getRating());
		}
	}
	
	@Test
	public void testDescription() throws InterruptedException {
		for (int i = 1; i <= 5; i++) {
			ImageObject img = new ImageObject(eventBus, serverConnection, i);
			ImageFull imageFull = new ImageFull();
			ImageInfo imageInfo = MockImageInfo.get(imageFull);
			when(serverConnection.getImageInfo(i)).thenReturn(imageInfo);
			
			assertFalse(img.hasDescription());
			assertEquals("", img.getDescription());
			when(serverConnection.describe(i, "" + i)).thenReturn(true);
			img.describe("" + i);
			Thread.sleep(100);
			assertTrue(img.hasDescription());
			assertEquals("" + i, img.getDescription());
		}
	}
	
	@Test
	public void testTag() throws InterruptedException {
		for (int i = 1; i <= 5; i++) {
			ImageObject img = new ImageObject(eventBus, serverConnection, i);
			ImageFull imageFull = new ImageFull();
			ImageInfo imageInfo = MockImageInfo.get(imageFull);
			when(serverConnection.getImageInfo(i)).thenReturn(imageInfo);
			
			assertFalse(img.hasTag("" + i));
			assertEquals(0, img.getTags().size());
			when(serverConnection.addTag(i, "" + i)).thenReturn(true);
			img.addTag("" + i);
			Thread.sleep(100);
			assertTrue(img.hasTag("" + i));
			assertEquals(1, img.getTags().size());
		}
	}

	@Test
	public void testImage() throws InterruptedException {
		final ImageObject img = new ImageObject(eventBus, serverConnection, 1);
		final BufferedImage imageCompressed = mock(BufferedImage.class);
		assertFalse(img.hasImage());
		assertFalse(img.hasImageRaw());
		assertNull(img.getImage());
		when(serverConnection.getImage(1, "compressed")).thenReturn(imageCompressed);
		img.loadImage(new Callback<BufferedImage>() {
			public void success(BufferedImage t) {
				assertEquals(imageCompressed, t);
				assertEquals(imageCompressed, img.getImage());
			}
			public void failure() {
				fail();
			}
		}, "compressed");
		Thread.sleep(100);
		assertTrue(img.hasImage());
		assertEquals(imageCompressed, img.getImage());
		

		final BufferedImage imageXs = mock(BufferedImage.class);
		assertFalse(img.hasThumb("xs"));
		assertNull(img.getThumb("xs"));
		when(serverConnection.getImage(1, "xs")).thenReturn(imageXs);
		img.loadImage(new Callback<BufferedImage>() {
			public void success(BufferedImage t) {
				assertEquals(imageXs, t);
				assertEquals(imageXs, img.getThumb("xs"));
			}
			public void failure() {
				fail();
			}
		}, "xs");
		Thread.sleep(100);
		assertTrue(img.hasThumb("xs"));
		assertEquals(imageXs, img.getThumb("xs"));
	}

}
