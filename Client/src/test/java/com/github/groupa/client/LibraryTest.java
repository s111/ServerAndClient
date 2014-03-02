package com.github.groupa.client;

import static org.junit.Assert.*;

import java.awt.Image;
import java.io.IOException;

import org.junit.Test;

public class LibraryTest {

	@Test
	public void addLocalImagesToLibrary() {
		Image img1 = null;
		Image img2 = null;
		ImageObject imgObject1;
		ImageObject imgObject2;
		Library lib;
		Requester request = new FakeImageRequester();
		
		try {
			img1 = request.requestImage(1);
			img2 = request.requestImage(2);
		} catch (IOException e) {
			fail("FakeImageRequester error");
		}
		
		assertEquals("Invalid count of images", Library.size(), 0);
		
		imgObject1 = Library.add(1, img1);
		lib = new Library();
		assertNull("Nonexisting image returned",lib.getImage(2));
		imgObject2 = Library.add(2, img2);

		assertEquals("Invalid count of images", Library.size(), 2);
		
		assertSame(imgObject1, lib.getImage(1));
		assertSame(imgObject2, lib.getImage(2));
		assertSame(imgObject1, lib.getPrevImage());
		assertSame(imgObject2, lib.getNextImage());
		assertNull("Nonexisting image returned", lib.getNextImage());
	}
}
