package com.github.groupa.client.core;
import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.Library;
import com.github.groupa.client.SingleLibrary;


public class SingleLibraryTest {
	private SingleLibrary library;
	
	public void testSetup() {
		library = new SingleLibrary();
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

	private ImageObject getMockImage(long id) {
		return new MockImageObject(id);
	}
	
	private void clearLibrary() {
		library.clear();
	}
}
